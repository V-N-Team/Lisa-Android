package ht.lisa.app.model.response;

public class TicketsPurchaseResponse extends BaseResponse {

    private static final String INCORRECT_DRAW_TIME = "incorrect draw time";
    private static final String SALE_CURRENT_DRAW_SUSPENDED = "sale for the current draw is suspended";
    private static final String LACK_OF_FUNDS = "lack of funds to buy";
    private static final String INCORRECT_TICKET_PRICE = "incorrect ticket price";

    private String ticket;

    public String getErrorMessage() {
        String errorMessage;
        switch (getState()) {
            case -1:
                errorMessage = INCORRECT_DRAW_TIME;
                break;

            case -2:
                errorMessage = SALE_CURRENT_DRAW_SUSPENDED;
                break;

            case -3:
                errorMessage = LACK_OF_FUNDS;
                break;

            case -4:
                errorMessage = INCORRECT_TICKET_PRICE;
                break;

            default:
                errorMessage = getBaseErrorMessage();
                break;

        }
        return errorMessage;
    }

    public String getTicket() {
        return ticket;
    }
}
