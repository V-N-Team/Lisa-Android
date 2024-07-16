package ht.lisa.app.ui.registration;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.OnClick;
import ht.lisa.app.R;
import ht.lisa.app.ui.main.MainActivity;


public class OverviewFragment extends BaseRegistrationFragment {


    @Override
    public View provideFragmentView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_overview, parent, false);
    }

    @OnClick({R.id.overview_screen_button, R.id.overview_screen_skip})
    void onOverviewButtonClick() {
        startActivity(new Intent(getContext(), MainActivity.class));
    }

}
