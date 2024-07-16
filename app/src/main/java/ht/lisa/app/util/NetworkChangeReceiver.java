package ht.lisa.app.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;

import ht.lisa.app.ui.registration.error.NoConnectivityDialog;

public class NetworkChangeReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(final Context context, final Intent intent) {
        int status = NetworkUtil.getConnectivityStatus(context);
        ReturnStatus(status, context);
    }

    public void ReturnStatus(int status, final Context context) {
        AppCompatActivity activity = (AppCompatActivity) context;
        if (status == NetworkUtil.TYPE_NOT_CONNECTED) {
            try {
                new NoConnectivityDialog().show(activity.getSupportFragmentManager(), NoConnectivityDialog.class.getSimpleName());
            } catch (IllegalStateException e) {
                e.printStackTrace();
            }
        }
    }
}
