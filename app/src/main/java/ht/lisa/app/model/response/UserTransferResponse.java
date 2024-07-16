package ht.lisa.app.model.response;

public class UserTransferResponse extends BaseResponse {

    private static final String OUT_RANGE_AMOUNT = "transfer amount is out of range";
    private static final String INCORRECT_PIN = "incorrect transfer amount";
    private static final String FUNDS_LACK = "lack of funds";
    private static final String NOT_FOUND_RECIPIENT = "recipient not found or blocked";

    public String getErrorMessage() {
        String errorMessage;
        switch (getState()) {

            case -1:
                errorMessage = OUT_RANGE_AMOUNT;
                break;

            case -2:
                errorMessage = INCORRECT_PIN;
                break;

            case -3:
                errorMessage = FUNDS_LACK;
                break;

            case -4:
                errorMessage = NOT_FOUND_RECIPIENT;
                break;

            default:
                errorMessage = getBaseErrorMessage();
                ;
                break;
        }
        return errorMessage;
    }
}
