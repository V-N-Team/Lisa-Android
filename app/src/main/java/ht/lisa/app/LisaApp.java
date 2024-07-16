package ht.lisa.app;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.lifecycle.ProcessLifecycleOwner;
import androidx.multidex.MultiDexApplication;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.onesignal.OSNotificationReceivedEvent;
import com.onesignal.OneSignal;

import org.greenrobot.eventbus.EventBus;
import org.joda.time.Days;
import org.joda.time.LocalDate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import ht.lisa.app.model.Avatar;
import ht.lisa.app.model.Message;
import ht.lisa.app.model.Phone;
import ht.lisa.app.model.User;
import ht.lisa.app.model.response.MessageResponse;
import ht.lisa.app.network.CallbackListener;
import ht.lisa.app.network.RequestManager;
import ht.lisa.app.ui.main.GetMessageIdSetEvent;
import ht.lisa.app.ui.main.SuccessPaymentEvent;
import ht.lisa.app.util.Analytics;
import ht.lisa.app.util.Constants;
import ht.lisa.app.util.RxUtil;
import ht.lisa.app.util.SharedPreferencesUtil;

import static ht.lisa.app.model.Message.MSG_TYPE_PAYMENT_SUCCESS;
import static ht.lisa.app.util.Settings.loadSettingsHelper;

public class LisaApp extends MultiDexApplication implements LifecycleObserver {
    private static final String ONESIGNAL_APP_ID = "ba19520f-527f-4410-812f-a8fa8f2b8963";

    private static LisaApp instance;
    private User user;
    private String bolotoJackpot;
    private ArrayList<Avatar> avatars;
    private ArrayList<String> readonlyFields;
    private long sessionStartTime;
    private boolean needToCalculateAppSpeed = true;

    private static final String ONE_SIGNAL_TAG = "ONESIGNALTAG";

    public static LisaApp getInstance() {
        return instance;
    }

    public String getLanguage(Context context) {
        return TextUtils.isEmpty(SharedPreferencesUtil.getLanguage()) ?
                context.getResources().getString(R.string.locale) : SharedPreferencesUtil.getLanguage();
    }

    public void setLanguage(String language, Context activity) {
        SharedPreferencesUtil.setLanguage(language);

        Locale defaultLocale;
        if (language.equals(Constants.LANGUAGE_KR)) {
            defaultLocale = new Locale("none");
        } else {
            defaultLocale = new Locale(language);
        }
        Locale.setDefault(defaultLocale);
        Configuration config = activity.getResources().getConfiguration();
        config.locale = defaultLocale;
        activity.getResources().updateConfiguration(config,
                activity.getResources().getDisplayMetrics());
        onConfigurationChanged(config);
    }

    public long getSessionStartTime() {
        return sessionStartTime;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        user = new User();
        avatars = new ArrayList<>();
        instance = this;
        loadSettingsHelper(this);
        //initGetUserListRequest();
        ProcessLifecycleOwner.get().getLifecycle().addObserver(this);
        initOneSignal();
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

    }

    public void authorize(String token) {
        if (token != null) {
            //LisaApp.getInstance().bindOneSignal();
        } else {
            LisaApp.getInstance().unbindOneSignal();
        }
        SharedPreferencesUtil.setToken(token);
    }

    public void bindOneSignal() {
        if (OneSignal.getDeviceState() != null && SharedPreferencesUtil.isAuthorized()) {
            String playerId = OneSignal.getDeviceState().getUserId();
            if (SharedPreferencesUtil.getPhone() == null || playerId == null) return;
            RxUtil.networkConsumer(
                    RequestManager.getInstance().bindOneSignal(new Phone(SharedPreferencesUtil.getPhone()), playerId),
                    responseBody -> Log.d(ONE_SIGNAL_TAG, "bound"),
                    throwable -> Log.d(ONE_SIGNAL_TAG, "error binding"));
        }
    }

    public void unbindOneSignal() {
        if (SharedPreferencesUtil.getPhone() == null) return;
        RxUtil.networkConsumer(
                RequestManager.getInstance().unbindOneSignal(new Phone(SharedPreferencesUtil.getPhone())),
                responseBody -> Log.d(ONE_SIGNAL_TAG, "unbound"),
                throwable -> Log.d(ONE_SIGNAL_TAG, "error unbinding"));
    }

    public void initOneSignal() {
        // Enable verbose OneSignal logging to debug issues if needed.
        OneSignal.setLogLevel(OneSignal.LOG_LEVEL.VERBOSE, OneSignal.LOG_LEVEL.NONE);

        // OneSignal Initialization
        OneSignal.initWithContext(this);
        OneSignal.setAppId(ONESIGNAL_APP_ID);

        OneSignal.setNotificationWillShowInForegroundHandler(new OneSignal.OSNotificationWillShowInForegroundHandler() {
            @Override
            public void notificationWillShowInForeground(OSNotificationReceivedEvent notificationReceivedEvent) {
                initGetUserListRequest();
            }
        });

        updateAnalytics();
    }

