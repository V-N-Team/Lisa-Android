package ht.lisa.app.util;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.hardware.fingerprint.FingerprintManagerCompat;

import com.an.biometric.BiometricCallback;
import com.an.biometric.BiometricManager;

import ht.lisa.app.R;
import ht.lisa.app.ui.base.BaseContract;


public class BiometricUtil implements BiometricCallback {

    private Context context;
    private BaseContract.BaseView view;

    public BiometricUtil(Context context, BaseContract.BaseView view) {
        this.context = context;
        this.view = view;
    }

    public static boolean isSensorAvialable(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return ActivityCompat.checkSelfPermission(context, Manifest.permission.USE_FINGERPRINT) == PackageManager.PERMISSION_GRANTED &&
                    context.getSystemService(FingerprintManager.class) != null && context.getSystemService(FingerprintManager.class).isHardwareDetected();
        } else {
            return FingerprintManagerCompat.from(context).isHardwareDetected();
        }
    }

    public void startBiometricManager() {
        BiometricManager mBiometricManager = new BiometricManager.BiometricBuilder(context)
                .setTitle(context.getString(R.string.biometric_title))
                .setSubtitle(context.getString(R.string.biometric_subtitle))
                .setDescription(context.getString(R.string.biometric_description))
                .setNegativeButtonText(context.getString(R.string.biometric_negative_button_text))
                .build();
        try {
            mBiometricManager.authenticate(this);
        } catch (Exception e) {
            if (e.getLocalizedMessage() != null) {
                Log.d("mBiometricManagerTAG", e.getLocalizedMessage());
            } else {
                Log.d("mBiometricManagerTAG", "unknown error");
            }

        }

    }

    private void setBiometricAuthentication(boolean b) {
        SharedPreferencesUtil.setBiometricAuthentication(b);
    }

    @Override
    public void onSdkVersionNotSupported() {
        Toast.makeText(context, context.getString(R.string.biometric_error_sdk_not_supported), Toast.LENGTH_LONG).show();
        setBiometricAuthentication(false);
    }

    @Override
    public void onBiometricAuthenticationNotSupported() {
        Toast.makeText(context, context.getString(R.string.biometric_error_hardware_not_supported), Toast.LENGTH_LONG).show();
        setBiometricAuthentication(false);
    }

    @Override
    public void onBiometricAuthenticationNotAvailable() {
        Toast.makeText(context, context.getString(R.string.biometric_error_fingerprint_not_available), Toast.LENGTH_LONG).show();
        setBiometricAuthentication(false);
    }

    @Override
    public void onBiometricAuthenticationPermissionNotGranted() {
        Toast.makeText(context, context.getString(R.string.biometric_error_permission_not_granted), Toast.LENGTH_LONG).show();
        setBiometricAuthentication(false);
    }

    @Override
    public void onBiometricAuthenticationInternalError(String error) {
        Toast.makeText(context, error, Toast.LENGTH_LONG).show();
        setBiometricAuthentication(false);
    }

    @Override
    public void onAuthenticationFailed() {
        Toast.makeText(context, context.getString(R.string.biometric_failure), Toast.LENGTH_LONG).show();
        setBiometricAuthentication(false);
    }

    @Override
    public void onAuthenticationCancelled() {
        Toast.makeText(context, context.getString(R.string.biometric_cancelled), Toast.LENGTH_LONG).show();
        setBiometricAuthentication(false);
    }

    @Override
    public void onAuthenticationSuccessful() {
        view.getAccessBiometricLogin();
        Toast.makeText(context, context.getString(R.string.biometric_success), Toast.LENGTH_LONG).show();
        setBiometricAuthentication(true);
    }

    @Override
    public void onAuthenticationHelp(int helpCode, CharSequence helpString) {
        Toast.makeText(context, helpString, Toast.LENGTH_LONG).show();
        setBiometricAuthentication(false);
    }

    @Override
    public void onAuthenticationError(int errorCode, CharSequence errString) {
        Toast.makeText(context, errString, Toast.LENGTH_LONG).show();
        setBiometricAuthentication(false);
    }
}
