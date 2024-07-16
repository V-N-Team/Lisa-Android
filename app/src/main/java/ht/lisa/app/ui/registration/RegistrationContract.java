package ht.lisa.app.ui.registration;

import ht.lisa.app.ui.base.BaseContract;
import ht.lisa.app.ui.mvp.MvpPresenter;

public interface RegistrationContract extends BaseContract {

    interface View extends BaseView {
        void getAccessBiometricLogin();
    }

    interface Presenter extends MvpPresenter<View> {

    }
}
