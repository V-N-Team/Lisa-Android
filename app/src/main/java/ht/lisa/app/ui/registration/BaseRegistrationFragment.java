package ht.lisa.app.ui.registration;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.fragment.app.Fragment;

import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Optional;
import butterknife.Unbinder;
import ht.lisa.app.LisaApp;
import ht.lisa.app.R;
import ht.lisa.app.ui.base.BaseFragment;
import ht.lisa.app.ui.main.MainActivity;
import ht.lisa.app.util.FragmentUtil;
import ht.lisa.app.util.RxUtil;
import ht.lisa.app.util.SharedPreferencesUtil;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;

public class BaseRegistrationFragment extends BaseFragment implements RegistrationContract.View {

    RegistrationPresenter registrationPresenter = new RegistrationPresenter();
    private CompositeDisposable disposable;
    private Unbinder unbinder;

    public static <T extends BaseRegistrationFragment> T newInstance(Class<T> mClass, Bundle args) {
        try {
            T instance = mClass.newInstance();
            instance.setArguments(args);
            return instance;
        } catch (java.lang.InstantiationException e) {
            e.printStackTrace();
        } catch (java.lang.IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View view = provideFragmentView(inflater, parent, savedInstanceState);
        unbinder = ButterKnife.bind(this, view);
        registrationPresenter.attachView(this);
        disposable = new CompositeDisposable();
        addProgressBar(view);
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
        if (getContext() != null) {
            return getResources().getString(stringId);
        } return "";
    }

    public int getColorFromResource(int colorId) {
        if (getContext() != null) {
            return getResources().getColor(colorId);
        } return 0;
    }

    Drawable getDrawableFromResource(int drawableId) {
        if (getContext() != null)
            return AppCompatResources.getDrawable(getContext(), drawableId);
        return AppCompatResources.getDrawable(LisaApp.getInstance().getApplicationContext(), drawableId);
    }

    public void changeButtonState(Button button, boolean isEnable) {
        try {
            button.setBackground(isEnable ? getDrawableFromResource(R.drawable.rounded_button_orange_2) : getDrawableFromResource(R.drawable.rounded_button_grey));
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    public void showNextFragment(Fragment fragment) {
        FragmentUtil.replaceFragment(getFragmentManager(), fragment, true);
    }

    public void showMessage(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
    }

    @Optional
    @OnClick(R.id.back_arrow)
    void onBackArrowClick() {
        if (getActivity() == null) return;
        getActivity().onBackPressed();
    }

    @Override
    public void onStart() {
        super.onStart();
        cancelCountdown();
    }

    protected void showDelayMessage(int delay) {
        RxUtil.mainThreadConsumer(this, new Consumer<BaseRegistrationFragment>() {
            @Override
            public void accept(BaseRegistrationFragment baseRegistrationFragment) throws Exception {
                new AlertDialog.Builder(getContext(), R.style.AlertDialogTheme)
                        .setTitle(getString(R.string.error))
                        .setMessage(/*"Please, wait for " + DateUtils.formatElapsedTime(delay)
                        + " before trying again"*/
                                getString(R.string.please_wait_a_few_mins))
                        .setNeutralButton(android.R.string.ok, null)
                        //.setNegativeButton(android.R.string.cancel, null)
                        .show();
            }
        });

        //showMessage("Try in " + DateUtils.formatElapsedTime(this.delay));
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
