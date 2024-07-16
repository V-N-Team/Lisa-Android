package ht.lisa.app.ui.registration.error;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.OnClick;
import ht.lisa.app.R;
import ht.lisa.app.ui.registration.BaseRegistrationFragment;
import ht.lisa.app.ui.registration.MobileRegistrationFragment;
import ht.lisa.app.util.FragmentUtil;

public class ErrorFragment extends BaseRegistrationFragment {

    public static ErrorFragment newInstance() {
        return new ErrorFragment();
    }

    @Override
    public View provideFragmentView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_error, parent, false);
    }

    @OnClick(R.id.error_button)
    void onErrorButtonClick() {
        FragmentUtil.replaceFragment(getFragmentManager(), new MobileRegistrationFragment(), false);
    }
}
