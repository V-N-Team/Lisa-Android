package ht.lisa.app.ui.wallet.poplocaton;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.tabs.TabLayout;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import butterknife.BindView;
import ht.lisa.app.R;
import ht.lisa.app.model.Poi;
import ht.lisa.app.model.response.PoiMonCashResponse;
import ht.lisa.app.model.response.PoiSogexpressResponse;
import ht.lisa.app.ui.wallet.BaseWalletFragment;
import ht.lisa.app.util.RxUtil;
import io.reactivex.Observable;
import io.reactivex.functions.Consumer;

import static android.content.Context.LOCATION_SERVICE;
import static androidx.constraintlayout.widget.Constraints.TAG;

public class PopLocationFragment extends BaseWalletFragment implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener {
    public static final String SCREEN_NAME = "PopLocationsScreen";

    private static final int MONCASH_ORDER = 0;

    @BindView(R.id.pop_location_tab_layout)
    TabLayout popLocationTabLayout;

    private List<Poi> locations = new ArrayList<>();
    GoogleMap googleMap;
    private Poi currentPos;
    private Geocoder gcd;
    private boolean isSogExpress;
    private LocationManager locationManager;


    private int[] tabIcons = {R.drawable.ic_mon_cash_mini, R.drawable.ic_sogexpress_mini};
    private int[] tabTitles = {R.string.mon_cash, R.string.sogexpress};
    private List<Poi> mons = new ArrayList<>();
    private List<Poi> sogs = new ArrayList<>();

    @Override
    public View provideFragmentView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_pop_location, parent, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViewPager();
        walletPresenter.getPoiMoncash();
        walletPresenter.getPoiSogexpress();
        showProgress();

        gcd = new Geocoder(Objects.requireNonNull(getActivity()).getBaseContext(), Locale.getDefault());
        requestPermissionsIfNecessary(new String[]{
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        });

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
    }

    private OnTabSelectedListener onTabSelectedListener = new OnTabSelectedListener() {
        @Override
        public void initMap() {
            googleMap.clear();
            isSogExpress = onTabSelectedListener.getSelectedOrder() != MONCASH_ORDER;
            addMarkersToMap(isSogExpress ? sogs : mons);
        }
    };

    @SuppressLint("ClickableViewAccessibility")
    private void initViewPager() {
        popLocationTabLayout.addOnTabSelectedListener(onTabSelectedListener);
        setupTabIcons();
    }

    private void setupTabIcons() {
        for (int i = 0; i < popLocationTabLayout.getTabCount(); i++) {
            TextView tab_text = (TextView) LayoutInflater.from(getContext()).inflate(R.layout.custom_tab, null);
            tab_text.setText(getStringFromResource(tabTitles[i]));
            tab_text.setCompoundDrawablesWithIntrinsicBounds(0, 0, tabIcons[i], 0);
            popLocationTabLayout.getTabAt(i).setCustomView(tab_text);
        }
    }

    @Override
    public void getData(Object object) {
        if (object instanceof PoiMonCashResponse) {
            PoiMonCashResponse response = (PoiMonCashResponse) object;
            mons = response.getPoiMoncCash();
            if (!isSogExpress) {
                onTabSelectedListener.initMap();
            }
        } else if (object instanceof PoiSogexpressResponse) {
            PoiSogexpressResponse response = (PoiSogexpressResponse) object;
            sogs = response.getPoiSogexpress();
            if (isSogExpress) {
                onTabSelectedListener.initMap();
            }
        }
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        RxUtil.asyncConsumer(Observable.just(googleMap), new Consumer<GoogleMap>() {
            @Override
            public void accept(GoogleMap googleMap) throws Exception {

            }
        });
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(getActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                buildGoogleApiClient();
                googleMap.setMyLocationEnabled(true);
            }
        } else {
            buildGoogleApiClient();
            googleMap.setMyLocationEnabled(true);
        }
        locationManager = (LocationManager) getActivity().getSystemService(LOCATION_SERVICE);
        if (locationManager != null) {
            Criteria criteria = new Criteria();
            String bestProvider = locationManager.getBestProvider(criteria, true);
            if (bestProvider != null) {
                if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                Location location = locationManager.getLastKnownLocation(bestProvider);
                if (location != null && location.getLatitude() != 0 && location.getLongitude() != 0) {
                    currentPos = new Poi(location.getLatitude(), location.getLongitude());
                }
                locationManager.requestLocationUpdates(bestProvider, 1000, 100, this);
            }
        }
        LatLng haiti = new LatLng(18.513815, -72.288193);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(haiti, 7f));
        if (locations.size() != 0) {
            addMarkersToMap(locations);
        }
    }


    public void requestPermissionsIfNecessary(String[] permissions) {
        ArrayList<String> permissionsToRequest = new ArrayList<>();
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(getActivity(), permission)
                    != PackageManager.PERMISSION_GRANTED) {
                permissionsToRequest.add(permission);
            }
        }
        if (permissionsToRequest.size() > 0) {
            requestPermissions(
                    permissionsToRequest.toArray(new String[0]),
                    55);
        }
    }


    protected synchronized void buildGoogleApiClient() {
        GoogleApiClient googleApiClient = new GoogleApiClient.Builder(getActivity())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        googleApiClient.connect();
        if (locationManager != null) {
            Criteria criteria = new Criteria();
            String bestProvider = locationManager.getBestProvider(criteria, true);
            if (bestProvider != null) {
                if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                Location location = locationManager.getLastKnownLocation(bestProvider);
                if (location != null && location.getLatitude() != 0 && location.getLongitude() != 0) {
                    currentPos = new Poi(location.getLatitude(), location.getLongitude());
                }
                locationManager.requestLocationUpdates(bestProvider, 1000, 100, this);
            }
        }
    }

    private void addMarkersToMap(List<Poi> locations) {
        Bitmap icon = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.custom_bank_marker), 88, 104, true);
        if (googleMap != null) {
            showProgress();
            for (Poi poi1 : locations) {
                googleMap.addMarker(new MarkerOptions()
                        .position(new LatLng(poi1.getLat(), poi1.getLng()))
                        .icon(BitmapDescriptorFactory.fromBitmap(icon)));
                googleMap.setOnMarkerClickListener(marker -> {
                    for (Poi poi : locations) {
                        if (marker.getPosition().latitude == poi.getLat() && marker.getPosition().longitude == poi.getLng()) {
                            MarkerDetailsDialog.newInstance(poi, currentPos, isSogExpress).show(getFragmentManager(),
                                    MarkerDetailsDialog.class.getSimpleName());
                            break;
                        }
                    }

                    return true;
                });
            }
            hideProgress();
        }

    }

    @Override
    public void onLocationChanged(Location location) {
        String longitude = "Longitude: " + location.getLongitude();
        Log.v(TAG, longitude);
        String latitude = "Latitude: " + location.getLatitude();
        Log.v(TAG, latitude);
        List<Address> addresses;
        try {
            addresses = gcd.getFromLocation(location.getLatitude(),
                    location.getLongitude(), 1);
            if (addresses.size() > 0) {
                System.out.println(addresses.get(0).getLocality());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        currentPos = new Poi(location.getLatitude(), location.getLongitude());
    }


    @Override
    public void onStop() {
        super.onStop();
        if (locationManager != null) {
            locationManager.removeUpdates(this);
        }
    }


    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(1000);
        locationRequest.setFastestInterval(1000);
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