    private void updateAnalytics() {
        Bundle bundle = new Bundle();
        if (SharedPreferencesUtil.isFirstAppLaunch()) {
            SharedPreferencesUtil.setSessionsDate(new Date().getTime());
            SharedPreferencesUtil.setVisits(1);
        } else {
            if (needToUpdateDailyStats()) {
                bundle.putInt(Analytics.VISITS, SharedPreferencesUtil.getVisits());
                SharedPreferencesUtil.setVisits(1);
                SharedPreferencesUtil.setSessionsDate(new Date().getTime());
            } else {
                SharedPreferencesUtil.setVisits(SharedPreferencesUtil.getVisits() + 1);
            }

            if (SharedPreferencesUtil.getLastSessionDuration() != 0) {
                bundle.putLong(Analytics.SESSION_DURATION, SharedPreferencesUtil.getLastSessionDuration());
            }
        }

        if (ContextCompat.checkSelfPermission(getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
            Location location = null;
            if (locationManager != null) {
                location = locationManager.getLastKnownLocation(Objects.requireNonNull(locationManager.getBestProvider(new Criteria(), false)));
            }
            if (location != null) {
                LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                bundle.putString(Analytics.LOCATION, latLng.toString());
            }
        }

        if (bundle.size() > 0)
            FirebaseAnalytics.getInstance(this)
                    .logEvent(FirebaseAnalytics.Event.APP_OPEN, bundle);

        SharedPreferencesUtil.setLastSessionDuration(0);
    }

    private boolean needToUpdateDailyStats() {
        LocalDate today = LocalDate.fromDateFields(new Date());
        LocalDate lastSession = LocalDate.fromDateFields(new Date(SharedPreferencesUtil.getSessionsLastDate()));
        return Days.daysBetween(today, lastSession).getDays() > 0;
    }

    public void setNeedToCalculateAppSpeed(boolean needToCalculateAppSpeed) {
        this.needToCalculateAppSpeed = needToCalculateAppSpeed;
    }

    public void setReadonlyFields(ArrayList<String> readonlyFields) {
        this.readonlyFields = readonlyFields;
    }

    public ArrayList<String> getReadonlyFields() {
        return readonlyFields;
    }

    public boolean isNeedToCalculateAppSpeed() {
        return needToCalculateAppSpeed;
    }

    public void initGetUserListRequest() {
        //RxUtil.delayedConsumer(3000, aLong -> {
            if (SharedPreferencesUtil.isAuthorized())
                RequestManager.getInstance().getMessageListFrom(new Phone(SharedPreferencesUtil.getPhone()), SharedPreferencesUtil.getLastMsgId() == null ? 0 : Integer.parseInt(SharedPreferencesUtil.getLastMsgId()), 100, new CallbackListener<MessageResponse>() {
                    @Override
                    public void onSuccess(MessageResponse response) {
                        List<Message> messages = response.getDataset();
                        if (messages == null) return;
                        Collections.sort(messages, (o1, o2) ->
                                Integer.parseInt(o2.getMsgId()) - Integer.parseInt(o1.getMsgId()));
                        EventBus.getDefault().post(new GetMessageIdSetEvent(messages));

                        Message message = messages.get(0);
                        if (SharedPreferencesUtil.getLastMsgId() == null || !SharedPreferencesUtil.getLastMsgId().equals(message.getMsgId())) {
                            SharedPreferencesUtil.setLastMsgId(message.getMsgId());
                       /* if (MSG_TYPE_PAYMENT_LINK == message.getMsgType()) {
                            EventBus.getDefault().post(new GetMessageEvent(message));
                        } else*/
                            if (MSG_TYPE_PAYMENT_SUCCESS == message.getMsgType()) {
                                EventBus.getDefault().post(new SuccessPaymentEvent(message));
                            }
                        }

                    }

                    @Override
                    public void onFailure(Throwable error) {
                        error.printStackTrace();
                    }
                });
            //initGetUserListRequest();
        //});
    }


    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    private void onAppStop() {
        SharedPreferencesUtil.setLastSessionDuration(((new Date().getTime() - sessionStartTime) / 1000) + SharedPreferencesUtil.getLastSessionDuration());
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    private void onAppStart() {
        sessionStartTime = new Date().getTime();
    }

    public void setAvatars(ArrayList<Avatar> avatars) {
        this.avatars = avatars;
    }

    public ArrayList<Avatar> getAvatars() {
        return avatars;
    }

    public User getUser() {
        return user;
    }

    public String getBolotoJackpot() {
        return bolotoJackpot;
    }

    public void setBolotoJackpot(String bolotoJackpot) {
        this.bolotoJackpot = bolotoJackpot;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public boolean isKreyol(Context context) {
        return context.getString(R.string.locale).equals(Constants.LANGUAGE_KR);
    }

    public boolean isFrench(Context context) {
        return context.getResources().getString(R.string.locale).equals(Constants.LANGUAGE_FR);
    }


}
