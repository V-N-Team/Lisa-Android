package ht.lisa.app.model.response;

public class SogexpressTransferResponse extends BaseResponse {

    private static final String AMOUNT_OUT_OF_RANGE = "transfer amount out of range";
    private static final String INCORRECT_TRANSFER_AMOUNT = "incorrect transfer amount";
    private static final String LACK_OF_FUNDS = "lack of funds";

    private String id;
    private String pin;

    public String getErrorMessage() {
        String errorMessage;
        switch (getState()) {
            case -1:
                errorMessage = AMOUNT_OUT_OF_RANGE;
                break;

            case -2:
                errorMessage = INCORRECT_TRANSFER_AMOUNT;
                break;

            case -3:
                errorMessage = LACK_OF_FUNDS;
                break;

            default:
                errorMessage = getBaseErrorMessage();
                break;

        }
        return errorMessage;
    }

    public String getId() {
        return id;
    }

    public String getPin() {
        return pin;
    }
}
