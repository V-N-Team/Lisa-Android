package ht.lisa.app.network;

public interface CallbackListener<T> {

    void onSuccess(T response);

    void onFailure(Throwable error);

}