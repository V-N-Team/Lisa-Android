package ht.lisa.app.model;

import android.content.Context;

import androidx.annotation.Nullable;

import java.io.Serializable;

import ht.lisa.app.LisaApp;

public class Ticket implements Serializable {

    public final static String SEPARATOR = "-";

    public static final String NY = "NY";
    public static final String FL = "FL";
    public static final String GA = "GA";

    public static final int BOLOTO_GAME = 1;
    public static final int BORLET_GAME = 2;
    public static final int MARIAGE_GAME = 3;
    public static final int LOTTO3_GAME = 4;
    public static final int LOTTO4_GAME = 5;
    public static final int LOTTO5_GAME = 6;
    public static final int LOTTO5_5G_GAME = 7;
    public static final int LOTTO5_ROYAL_GAME = 8;

    public static final int CODE_REQUEST_CREATED = 0;
    public static final int CODE_PENDING_TICKET = 1;
    public static final int CODE_ACCEPTED = 2;
    public static final int CODE_PENDING_GAME = 3;
    public static final int CODE_LOST = 4;
    public static final int CODE_WIN = 5;
    public static final int CODE_WIN_JACKPOT = 6;
    public static final int CODE_CANCELED = 249;
    public static final int CODE_PAYMENT_GATEWAY_ERROR = 250;
    public static final int CODE_POSTPAID_SETTLEMENT_SYSTEM = 251;
    public static final int CODE_LACK_OF_FUNDS = 252;
    public static final int CODE_RISK_CONTROL_SYSTEM = 253;
    public static final int CODE_INCORRECT_TIME = 254;
    public static final int CODE_BLOCKED = 255;

    private static final String CODE_CANCELED_REASON = "Ticket canceled";
    private static final String CODE_PAYMENT_GATEWAY_ERROR_REASON = "Payment gateway error";
    private static final String CODE_PAID_SETTLEMENT_SYSTEM_REASON = "Postpaid settlement system";
    private static final String CODE_LACK_OF_FUNDS_REASON = "Insufficient funds\nRecharge your account to keep playing!";
    private static final String CODE_LACK_OF_FUNDS_REASON_FR = "Fonds insuffisants\nChargé votre compte pour continuer à jouer!";
    private static final String CODE_LACK_OF_FUNDS_REASON_KR = "Balans two piti\nRechaje kont ou pou kontinye jwe!";
    private static final String CODE_RISK_CONTROL_SYSTEM_REASON = "Risk control system";
    private static final String CODE_INCORRECT_TIME_REASON = "Incorrect time";
    private static final String CODE_BLOCKED_REASON = "Blocked";


    private int game;
    private long date;
    private long draw;
    private String num;
    private int type;
    private int code;
    private int quick;
    private int auto;
    private int cost;
    private String region;
    private int bonus;
    private int digicel;
    private String win;
    @Nullable
    private boolean combo;
    @Nullable
    private boolean subscribe;
    @Nullable
    private String name;
    @Nullable
    private int comboCost;
    /*@Nullable
    private boolean nyDraw;*/

    public Ticket() {
    }

    public Ticket(String name, long draw, String num, int cost, boolean combo, String region, int type, boolean subscribe) {
        this.name = name;
        this.region = region;
        this.draw = draw;
        this.num = num;
        this.cost = cost;
        this.combo = combo;
        this.type = type;
        this.subscribe = subscribe;
    }

    public Ticket(String name, long draw, String num, int cost, int comboCost, boolean combo, String region, int type, boolean subscribe) {
        this.name = name;
        this.draw = draw;
        this.region = region;
        this.num = num;
        this.cost = cost;
        this.comboCost = comboCost;
        this.combo = combo;
        this.type = type;
        this.subscribe = subscribe;
    }

    public boolean isBoloto() {
        return getGame() == BOLOTO_GAME;
    }

    public boolean isBorlet() {
        return getGame() == BORLET_GAME;
    }

    public boolean isMariage() {
        return getGame() == MARIAGE_GAME;
    }

    public boolean isLotto3() {
        return getGame() == LOTTO3_GAME;
    }

    public boolean isLotto4() {
        return getGame() == LOTTO4_GAME;
    }

    public boolean isLotto5() {
        return getGame() == LOTTO5_GAME;
    }

    public boolean isLotto5Royal() {
        return getGame() == LOTTO5_ROYAL_GAME;
    }

    /*public boolean isNyDraw() {
        return nyDraw;
    }*/

    public boolean isLotto55g() {
        return getGame() == LOTTO5_5G_GAME;
    }


    public String[] getDrawArray() {
        return getNum().split(SEPARATOR);
    }

    public int getGame() {
        return game;
    }

    public int getComboCost() {
        return comboCost;
    }

