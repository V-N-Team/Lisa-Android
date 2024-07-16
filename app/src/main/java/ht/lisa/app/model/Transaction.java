package ht.lisa.app.model;

import android.content.Context;

import com.google.gson.annotations.SerializedName;

import ht.lisa.app.R;

public class Transaction {


    private String id;
    private String name;
    private String date;
    @SerializedName("date_8601")
    private String date8601;
    private int type;
    private int amount;

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDate() {
        return date;
    }

    public String getDate8601() {
        return date8601;
    }

    public int getType() {
        return type;
    }

    public int getAmount() {
        return amount;
    }

    public static String getTransactionMessage(Context context, int code) {
        for (Transactions transaction : Transactions.values()) {
            if (transaction.getCode() == code) {
                if (transaction.getMessage() != 0)
                    return context.getResources().getString(transaction.getMessage());
            }
        }
        return "";
    }

    public enum Transactions {
        WITHDRAWAL_OF_FUNDS_THROUGH_SOGEXPRESS(R.string.cash_out_with_sogexpress, 2),
        REFUND_FROM_SOGEXPRESS(R.string.refund_from_sogexpress, 3),
        RECHARGE_USER_ACCOUNT_FROM_UTIBA_TTM(R.string.reload_moncash_trans, 4),
        REPLENISHMENT_OF_AN_AGENT_ACCOUNT_FROM_UTIBA_TTM(0, 5),
        TRANSFER_OF_FUNDS_TO_THE_USER_RECEIVING_EFFECTS_FROM_THE_USER(R.string.credits_transfered_to_lisa_user, 6),
        TRANSFER_OF_FUNDS_TO_THE_USER_FROM_THE_AGENT(0, 7),
        REPLENISHMENT_OF_THE_USER_ACCOUNT_BY_THE_ADMINISTRATOR(0, 8),
        DEBIT_FROM_THE_USER_ACCOUNT_BY_THE_ADMINISTRATOR(0, 9),
        REPLENISHMENT_THE_USER_ACCOUNT_BY_THE_ADMINISTRATOR(R.string.account_recharged_by_lisa_user, 10),
        DEBITING_FROM_THE_USER_ACCOUNT_BY_THE_ADMINISTRATOR(0, 11),
        REPLENISHMENT_OF_THE_AGENT_ACCOUNT_BY_THE_ADMINISTRATOR(0, 12),
        DEBITING_FROM_THE_AGENT_ACCOUNT_BY_THE_ADMINISTRATOR(0, 13),
        BONUS_FOR_REPLENISHING_A_USER_ACCOUNT_THROUGH_TCHOTCHO(R.string.reload_moncash_bonus, 14),
        BONUS_FOR_REPLENISHING_AN_AGENT_ACCOUNT_THROUGH_TCHOTCHO(0, 15),
        BOLOTO_TICKET_PURCHASED(R.string.boloto_ticket_purchased, 16),
        PURCHASED_BY_BOLOTO_QUICK_PICK(R.string.purchased_by_boloto_qp, 17),
        WIN_BOLOTO_JACKPOT(R.string.boloto_jackpot_win, 18),
        WIN_BOLOTO_FREE_TICKET(R.string.boloto_ticket_win_trans, 19),
        WIN_BOLOTO(R.string.boloto_ticket_win, 20),
        TRANSFER_TO_AGENT_FROM_AGENT(0, 21),
        REPLENISHMENT_OF_A_USER_ACCOUNT_FROM_POPM(0, 22),
        REPLENISHMENT_OF_AN_AGENT_ACCOUNT_FROM_POPM(0, 23),
        BONUS_FOR_REPLENISHING_A_USER_ACCOUNT_FROM_POPM(0, 24),
        BONUS_FOR_REPLENISHING_AN_AGENT_ACCOUNT_FROM_POPM(0, 25),
        PURCHASED_BY_BOLOTO_COMBO(R.string.boloto_combo_ticket_purchase, 26),
        WIN_LOTTO3(R.string.lotto3_ticket_win_trans, 27),
        LOTTO3_TICKET_PURCHASED(R.string.lotto3_ticket_purchased, 28),
        BOLOTO_RETURN(0, 29),
        PURCHASED_LOTTO3(R.string.lotto3_ticket_purchased, 30),
        LOTTO3_RETURN(0, 31),
        TRANSFER_FUNDS_TO_THE_USER_FROM_THE_AGENT(0, 32),
        TRANSFER_FUNDS_TO_THE_USER_FROM_THE_AGENT_(0, 33),
        TIBOUL_TICKET_PURCHASED(0, 34),
        WIN_TIBOUL(0, 35),
        PURCHASED_BY_TIBOUL_QUICK_PICK(0, 36),
        WIN_VAS_PROMO(0, 37),
        CANCELLATION_OF_VAS_PROMO(0, 38),
        PILAY_1_TICKET_PURCHASED(0, 39),
        RECHARGE_USER_ACCOUNT_FROM_DIGICEL_EPIN(R.string.reload_acc_pappadap, 40),
        BONUS_FOR_REPLENISHING_A_USER_ACCOUNT_FROM_DIGICEL_EPIN(R.string.reload_acc_pappadap_bonus, 41),
        AGENT_REPLENISHMENT_FROM_DIGICEL_EPIN(0, 42),
        BONUS_FOR_REPLENISHING_AN_AGENT_ACCOUNT_FROM_DIGICEL_EPIN(0, 43),
        RECHARGE_USER_ACCOUNT_FROM_MONCASH(R.string.reload_moncash_trans, 44),
        BONUS_FOR_REPLENISHING_A_USER_ACCOUNT_FROM_MONCASH(R.string.reload_moncash_bonus, 45),
        REPLENISHMENT_OF_AN_AGENT_ACCOUNT_FROM_MONCASH(R.string.reload_moncash_trans, 46),
        BONUS_FOR_REPLENISHING_AN_AGENT_ACCOUNT_FROM_MONCASH(R.string.reload_moncash_bonus, 47),
        WIN_MILYON20(0, 48),
        BOLOTO_TICKET_PURCHASED_(R.string.boloto_ticket_purchased, 49),
        WIN_BOLOTO_(R.string.boloto_ticket_win_trans, 50),
        RECHARGE_USER_ACCOUNT_FROM_DIGICEL_AIRTIME(0, 51),
        LOTTO5_TICKET_PURCHASED(R.string.lotto5_ticket_purchased, 52),
        WIN_LOTTO5(R.string.lotto5_ticket_win_trans, 53),
        LOTTO_5G_TICKET_PURCHASED(R.string.lotto5jr_ticket_purchased, 54),
        WIN_LOTTO5_5G(R.string.lotto5jr_ticket_win_trans, 55),
        LOTTO3_TICKET_PURCHASED_(R.string.lotto3_ticket_purchased, 56),
        WIN_LOTTO3_(R.string.lotto3_ticket_win_trans, 57),
        MONCASH_TRANSLATION(R.string.transfer_has_been_done_success, 58),
        BORLETTE_TICKET_PURCHASED(R.string.bolet_ticket_purchased, 59),
        BORLETTE_WINNINGS(R.string.bolet_ticket_win_trans, 60),
        MARIAGE_TICKET_PURCHASED(R.string.maryaj_ticket_purchased, 61),
        WIN_MARIAGE(R.string.mariage_ticket_win, 62),
        LOTTO5_TICKET_PURCHASED_(R.string.lotto5_ticket_purchased, 63),
        LOTTO5_WIN(R.string.lotto5_ticket_win_trans, 64),
        FIDELISA_TRANSLATION(0, 65),
        WIN_PROMO_USSD(0, 66),
        LOTTO4_TICKET_PURCHASED(R.string.lotto4_ticket_purchased, 67),
        WIN_LOTTO4(R.string.lotto4_ticket_win_trans, 68),
        ACCRUAL_FOR_REFERRAL_PROGRAM(0, 69),
        ACCRUAL_FOR_REFERRAL_PROGRAM_(0, 70),
        LOTTO3_WIN_PROMO(R.string.lotto3_ticket_win_trans, 71),
        VAS_LOTTO5_5G_CHECK_LASTDRAWNNUMBERS(R.string.lotto5jr_last_drawn_numbers, 72),
        VAS_LOTTO3_LASTDRAWNNUMBERS_CHECK(R.string.lotto3_last_drawn_numbers, 73),
        VAS_BORLETTE_LASTDRAWNNUMBERS_CHECK(R.string.bolet_last_drawn_numbers, 74),
        VAS_LOTTO5_LASTDRAWNNUMBERS_CHECK(R.string.lotto5_last_drawn_numbers, 75),
        VAS_(0, 76),
        BOLOTO_TICKET_PURCHASED_2(R.string.boloto_ticket_purchased, 78),
        WIN_BOLOTO_2(R.string.boloto_ticket_win_trans, 79),
        WRITE_OFF_FOR_REGISTRATION_OF_REFERRAL_PROGRAM(0, 80),
        REFERRAL_PROGRAM_PAYOUT(0, 81),
        CREDITING_BOLOTO_CASHBACK(R.string.credits_added_to_acc, 82),
        CREDITING_BORLETTE_CASHBACK(R.string.credits_added_to_acc, 83),
        CREDITING_MARIAGE_CASHBACK(R.string.credits_added_to_acc, 84),
        CREDITING_LOTTO3_CASHBACK(R.string.credits_added_to_acc, 85),
        CREDITING_LOTTO4_CASHBACK(R.string.credits_added_to_acc, 86),
        CREDITING_LOTTO5_CASHBACK(R.string.credits_added_to_acc, 87),
        CREDITING_LOTTO5_5G_CASHBACK(R.string.credits_added_to_acc, 88),
        CREDITING_BOLOTO_CASHBACK_(R.string.credits_added_to_acc, 89),
        WELCOME_BONUS_ENROLLMENT(R.string.welcome_bonus, 90),
        RECHARGE_USER_ACCOUNT_FROM_MONCASH_FROM_APP(R.string.reload_moncash_trans, 94),

