package ht.lisa.app.model;

import android.annotation.SuppressLint;
import android.content.Context;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import ht.lisa.app.LisaApp;
import ht.lisa.app.R;
import ht.lisa.app.model.response.BaseResponse;
import ht.lisa.app.util.Constants;
import ht.lisa.app.util.DateTimeUtil;

public class Message extends BaseResponse implements Serializable {


    private static final String DRAW_FORMAT = "dd/MM/yyyy hh:mm a";


    public static final int STATE_MESSAGE_NOT_FOUND = -1;

    public static final int MSG_TYPE_SIMPLE_MESSAGE = 0;
    public static final int MSG_TYPE_PAYMENT_LINK = 1;
    public static final int MSG_TYPE_PAYMENT_SUCCESS = 2;
    public static final int MSG_TYPE_PIN_FUNDS = 3;
    public static final int MSG_TYPE_FUNDS_BLOCKED = 4;
    public static final int MSG_TYPE_FUNDS_ISSUED = 5;
    public static final int MSG_TYPE_FUNDS_RETURNED = 6;
    public static final int MSG_TYPE_MONCASH_TRANSFER = 7;
    public static final int MSG_TYPE_BOLOTO_TICKET_BOUGHT = 8;
    public static final int MSG_TYPE_BOLET_TICKET_BOUGHT = 9;
    public static final int MSG_TYPE_MARYAJ_TICKET_BOUGHT = 10;
    public static final int MSG_TYPE_ROYAL5_TICKET_BOUGHT = 11;
    public static final int MSG_TYPE_ROYAL5_WIN = 12;
    public static final int MSG_TYPE_ROYAL5_JACKPOT = 13;
    public static final int MSG_TYPE_BOLOTO_WIN = 14;
    public static final int MSG_TYPE_BOLOTO_JACKPOT = 15;
    public static final int MSG_TYPE_BOLET_WIN = 16;
    public static final int MSG_TYPE_MARYAJ_WIN = 17;
    public static final int MSG_TYPE_LOTTO3_TICKET_BOUGHT = 18;
    public static final int MSG_TYPE_LOTTO3_WIN = 19;
    public static final int MSG_TYPE_LOTTO4_TICKET_BOUGHT = 20;
    public static final int MSG_TYPE_LOTTO4_WIN = 21;
    public static final int MSG_TYPE_LOTTO5_TICKET_BOUGHT = 22;
    public static final int MSG_TYPE_LOTTO5_WIN = 23;
    public static final int MSG_TYPE_LOTTO5JR_TICKET_BOUGHT = 24;
    public static final int MSG_TYPE_LOTTO5JR_WIN = 25;
    public static final int MSG_TYPE_LOTTO5JR_JACKPOT = 26;
    public static final int MSG_TYPE_MONCASH_CASH_OUT = 29;

    @SerializedName("id")
    private String id;
    private int amount;
    private String expire;
    private String en;
    private String EN;
    private String num;
    private String region;
    private long draw;
    private String fr;
    private String FR;
    private String ht;
    private String HT;
    private int code;
    private int cost;
    private int bonus;
    private int total;
    private String win;
    private long payout;
    private int digicel;
    private String url;
    private String pin;
    @SerializedName("order_id")
    private String orderId;
    @SerializedName("msg_id")
    private String msgId;
    @SerializedName("msg_type")
    private int msgType;
    @SerializedName("msg_time")
    private int msgTime;

    public String getId() {
        return id;
    }

    public int getAmount() {
        return amount;
    }

    public String getExpire() {
        return expire;
    }

    public String getWin() {
        return win;
    }

    public String getOrderId() {
        return orderId;
    }

    public String getPin() {
        return pin;
    }

    public long getPayout() {
        return payout;
    }

    public String getUrl() {
        return url;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }

    public String getNum() {
        return num;
    }

    public String getRegion() {
        return region;
    }

    public long getDraw() {
        return draw;
    }

    public int getCode() {
        return code;
    }

    public int getCost() {
        return cost;
    }

    public int getBonus() {
        return bonus;
    }

    public int getDigicel() {
        return digicel;
    }

    public String getMsgId() {
        return msgId;
    }

    public String getEn() {
        return en;
    }

