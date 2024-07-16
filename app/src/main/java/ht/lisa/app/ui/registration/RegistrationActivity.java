package ht.lisa.app.ui.registration;

import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import org.greenrobot.eventbus.EventBus;

import ht.lisa.app.R;
import ht.lisa.app.ui.registration.error.NoConnectivityDialog;
import ht.lisa.app.util.DeviceUtils;
import ht.lisa.app.util.FragmentUtil;
import ht.lisa.app.util.LanguageUtil;
import ht.lisa.app.util.NetworkChangeReceiver;
import ht.lisa.app.util.SharedPreferencesUtil;

public class RegistrationActivity extends AppCompatActivity {

    public static final String FROM_BASE_FRAGMENT_KEY = "fromBaseActivityKey";
    public static final String SIGN_OUT_KEY = "signOutKey";

    private boolean isFromBaseActivity;
    private boolean isSignedOut;

    public NetworkChangeReceiver receiver;

    @Override
    protected void onStart() {
        super.onStart();
        registerCheckInternetReceiver();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LanguageUtil.initAppLanguage(this);
        setContentView(R.layout.activity_registration);
        SharedPreferencesUtil.setDeviceId(DeviceUtils.getDeviceId());

        if (getIntent().getExtras() != null) {
            isFromBaseActivity = getIntent().getExtras().getBoolean(FROM_BASE_FRAGMENT_KEY, false);
            isSignedOut = getIntent().getExtras().getBoolean(SIGN_OUT_KEY, false);
        }

        if (isFromBaseActivity) {
            if (DeviceUtils.isConnectedToInternet(this)) {
                Bundle args = new Bundle();
                args.putBoolean(FROM_BASE_FRAGMENT_KEY, true);
                FragmentUtil.replaceFragment(getSupportFragmentManager(), SharedPreferencesUtil.isAuthorized() ?
                        BaseRegistrationFragment.newInstance(PinCodeRegistrationFragment.class, args)
                        : (SharedPreferencesUtil.getSalt() == null || SharedPreferencesUtil.getSalt().isEmpty()) ?
                        BaseRegistrationFragment.newInstance(ChooseLanguageRegistrationFragment.class, null)
                        : BaseRegistrationFragment.newInstance(VerificationRegistrationFragment.class, null), false);
            } else {
                new NoConnectivityDialog().show(getSupportFragmentManager(), NoConnectivityDialog.class.getSimpleName());
            }
        } else if (isSignedOut) {
            FragmentUtil.replaceFragment(getSupportFragmentManager(), new ChooseLanguageRegistrationFragment(), false);
        } else {
            FragmentUtil.replaceFragment(getSupportFragmentManager(), new SplashFragment(), false);
        }

        /*startActivity(new Intent(this, WalletActivity.class)
                .putExtra(WalletActivity.WALLET_FRAGMENT, WalletActivity.WalletPage.RELOAD_MONEY_FUNDS));*/
    }

    @Override
    protected void onPause() {
        super.onPause();
        EventBus.getDefault().unregister(this);
        unRegisterInternetReceiver();
    }

    private void registerCheckInternetReceiver() {
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        receiver = new NetworkChangeReceiver();
        registerReceiver(receiver, filter);
    }

    private void unRegisterInternetReceiver() {
        try {
            unregisterReceiver(receiver);
        } catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
        }
    }
}
