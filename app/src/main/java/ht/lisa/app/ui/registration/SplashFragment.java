package ht.lisa.app.ui.registration;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ht.lisa.app.R;
import ht.lisa.app.ui.registration.error.NoConnectivityDialog;
import ht.lisa.app.util.DeviceUtils;
import ht.lisa.app.util.RxUtil;
import ht.lisa.app.util.SharedPreferencesUtil;

public class SplashFragment extends BaseRegistrationFragment {

    @Override
    public View provideFragmentView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        SharedPreferencesUtil.setLisaSoundNeeded(true);
        RxUtil.delayedConsumer(3000, aLong -> {
            if (getContext() != null && DeviceUtils.isConnectedToInternet(getContext())) {
                showNextFragment(SharedPreferencesUtil.isAuthorized() ?
                        BaseRegistrationFragment.newInstance(PinCodeRegistrationFragment.class, null)
                        : (SharedPreferencesUtil.getSalt() == null || SharedPreferencesUtil.getSalt().isEmpty()) ?
                        (SharedPreferencesUtil.getLanguage() == null ? BaseRegistrationFragment.newInstance(ChooseLanguageRegistrationFragment.class, null) : BaseRegistrationFragment.newInstance(GetStartedRegistrationFragment.class, null))
                        : BaseRegistrationFragment.newInstance(VerificationRegistrationFragment.class, null));
            } else {
                new NoConnectivityDialog().show(getFragmentManager(), NoConnectivityDialog.class.getSimpleName());
            }
        });
        return inflater.inflate(R.layout.fragment_splash, parent, false);
    }

}
