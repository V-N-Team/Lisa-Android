package ht.lisa.app.ui.wallet;


import ht.lisa.app.ui.base.BaseContract;
import ht.lisa.app.ui.mvp.MvpPresenter;

public interface WalletContract extends BaseContract {

    interface View extends BaseContract.BaseView {
        void getAccessBiometricLogin();
    }

    interface Presenter extends MvpPresenter<View> {

    }
}
