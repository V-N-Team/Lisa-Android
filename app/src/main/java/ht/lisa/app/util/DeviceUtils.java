package ht.lisa.app.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;

import ht.lisa.app.LisaApp;


public class DeviceUtils {

    public static String getDeviceId() {
        String androidId = Settings.Secure.getString(LisaApp.getInstance().getContentResolver(),
                Settings.Secure.ANDROID_ID);
        return androidId == null ? "" : androidId;
    }

    public static boolean isConnectedToInternet(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo activeNetworkInfo = null;
            activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            return activeNetworkInfo != null && activeNetworkInfo.isConnected();
        }
        return false;
    }

    public static int GetPixelFromDips(float pixels, Context context) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pixels * scale + 0.5f);
    }

}