        VAS_ROYAL5(R.string.vas_royal5, 95),
        ROYAL5_TICKET(R.string.royal5_ticket, 96),
        ROYAL5_WIN(R.string.royal5_string, 97),
        VAS_ROYAL5_SAME_TICKET(R.string.royal5_same_ticket, 98),
        VAS_LOTTO5X_SAME_TICKET(R.string.lotto5x_same_ticket, 99),
        VAS_BOLOTO(R.string.boloto_last_drawn, 100),
        VAS_BOLOTO_SAME_TICKET(R.string.boloto_same_ticket, 101),
        ROYAL5_CASHBACK(R.string.royal5_cashback, 102),
        INSTABOUL_TICKET(R.string.instaboul_ticket, 103),
        INSTABOUL_WIN(R.string.instaboul_win, 104),
        INSTABOUL_LAST_DRAWN(R.string.instaboul_last_drawn, 105),
        INSTABOUL_CASHBACK(R.string.instaboul_cashback, 106);

        private final int message;
        private final int code;

        Transactions(int messageRes, int code) {
            this.message = messageRes;
            this.code = code;
        }

        public int getMessage() {
            return message;
        }

        public int getCode() {
            return code;
        }
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", date='" + date + '\'' +
                ", date8601='" + date8601 + '\'' +
                ", type=" + type +
                ", amount=" + amount +
                '}';
    }
}
