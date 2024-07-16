package ht.lisa.app.ui.registration;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.OnClick;
import ht.lisa.app.R;
import ht.lisa.app.util.SharedPreferencesUtil;

public class AcceptNotifiedRegistrationFragment extends BaseRegistrationFragment {

    @Override
    public View provideFragmentView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_accept_notified_registration, parent, false);
    }

    @OnClick({R.id.accept_notified_turn_on_button, R.id.accept_notified_no_thanks_button})
    void onButtonClick(View view) {
        switch (view.getId()) {
            case R.id.accept_notified_turn_on_button:
                SharedPreferencesUtil.setNotificationsAllowed(true);
                showNextFragment(BaseRegistrationFragment.newInstance(MobileRegistrationFragment.class, null));
                break;
            case R.id.accept_notified_no_thanks_button:
                SharedPreferencesUtil.setNotificationsAllowed(false);
                showNextFragment(BaseRegistrationFragment.newInstance(MobileRegistrationFragment.class, null));
                break;
        }
    }
}
