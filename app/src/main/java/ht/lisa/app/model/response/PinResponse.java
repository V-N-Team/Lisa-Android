package ht.lisa.app.model.response;

public class PinResponse extends BaseResponse {

    private static final String INCORRECT_PIN = "pin code is incorrect";
    private static final String IDENTICAL_CODE = "pin code is identical";

    public String getErrorMessage() {
        String errorMessage;
        if (getState() == -1) {
            errorMessage = INCORRECT_PIN;
        } else if (getState() == -2) {
            errorMessage = IDENTICAL_CODE;
        } else {
            errorMessage = getBaseErrorMessage();
        }
        return errorMessage;
    }

}
