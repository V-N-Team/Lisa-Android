package ht.lisa.app.model.response;

import com.google.gson.annotations.SerializedName;

public class UserInfoResponse extends BaseResponse {

    @SerializedName("pin")
    private int pinActive;
    @SerializedName("secret")
    private int secretActive;
    @SerializedName("onesignal")
    private int onesignalActive;

    public int getPinActive() {
        return pinActive;
    }

    public int getSecretActive() {
        return secretActive;
    }

    public String getErrorMessage() {
        return getBaseErrorMessage();
    }

    public void setPinActive(int pinActive) {
        this.pinActive = pinActive;
    }

    public boolean isOnesignalActive() {
        return onesignalActive > 0;
    }
}