    public void setComboCost(int comboCost) {
        this.comboCost = comboCost;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public long getDraw() {
        return draw;
    }

    public void setDraw(long draw) {
        this.draw = draw;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public int getQuick() {
        return quick;
    }

    public void setQuick(int quick) {
        this.quick = quick;
    }

    public boolean isAuto() {
        return auto == 1;
    }

    public void setAuto(boolean auto) {
        this.auto = auto ? 1 : 0;
    }

    public int getCost() {
        return cost;
    }

    public int getEditedCost() {
        return cost * 100;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public int getBonus() {
        return bonus;
    }

    public void setBonus(int bonus) {
        this.bonus = bonus;
    }

    public int getDigicel() {
        return digicel;
    }

    public void setDigicel(int digicel) {
        this.digicel = digicel;
    }

    public String getWin() {
        return win;
    }

    public void setWin(String win) {
        this.win = win;
    }

    public String getRegion() {
        return region;
    }

    public String getSubscribeRegion() {
        return region;
    }

    public boolean isCombo() {
        return combo;
    }

    public void setCombo(boolean combo) {
        this.combo = combo;
    }

    public boolean isSubscribe() {
        return subscribe;
    }

    public void setSubscribe(boolean subscribe) {
        this.subscribe = subscribe;
    }

    @Nullable
    public String getName() {
        String name = null;
        if (this.name == null) {
            switch (game) {
                case BOLOTO_GAME:
                    name = Draw.BOLOTO;
                    break;
                case BORLET_GAME:
                    name = Draw.BOLET;
                    break;
                case LOTTO3_GAME:
                    name = Draw.LOTTO3;
                    break;
                case LOTTO4_GAME:
                    name = Draw.LOTTO4;
                    break;
                case LOTTO5_GAME:
                    name = Draw.LOTTO5;
                    break;
                case LOTTO5_5G_GAME:
                    name = Draw.LOTTO5JR;
                    break;
                case MARIAGE_GAME:
                    name = Draw.MARIAGE;
                    break;
                case LOTTO5_ROYAL_GAME:
                    name = Draw.ROYAL5;
                    break;
            }
        } else {
            name = this.name;
        }
        return name;
    }

    public void setName(@Nullable String name) {
        this.name = name;
    }

    public boolean isBeforeDraw() {
        return getCode() == CODE_REQUEST_CREATED || getCode() == CODE_PENDING_TICKET || getCode() == CODE_ACCEPTED || getCode() == CODE_PENDING_GAME;
    }

    public boolean isNy() {
        return region != null && region.equals(NY);
    }

    public boolean isGa() {
        return region != null && region.equals(GA);
    }

    public boolean isFl() {
        return region != null && region.equals(FL);
    }

    public boolean isRequestCreated() {
        return getCode() == CODE_REQUEST_CREATED;
    }

    public boolean isPendingTicket() {
        return getCode() == CODE_PENDING_TICKET;
    }

    public boolean isTicketAccepted() {
        return getCode() == CODE_ACCEPTED;
    }

    public boolean isPendingGame() {
        return getCode() == CODE_PENDING_GAME;
    }

    public boolean isGameLost() {
        return getCode() == CODE_LOST;
    }

    public boolean isGameWin() {
        return getCode() == CODE_WIN;
    }

    public boolean isGameWinJackpot() {
        return getCode() == CODE_WIN_JACKPOT;
    }

    public boolean isNotAccepted() {
        return getCode() == CODE_CANCELED || getCode() == CODE_BLOCKED
                || getCode() == CODE_INCORRECT_TIME || getCode() == CODE_POSTPAID_SETTLEMENT_SYSTEM
                || getCode() == CODE_PAYMENT_GATEWAY_ERROR || getCode() == CODE_RISK_CONTROL_SYSTEM
                || getCode() == CODE_LACK_OF_FUNDS;
    }

    public String getNotAcceptedReason(Context context) {
        String reason;
        switch (getCode()) {
            case CODE_CANCELED:
                reason = CODE_CANCELED_REASON;
                break;
            case CODE_PAYMENT_GATEWAY_ERROR:
                reason = CODE_PAYMENT_GATEWAY_ERROR_REASON;
                break;
            case CODE_POSTPAID_SETTLEMENT_SYSTEM:
                reason = CODE_PAID_SETTLEMENT_SYSTEM_REASON;
                break;
            case CODE_LACK_OF_FUNDS:
                if (LisaApp.getInstance().isFrench(context)) {
                    reason = CODE_LACK_OF_FUNDS_REASON_FR;
                } else if (LisaApp.getInstance().isKreyol(context)) {
                    reason = CODE_LACK_OF_FUNDS_REASON_KR;
                } else {
                    reason = CODE_LACK_OF_FUNDS_REASON;
                }
                break;
            case CODE_RISK_CONTROL_SYSTEM:
                reason = CODE_RISK_CONTROL_SYSTEM_REASON;
                break;
            case CODE_INCORRECT_TIME:
                reason = CODE_INCORRECT_TIME_REASON;
                break;
            case CODE_BLOCKED:
                reason = CODE_BLOCKED_REASON;
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + getCode());
        }
        return reason;
    }

}
