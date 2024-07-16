package ht.lisa.app.ui.wallet.poplocaton;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.text.DecimalFormat;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import ht.lisa.app.R;
import ht.lisa.app.model.Poi;

public class MarkerDetailsDialog extends BottomSheetDialogFragment {

    private static final String POI_KEY = "poiKey";
    private static final String IS_SOG_KEY = "isSog";
    private static final String CURRENT_POSITION = "currentPosition";
    @BindView(R.id.distance_text)
    TextView distanceText;
    @BindView(R.id.distance_level)
    TextView distanceLevel;
    @BindView(R.id.place_img)
    ImageView placeImg;
    @BindView(R.id.place_name)
    TextView placeName;
    @BindView(R.id.address)
    TextView address;
    private Poi poi;
    private Poi currentPosition;
    private double distance;
    private Unbinder unbinder;
    private boolean isSogExpress;

    public static MarkerDetailsDialog newInstance(Poi poi, Poi currentPosition, boolean isSogExpress) {
        MarkerDetailsDialog markerDetailsDialog = new MarkerDetailsDialog();
        Bundle args = new Bundle();
        args.putSerializable(POI_KEY, poi);
        args.putSerializable(CURRENT_POSITION, currentPosition);
        args.putBoolean(IS_SOG_KEY, isSogExpress);
        markerDetailsDialog.setArguments(args);
        return markerDetailsDialog;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            poi = (Poi) getArguments().getSerializable(POI_KEY);
            currentPosition = (Poi) getArguments().getSerializable(CURRENT_POSITION);
            isSogExpress = getArguments().getBoolean(IS_SOG_KEY);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_bottom_marker_details, container,
                false);
        unbinder = ButterKnife.bind(this, view);

        address.setText(poi.getAddr());
        placeName.setText(poi.getLoc());
        distanceText.setText(String.format("%s km", new DecimalFormat("#.#").format(Poi.calculateByDistance(currentPosition, poi))));
        /*distanceLevel.setText(distance < 10 ? "Close" : "Long away");*/
        placeImg.setImageDrawable(isSogExpress ? (getResources().getDrawable(R.drawable.sogexpress_main)) : (getResources().getDrawable(R.drawable.mon_cash_main)));
        return view;

    }

    @OnClick(R.id.get_directions_button)
    void onGetDirectionsButtonClick() {
        String uri = "http://maps.google.com/maps?saddr=" + (currentPosition == null ? 0 : currentPosition.getLat()) + "," + (currentPosition == null ? 0 : currentPosition.getLng()) + "&daddr=" + (poi == null ? 0 : poi.getLat()) + "," + (poi == null ? 0 : poi.getLng());
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
        startActivity(intent);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}