    public String getFr() {
        return fr;
    }

    public String getHt() {
        return ht;
    }

    public int getMsgType() {
        return msgType;
    }

    public int getMsgTime() {
        return msgTime;
    }

    private String getFormattedAmount(int amount) {
        double formattedAmount = amount / 100.00;
        String formattedAmountString = String.valueOf(formattedAmount);
        if (formattedAmountString.endsWith(".00")) {
            formattedAmountString = formattedAmountString.replace(".00", "");
        }
        return formattedAmountString;
    }

    public String getMessageTile(Context context) {
        String title = "";
        switch (msgType) {
            case Message.MSG_TYPE_SIMPLE_MESSAGE:
                switch (LisaApp.getInstance().getLanguage(context)) {
                    case Constants.LANGUAGE_EN:
                        title = EN;
                        break;
                    case Constants.LANGUAGE_FR:
                        title = FR;
                        break;
                    case Constants.LANGUAGE_KR:
                        title = HT;
                        break;
                }
                break;
            case Message.MSG_TYPE_PAYMENT_LINK:
            case Message.MSG_TYPE_PAYMENT_SUCCESS:
            case Message.MSG_TYPE_MONCASH_TRANSFER:
                title = context.getString(R.string.mon_cash_lisa);
                break;
            case Message.MSG_TYPE_MONCASH_CASH_OUT:
                title = context.getString(R.string.lisa_mon_cash);
                break;
            case Message.MSG_TYPE_PIN_FUNDS:
            case Message.MSG_TYPE_FUNDS_BLOCKED:
            case Message.MSG_TYPE_FUNDS_ISSUED:
            case Message.MSG_TYPE_FUNDS_RETURNED:
                title = context.getString(R.string.sogexpress);
                break;
            case Message.MSG_TYPE_BOLOTO_TICKET_BOUGHT:
            case Message.MSG_TYPE_BOLOTO_WIN:
            case Message.MSG_TYPE_BOLOTO_JACKPOT:
                title = context.getString(R.string.boloto);
                break;
            case Message.MSG_TYPE_ROYAL5_WIN:
            case Message.MSG_TYPE_ROYAL5_TICKET_BOUGHT:
            case Message.MSG_TYPE_ROYAL5_JACKPOT:
                title = context.getString(R.string.lotto5_royal);
                break;
            case Message.MSG_TYPE_LOTTO3_TICKET_BOUGHT:
            case Message.MSG_TYPE_LOTTO3_WIN:
                title = context.getString(R.string.lotto3);
                break;
            case Message.MSG_TYPE_LOTTO4_TICKET_BOUGHT:
            case Message.MSG_TYPE_LOTTO4_WIN:
                title = context.getString(R.string.lotto4);
                break;
            case Message.MSG_TYPE_LOTTO5_TICKET_BOUGHT:
            case Message.MSG_TYPE_LOTTO5_WIN:
                title = context.getString(R.string.lotto5);
                break;
            case Message.MSG_TYPE_LOTTO5JR_TICKET_BOUGHT:
            case Message.MSG_TYPE_LOTTO5JR_JACKPOT:
            case Message.MSG_TYPE_LOTTO5JR_WIN:
                title = context.getString(R.string.lotto5jr);
                break;
            case Message.MSG_TYPE_BOLET_WIN:
            case Message.MSG_TYPE_BOLET_TICKET_BOUGHT:
                title = context.getString(R.string.borlet);
                break;
            case Message.MSG_TYPE_MARYAJ_WIN:
            case Message.MSG_TYPE_MARYAJ_TICKET_BOUGHT:
                title = context.getString(R.string.maryage);
                break;
            default:
                title = "Message";
                break;
        }
        return title;
    }

