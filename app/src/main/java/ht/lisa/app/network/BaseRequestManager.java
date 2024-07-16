package ht.lisa.app.network;


import org.greenrobot.eventbus.EventBus;

import ht.lisa.app.model.response.BaseResponse;
import ht.lisa.app.ui.main.InvalidTokenEvent;
import ht.lisa.app.ui.main.PinCodeNeededEvent;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public abstract class BaseRequestManager {

    private <T> void onResponse(Response<T> response, CallbackListener<T> callbackListener) {
        BaseResponse baseResponse = (BaseResponse) response.body();
        if (baseResponse != null) {
            try {
                if (baseResponse.getState() == 3) {
                    EventBus.getDefault().post(new InvalidTokenEvent());
                } else if (baseResponse.getState() == 8) {
                    EventBus.getDefault().post(new PinCodeNeededEvent());
                } else {
                    callbackListener.onSuccess(response.body());
                }
            } catch (Exception e) {
                System.out.println(e.getLocalizedMessage());
            }
        }
    }

    protected <T> Callback<T> getCallback(CallbackListener<T> callbackListener) {
        return new Callback<T>() {
            @Override
            public void onResponse(Call<T> call, Response<T> response) {
                BaseRequestManager.this.onResponse(response, callbackListener);
            }

            @Override
            public void onFailure(Call<T> call, Throwable t) {
                callbackListener.onFailure(t);
            }
        };
    }
}
