package ht.lisa.app.model.response;

import android.content.Context;

import ht.lisa.app.R;

public class BindDeviceResponse extends BaseResponse {

    private static final String OPERATOR_NOT_SUPPORT_SERVICE = "Your operator does not support this service.";
    private static final String SERVICE_CAN_NOT_ACTIVATED = " This service currently cannot be activated.";

    private String algo;
    private String salt;

    public String getAlgo() {
        return algo;
    }

    public String getSalt() {
        return salt;
    }

    public String getErrorMessage(Context context) {
        String errorMessage;
        switch (getState()) {
            case -1:
                errorMessage = context.getString(R.string.your_operator_does_not_support_service);
                break;

            case -2:
                errorMessage = SERVICE_CAN_NOT_ACTIVATED;
                break;

            default:
                errorMessage = getBaseErrorMessage();
                break;
        }
        return errorMessage;
    }
}