    @SuppressLint("StringFormatMatches")
    public String getMessageText(Context context) {
        String text = "";
        switch (msgType) {
            case Message.MSG_TYPE_SIMPLE_MESSAGE:
                switch (LisaApp.getInstance().getLanguage(context)) {
                    case Constants.LANGUAGE_EN:
                        text = en;
                        break;
                    case Constants.LANGUAGE_FR:
                        text = fr;
                        break;
                    case Constants.LANGUAGE_KR:
                        text = ht;
                        break;
                }
                break;
            case Message.MSG_TYPE_PAYMENT_LINK:
                text = context.getString(R.string.payment_link_message, getFormattedAmount(amount));
                break;
            case Message.MSG_TYPE_PAYMENT_SUCCESS:
            case Message.MSG_TYPE_MONCASH_TRANSFER:
                text = context.getString(R.string.success_payment_message_bonus, getFormattedAmount(getTotal()), getFormattedAmount(getAmount()), getFormattedAmount((getTotal() - getAmount())), getId());
                break;
            case MSG_TYPE_PIN_FUNDS:
                text = context.getString(R.string.pin_funds_message, getFormattedAmount(getAmount()), getPin());
                break;
            case MSG_TYPE_FUNDS_BLOCKED:
                text = context.getString(R.string.funds_blocked_message, getFormattedAmount(getAmount()), getExpire(), getId());
                break;
            case MSG_TYPE_FUNDS_ISSUED:
                text = context.getString(R.string.funds_issued_message, getFormattedAmount(getAmount()), getExpire(), getId());
                break;
            case MSG_TYPE_FUNDS_RETURNED:
                text = context.getString(R.string.funds_returned_message, getFormattedAmount(getAmount()), getId());
                break;
            /*case MSG_TYPE_MONCASH_TRANSFER:
                text = context.getString(R.string.moncash_transfer_message, getFormattedAmount(getAmount()), getId());
                break;*/
            case MSG_TYPE_BOLOTO_TICKET_BOUGHT:
                text = context.getString(R.string.boloto_ticket_bought, getNum(), getAccepted(context, getCode()), getRegion(),
                        DateTimeUtil.getTransactionDateFormat(String.valueOf(getDraw()), context), getId(), getFormattedAmount(getBonus()), getLisa(getBonus(), getCost(), getDigicel()), getFormattedAmount(getDigicel()));
                break;
            case MSG_TYPE_ROYAL5_TICKET_BOUGHT:
                text = context.getString(R.string.royal5_ticket_bought, getNum(), getAccepted(context, getCode()), getRegion(),
                        DateTimeUtil.getTransactionDateFormat(String.valueOf(getDraw()), context), getId(), getFormattedAmount(getBonus()), getLisa(getBonus(), getCost(), getDigicel()), getFormattedAmount(getDigicel()));
                break;
            case MSG_TYPE_LOTTO3_TICKET_BOUGHT:
                text = context.getString(R.string.lotto3_ticket_bought, getNum(), getAccepted(context, getCode()), getRegion(), DateTimeUtil.getTransactionDateFormat(String.valueOf(getDraw()), context), getId(), getFormattedAmount(getBonus()), getLisa(getBonus(), getCost(), getDigicel()), getFormattedAmount(getDigicel()));
                break;
            case MSG_TYPE_LOTTO4_TICKET_BOUGHT:
                text = context.getString(R.string.lotto4_ticket_bought, getNum(), getRegion(), DateTimeUtil.getTransactionDateFormat(String.valueOf(getDraw()), context), getId(), getFormattedAmount(getBonus()), getLisa(getBonus(), getCost(), getDigicel()), getFormattedAmount(getDigicel()));
                break;
            case MSG_TYPE_LOTTO5_TICKET_BOUGHT:
                text = context.getString(R.string.lotto5_ticket_bought, getNum(), getRegion(), DateTimeUtil.getTransactionDateFormat(String.valueOf(getDraw()), context), getId(), getFormattedAmount(getBonus()), getLisa(getBonus(), getCost(), getDigicel()), getFormattedAmount(getDigicel()));
                break;
            case MSG_TYPE_LOTTO5JR_TICKET_BOUGHT:
                text = context.getString(R.string.lotto5jr_ticket_bought, getNum(), getRegion(), DateTimeUtil.getTransactionDateFormat(String.valueOf(getDraw()), context), getId(), getFormattedAmount(getBonus()), getLisa(getBonus(), getCost(), getDigicel()), getFormattedAmount(getDigicel()));
                break;
            case MSG_TYPE_BOLOTO_WIN:
                text = context.getString(R.string.boloto_ticket_win, getRegion(), DateTimeUtil.getTransactionDateFormat(String.valueOf(getDraw()), context), getPayout());
                break;
            case MSG_TYPE_BOLOTO_JACKPOT:
                text = context.getString(R.string.boloto_ticket_win_jackpot, getRegion(), DateTimeUtil.getTransactionDateFormat(String.valueOf(getDraw()), context), getPayout());
                break;
            case MSG_TYPE_ROYAL5_WIN:
                text = context.getString(R.string.royal5_ticket_win, getRegion(), DateTimeUtil.getTransactionDateFormat(String.valueOf(getDraw()), context), getPayout());
                break;
            case MSG_TYPE_ROYAL5_JACKPOT:
                text = context.getString(R.string.royal5_ticket_win_jackpot, getRegion(), DateTimeUtil.getTransactionDateFormat(String.valueOf(getDraw()), context), getPayout());
                break;
            case MSG_TYPE_LOTTO5JR_JACKPOT:
                text = context.getString(R.string.lotto5jr_ticket_win_jackpot, getRegion(), DateTimeUtil.getTransactionDateFormat(String.valueOf(getDraw()), context), getPayout());
                break;
            case MSG_TYPE_BOLET_WIN:
                text = context.getString(R.string.bolet_ticket_win, getRegion(), DateTimeUtil.getTransactionDateFormat(String.valueOf(getDraw()), context), getPayout());
                break;
            case MSG_TYPE_MARYAJ_WIN:
                text = context.getString(R.string.mariage_ticket_win, getRegion(), DateTimeUtil.getTransactionDateFormat(String.valueOf(getDraw()), context), getPayout());
                break;
            case MSG_TYPE_LOTTO3_WIN:
                text = context.getString(R.string.lotto3_ticket_win, getRegion(), DateTimeUtil.getTransactionDateFormat(String.valueOf(getDraw()), context), getPayout());
                break;
            case MSG_TYPE_LOTTO4_WIN:
                text = context.getString(R.string.lotto4_ticket_win, getRegion(), DateTimeUtil.getTransactionDateFormat(String.valueOf(getDraw()), context), getPayout());
                break;
            case MSG_TYPE_LOTTO5_WIN:
                text = context.getString(R.string.lotto5_ticket_win, getRegion(), DateTimeUtil.getTransactionDateFormat(String.valueOf(getDraw()), context), getPayout());
                break;
            case MSG_TYPE_LOTTO5JR_WIN:
                text = context.getString(R.string.lotto5jr_ticket_win, getRegion(), DateTimeUtil.getTransactionDateFormat(String.valueOf(getDraw()), context), getPayout());
                break;
            case MSG_TYPE_BOLET_TICKET_BOUGHT:
                text = context.getString(R.string.bolet_ticket_bought, getNum(), getAccepted(context, getCode()), getRegion(), DateTimeUtil.getTransactionDateFormat(String.valueOf(getDraw()), context), getId(), getFormattedAmount(getBonus()), getLisa(getBonus(), getCost(), getDigicel()), getFormattedAmount(getDigicel()));
                break;
            case MSG_TYPE_MARYAJ_TICKET_BOUGHT:
                text = context.getString(R.string.mariage_ticket_bought, getNum(), getAccepted(context, getCode()), getRegion(), DateTimeUtil.getTransactionDateFormat(String.valueOf(getDraw()), context), getId(), getFormattedAmount(getBonus()), getLisa(getBonus(), getCost(), getDigicel()), getFormattedAmount(getDigicel()));
                break;
            case MSG_TYPE_MONCASH_CASH_OUT:
                text = context.getString(R.string.moncash_cash_out, getFormattedAmount(getAmount()));
                break;

            default:
                text = "Unprocessed message with type " + getMsgType();
                break;
        }
        return text;
    }

    private String getAccepted(Context context, int code) {
        return context.getString(code >= 249 ? R.string.not_accepted_lower : R.string.accepted);
    }


    @SuppressLint("DefaultLocale")
    private String getLisa(int bonus, int cost, int digicel) {
        double value = (double) (cost - (bonus + digicel)) / 100;
        if (value == (int) value) {
            return String.valueOf(value);
        } else {
            return String.format("%.2f", value);
        }
    }

    public int getTotal() {
        return total;
    }
}
