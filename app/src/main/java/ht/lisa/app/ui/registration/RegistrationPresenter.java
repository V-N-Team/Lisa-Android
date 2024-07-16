package ht.lisa.app.ui.registration;

import android.content.Context;

import ht.lisa.app.LisaApp;
import ht.lisa.app.model.Device;
import ht.lisa.app.model.Phone;
import ht.lisa.app.model.Pin;
import ht.lisa.app.model.PinChange;
import ht.lisa.app.model.User;
import ht.lisa.app.model.response.BindDeviceResponse;
import ht.lisa.app.model.response.PinResponse;
import ht.lisa.app.model.response.ProfileResponse;
import ht.lisa.app.model.response.UserInfoResponse;
import ht.lisa.app.network.CallbackListener;
import ht.lisa.app.network.RequestManager;
import ht.lisa.app.ui.component.PinView;
import ht.lisa.app.ui.mvp.BasePresenter;
import ht.lisa.app.util.RxUtil;
import ht.lisa.app.util.SharedPreferencesUtil;


class RegistrationPresenter extends BasePresenter<RegistrationContract.View> {

    private RequestManager requestManager = RequestManager.getInstance();

    void bindDevice(Device device, Context context) {
        getView().showProgress();
        requestManager.bindDevice(device, new CallbackListener<BindDeviceResponse>() {
            @Override
            public void onSuccess(BindDeviceResponse response) {
                getView().hideProgress();
                if (response == null) return;
                /*if (response.getState() == 0) {
                    getView().getData(response);
                } else {
                    getView().showMessage(response.getErrorMessage(context));
                }*/
                getView().getData(response);
            }

            @Override
            public void onFailure(Throwable error) {
                onFailureAction(error);
            }
        });
    }

    void confirmDevice(String token, Phone phone) {
        getView().showProgress();
        RxUtil.networkConsumer(requestManager.confirmDevice(token, phone), response -> {
            getView().hideProgress();
            if (response == null) return;
            getView().getData(response);
        }, throwable -> {
            onFailureAction(throwable);
            SharedPreferencesUtil.clearToken();
            LisaApp.getInstance().unbindOneSignal();
        });
    }

    void getUserInfo(String token, Phone phone) {
        getView().showProgress();
        requestManager.getUserInfo(token, phone, new CallbackListener<UserInfoResponse>() {
            @Override
            public void onSuccess(UserInfoResponse response) {
                if (!response.isOnesignalActive()) {
                    LisaApp.getInstance().bindOneSignal();
                }
                getView().hideProgress();
                /*if (response.getState() != 0) {
                    getView().showMessage(response.getBaseErrorMessage());
                }*/
                getView().getData(response);
            }

            @Override
            public void onFailure(Throwable error) {
                onFailureAction(error);
                SharedPreferencesUtil.clearToken();
                LisaApp.getInstance().unbindOneSignal();
            }
        });
    }

    void getProfile(User user) {
        getView().showProgress();
        requestManager.setUserProfile(user, new CallbackListener<ProfileResponse>() {
            @Override
            public void onSuccess(ProfileResponse response) {
                if (response.getState() == 0) {
                    User user1 = response.getUser();
                    user1.setPhone(SharedPreferencesUtil.getPhone());
                    LisaApp.getInstance().setUser(user1);
                    LisaApp.getInstance().setReadonlyFields(response.getReadonly());
                }
                getView().hideProgress();
                getView().getData(response);
            }

            @Override
            public void onFailure(Throwable error) {
                onFailureAction(error);
            }
        });
    }

    void createPin(String token, Pin pin, PinView pinView) {
        getView().showProgress();
        requestManager.createPin(token, pin, getPinCallBack(pinView));
    }

    void authorizePin(Pin pin, PinView pinView) {
        getView().showProgress();
        requestManager.authorizePin(pin, getPinCallBack(pinView));
    }

    void changePin(PinChange pin, PinView pinView) {
        getView().showProgress();
        requestManager.changePin(pin, getPinCallBack(pinView));
    }

    private CallbackListener<PinResponse> getPinCallBack(PinView pinView) {
        return new CallbackListener<PinResponse>() {
            @Override
            public void onSuccess(PinResponse response) {
                getView().hideProgress();
                if (response == null) return;
                if (response.getState() == 0 || response.getState() == -1) {
                    if (!SharedPreferencesUtil.isAuthorized()) {
                        LisaApp.getInstance().authorize(SharedPreferencesUtil.getTemporaryToken());
                        SharedPreferencesUtil.setTemporaryToken("");
                    }
                } else if (response.getState() != 6) {
                    getView().showMessage(response.getErrorMessage());
                }
                getView().getData(response);
                pinView.clear();
            }

            @Override
            public void onFailure(Throwable error) {
                onFailureAction(error);
                pinView.clear();
            }
        };
    }

    private void onFailureAction(Throwable error) {
        if (getView() == null) return;
        getView().hideProgress();
        getView().showMessage(error.getLocalizedMessage());
        error.printStackTrace();
    }
}
