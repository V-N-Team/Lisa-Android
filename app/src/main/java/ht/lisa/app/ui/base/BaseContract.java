package ht.lisa.app.ui.base;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import ht.lisa.app.ui.mvp.MvpPresenter;
import ht.lisa.app.ui.mvp.MvpView;

public interface BaseContract {

    interface BaseView extends MvpView {
        android.view.View provideFragmentView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState);
        void showProgress();
        void hideProgress();
        void showMessage(String message);
        void getData(Object object);
        void getAccessBiometricLogin();
    }

    interface Presenter extends MvpPresenter<BaseView> {

    }
}
