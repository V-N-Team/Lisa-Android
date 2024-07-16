package ht.lisa.app.ui.registration;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.OnClick;
import butterknife.Optional;
import ht.lisa.app.R;
import ht.lisa.app.util.SharedPreferencesUtil;

public class GetStartedRegistrationFragment extends BaseRegistrationFragment {

    @Override
    public View provideFragmentView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_get_started, parent, false);
    }

    @Optional
    @OnClick(R.id.get_started_button)
    void onGetStartedClick() {
        SharedPreferencesUtil.setGetStarted(true);
        showNextFragment(BaseRegistrationFragment.newInstance(AcceptNotifiedRegistrationFragment.class, null));
    }
}
