package ht.lisa.app.model.response;

public class SubscribeResponse extends BaseResponse {
    public static final int NOT_ENOUGH_CASH = -1;
    public static final int COMBINATION_BOOKED = -2;

    private int ticket;

    public String getSubscribeError() {
        String errorMessage = "";
        switch (getState()) {
            case NOT_ENOUGH_CASH:
                errorMessage = "not enough cash";
                break;
            case COMBINATION_BOOKED:
                errorMessage = "combination booked";
                break;
            default:
                errorMessage = getBaseErrorMessage();
                break;
        }
        return errorMessage;
    }

    public int getTicket() {
        return ticket;
    }
}
