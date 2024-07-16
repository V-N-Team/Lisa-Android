package ht.lisa.app.ui.wallet;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.fragment.app.Fragment;

import org.jetbrains.annotations.Nullable;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Optional;
import butterknife.Unbinder;
import ht.lisa.app.R;
import ht.lisa.app.ui.base.BaseFragment;
import ht.lisa.app.ui.wallet.WalletContract;
import ht.lisa.app.ui.wallet.WalletPresenter;
import ht.lisa.app.util.FragmentUtil;
import io.reactivex.disposables.CompositeDisposable;

public class BaseWalletFragment extends BaseFragment implements WalletContract.View {

    protected WalletPresenter walletPresenter = new WalletPresenter();

    private CompositeDisposable disposable;
    private Unbinder unbinder;

    public static <T extends BaseWalletFragment> T newInstance(Class<T> mClass, Bundle args) {
        try {
            T instance = mClass.newInstance();
            instance.setArguments(args);
            return instance;
        } catch (java.lang.InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View view = provideFragmentView(inflater, parent, savedInstanceState);
        unbinder = ButterKnife.bind(this, view);
        walletPresenter.attachView(this);
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

    public String getStringFromResource(int stringId) {
        return getResources().getString(stringId);
    }

    public int getColorFromResource(int colorId) {
        return getResources().getColor(colorId);
    }

    Drawable getDrawableFromResource(int drawableId) {
        return getResources().getDrawable(drawableId);
    }

    public void changeButtonState(Button button, boolean isEnable) {
        if (getContext() != null) {
            button.setBackground(isEnable ? getDrawableFromResource(R.drawable.rounded_button_orange_2) : getDrawableFromResource(R.drawable.rounded_button_grey));
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
    public void onBackArrowClick() {
        if (getActivity() == null) return;
        getActivity().onBackPressed();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (disposable != null) {
            disposable.dispose();
            disposable.clear();
        }
        walletPresenter.detachView();
        if (unbinder != null) {
            unbinder.unbind();
        }
    }

    @Override
    public void getAccessBiometricLogin() {

    }
}
