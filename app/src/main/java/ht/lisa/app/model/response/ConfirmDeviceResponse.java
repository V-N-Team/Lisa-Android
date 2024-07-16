package ht.lisa.app.model.response;

public class ConfirmDeviceResponse extends BaseResponse {

    private static final String UNAVAILABLE_METHOD = "method is not available for subscribers of the specified mobile operator";
    private static final String SMS_ERROR_SENDING = "error sending SMS messages";

    public String getErrorMessage() {
        String errorMessage;
        switch (getState()) {
            case -1:
                errorMessage = UNAVAILABLE_METHOD;
                break;

            case -2:
                errorMessage = SMS_ERROR_SENDING;
                break;

            case 5:
                errorMessage = "Invalid " + getInvalid();
                break;

            default:
                errorMessage = getBaseErrorMessage();
                break;

        }
        return errorMessage;
    }
}
