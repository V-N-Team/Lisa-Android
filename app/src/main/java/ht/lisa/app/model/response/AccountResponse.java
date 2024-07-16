package ht.lisa.app.model.response;

public class AccountResponse extends BaseResponse {

    private int total;
    private int bonus;
    private double withdrawal;

    public int getTotal() {
        return total;
    }

    public int getBonus() {
        return bonus;
    }

    public int getWithdrawal() {
        return (int) withdrawal;
    }
}
