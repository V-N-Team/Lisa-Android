package ht.lisa.app.util;

import com.google.firebase.analytics.FirebaseAnalytics;

public interface Analytics {

    String SCREEN_NAME = "screen_name";
    String SCREEN_VIEW = "screen_view";

    String APP_SPEED = "app_speed";
    String SPEED = "speed";


    String LOCATION = FirebaseAnalytics.Param.LOCATION;
    String SESSION_DURATION = "session_duration";
    String VISITS = "visits";

}
