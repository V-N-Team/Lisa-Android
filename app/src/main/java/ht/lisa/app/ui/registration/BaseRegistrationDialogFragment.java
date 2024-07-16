package ht.lisa.app.ui.registration;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import ht.lisa.app.R;
import ht.lisa.app.ui.base.BaseDialogFragment;
import ht.lisa.app.ui.main.MainActivity;
import ht.lisa.app.util.FragmentUtil;
import ht.lisa.app.util.SharedPreferencesUtil;
import io.reactivex.disposables.CompositeDisposable;

public class BaseRegistrationDialogFragment extends BaseDialogFragment implements RegistrationContract.View {

    RegistrationPresenter registrationPresenter = new RegistrationPresenter();
    private CompositeDisposable disposable;
    private Unbinder unbinder;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View view = provideFragmentView(inflater, parent, savedInstanceState);
        unbinder = ButterKnife.bind(this, view);
        registrationPresenter.attachView(this);
        disposable = new CompositeDisposable();
        addProgressBar(view);
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        return view;
    }

    public View provideFragmentView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_registration, parent, false);
    }

    public void getData(Object object) {
        Log.d("getdataTAG", "baseregistration " + object.toString());
    }

    @Override
    public void getAccessBiometricLogin() {
        Fragment fragment = getActivity().getSupportFragmentManager().findFragmentById(R.id.container);
        if (fragment instanceof SecurityWordRegistrationFragment) {
            showNextFragment(BaseRegistrationFragment.newInstance(OverviewFragment.class, null));
        } else {
            SharedPreferencesUtil.setBiometricAuthentication(true);
            startActivity(new Intent(getContext(), MainActivity.class));
            if (getActivity() == null) return;
            getActivity().finish();
        }
    }

    String getStringFromResource(int stringId) {
        return getResources().getString(stringId);
    }

    public int getColorFromResource(int colorId) {
        return getResources().getColor(colorId);
    }

    Drawable getDrawableFromResource(int drawableId) {
        return getResources().getDrawable(drawableId);
    }

    public void changeButtonState(Button button, boolean isEnable) {
        button.setBackground(isEnable ? getDrawableFromResource(R.drawable.rounded_button_orange_2) : getDrawableFromResource(R.drawable.rounded_button_grey));
    }

    public void showNextFragment(Fragment fragment) {
        FragmentUtil.replaceFragment(getFragmentManager(), fragment, true);
    }

    public void showMessage(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (disposable != null) {
            disposable.dispose();
            disposable.clear();
        }
        registrationPresenter.detachView();
        if (unbinder != null) {
            unbinder.unbind();
        }
    }
}
