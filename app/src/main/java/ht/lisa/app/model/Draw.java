package ht.lisa.app.model;

import android.content.Context;

import java.util.ArrayList;

import ht.lisa.app.LisaApp;
import ht.lisa.app.R;

public class Draw {

    public final static String BOLOTO = "boloto";
    public final static String ROYAL5 = "royal5";
    public final static String BOLET = "bolet";
    public final static String MARIAGE = "mariage";
    public final static String LOTTO3 = "lotto3";
    public final static String LOTTO4 = "lotto4";
    public final static String LOTTO5 = "lotto5";
    public final static String LOTTO5ROYAL = "royal5";
    public final static String LOTTO5JR = "lotto5/5g";
    public final static String LOTTO5JR_NEW = "lotto5jr";
    public final static String LOTTO5P5 = "lotto5p5";

    private final static int BOLOTO_ORDER = 8;
    private final static int BOLET_ORDER = 7;
    private final static int MARYAJ_ORDER = 6;
    private final static int LOTTO3_ORDER = 5;
    private final static int LOTTO4_ORDER = 4;
    private final static int LOTTO5_ORDER = 3;
    private final static int LOTTO5JR_ORDER = 2;
    private final static int ROYAL5_ORDER = 1;

    private int draw;
    private String region;
    private String num;
    private String boloto;
    private String bolet;
    private String mariage;
    private String lotto3;
    private String lotto4;
    private String lotto5;
    private String lotto5jr;
    private String royal5;

    public static ArrayList<String> getDrawNameList() {
        ArrayList<String> drawNameList = new ArrayList<>();
        drawNameList.add(BOLOTO);
        drawNameList.add(BOLET);
        drawNameList.add(MARIAGE);
        drawNameList.add(LOTTO3);
        drawNameList.add(LOTTO4);
        drawNameList.add(LOTTO5);
        drawNameList.add(LOTTO5P5);
        drawNameList.add(ROYAL5);
        return drawNameList;
    }

    public static ArrayList<String> getDrawResponseNameList() {
        ArrayList<String> drawNameList = new ArrayList<>();
        drawNameList.add(BOLOTO);
        drawNameList.add(BOLET);
        drawNameList.add(MARIAGE);
        drawNameList.add(LOTTO3);
        drawNameList.add(LOTTO4);
        drawNameList.add(LOTTO5);
        drawNameList.add(LOTTO5JR_NEW);
        drawNameList.add(ROYAL5);
        return drawNameList;
    }

    public int getDraw() {
        return draw;
    }

    public String getNum() {
        return num;
    }

    public String getBoloto() {
        return boloto;
    }

    public String getBolet() {
        return bolet;
    }

    public String getMariage() {
        return mariage;
    }

    public String getLotto3() {
        return lotto3;
    }

    public String getLotto4() {
        return lotto4;
    }

    public String getLotto5() {
        return lotto5;
    }

    public String getLotto5jr() {
        return lotto5jr;
    }

    public String getRoyal5() {
        return royal5;
    }

    public String getRegion() {
        return region;
    }

    public static int getDrawTypeImage(Context context) {
        return LisaApp.getInstance().isFrench(context) ? R.drawable.ic_draw_type_image_fr : LisaApp.getInstance().isKreyol(context) ? R.drawable.ic_draw_type_image_ht : R.drawable.ic_draw_type_image;
    }

    public static int getOrderByName(String name) {
        switch (name) {
            case BOLOTO:
                return BOLOTO_ORDER;
            case BOLET:
                return BOLET_ORDER;
            case MARIAGE:
                return MARYAJ_ORDER;
            case LOTTO3:
                return LOTTO3_ORDER;
            case LOTTO4:
                return LOTTO4_ORDER;
            case LOTTO5:
                return LOTTO5_ORDER;
            case ROYAL5:
                return ROYAL5_ORDER;
            default:
                return LOTTO5JR_ORDER;
        }
    }

    public static int getLayoutByName(String name) {
        int layout;
        switch (name.toLowerCase()) {
            case BOLOTO:
                layout = R.layout.item_boloto;
                break;
            case BOLET:
                layout = R.layout.item_bolet;
                break;
            case MARIAGE:
                layout = R.layout.item_mariaj;
                break;
            case LOTTO5JR:
            case LOTTO5JR_NEW:
            case LOTTO5P5:
                layout = R.layout.item_lotto5jr;
                break;
            case LOTTO3:
                layout = R.layout.item_lotto3;
                break;
            case LOTTO4:
                layout = R.layout.item_lotto4;
                break;
            case LOTTO5:
                layout = R.layout.item_lotto5;
                break;
            case LOTTO5ROYAL:
                layout = R.layout.item_lotto5_royal;
                break;
            default:
                layout = 0;
                break;
        }
        return layout;
    }

    public static int getGameBgByName(String name) {
        int layout;
        switch (name.toLowerCase()) {
            case BOLOTO:
                layout = R.drawable.bg_boloto;
                break;
            case BOLET:
                layout = R.drawable.bg_bolet;
                break;
            case MARIAGE:
                layout = R.drawable.bg_maryaj;
                break;
            case LOTTO5JR:
            case LOTTO5JR_NEW:
            case LOTTO5P5:
                layout = R.drawable.bg_lotto5jr;
                break;
            case LOTTO3:
                layout = R.drawable.bg_lotto3;
                break;
            case LOTTO4:
                layout = R.drawable.bg_lotto4;
                break;
            case LOTTO5:
                layout = R.drawable.bg_lotto5;
                break;
            case ROYAL5:
                layout = R.drawable.royal5_bg;
                break;
            default:
                layout = 0;
                break;
        }
        return layout;
    }

    public static int getGamesDescription(String name) {
        int description;
        switch (name.toLowerCase()) {
            case BOLOTO:
                description = R.string.boloto_games_description;
                break;
            case BOLET:
                description = R.string.bolet_games_description;
                break;
            case MARIAGE:
                description = R.string.maryaj_games_description;
                break;
            case LOTTO5JR:
            case LOTTO5JR_NEW:
            case LOTTO5P5:
                description = R.string.lotto5jr_games_description;
                break;
            case LOTTO3:
                description = R.string.lotto3_games_description;
                break;
            case LOTTO4:
                description = R.string.lotto4_games_description;
                break;
            case LOTTO5:
                description = R.string.lotto5_games_description;
                break;
            case ROYAL5:
                description = R.string.royal5_games_description;
                break;
            default:
                description = 0;
                break;
        }
        return description;
    }

    public static int getNumsAndBetDescription(String name) {
        int description;
        switch (name.toLowerCase()) {
            case BOLOTO:
                description = R.string.boloto_numbers_description;
                break;
            case BOLET:
                description = R.string.numbers_and_bet_description;
                break;
            case MARIAGE:
                description = R.string.maryaj_numbers_description;
                break;
            case LOTTO5JR:
            case LOTTO5JR_NEW:
            case LOTTO5P5:
            case ROYAL5:
                description = R.string.lotto5jr_numbers_description;
                break;
            case LOTTO3:
                description = R.string.lotto3_numbers_description;
                break;
            case LOTTO4:
                description = R.string.lotto4_numbers_description;
                break;
            case LOTTO5:
                description = R.string.lotto5_numbers_description;
                break;
            default:
                description = 0;
                break;
        }
        return description;
    }

    public static int getQpDrawableByGame(String name) {
        int drawable;
        switch (name.toLowerCase()) {
            case BOLOTO:
                drawable = R.drawable.ic_boloto_qp;
                break;
            case BOLET:
            case MARIAGE:
                drawable = R.drawable.ic_bolet_maryaj_qp;
                break;
            case LOTTO5JR:
            case LOTTO5JR_NEW:
            case LOTTO5P5:
                drawable = R.drawable.ic_lotto5jr_qp;
                break;
            case LOTTO3:
                drawable = R.drawable.ic_lotto3_qp;
                break;
            case LOTTO4:
                drawable = R.drawable.ic_lotto4_qp;
                break;
            case LOTTO5:
                drawable = R.drawable.ic_lotto5_qp;
                break;
            case ROYAL5:
                drawable = R.drawable.ic_royal5_qp;
                break;
            default:
                drawable = 0;
                break;
        }
        return drawable;
    }

    public static int getOptionsBgByName(String name) {
        int color;
        switch (name.toLowerCase()) {
            case LOTTO4:
                color = R.color.lotto4Options;
                break;
            case LOTTO5:
                color = R.color.lotto5Options;
                break;
            default:
                color = android.R.color.transparent;
                break;
        }
        return color;
    }

    public static int getTicketHeaderBG(String name) {
        int headerBg;
        switch (name.toLowerCase()) {
            case BOLET:
            case MARIAGE:
                headerBg = R.color.boletMaryajSelection;
                break;
            case LOTTO5JR:
            case LOTTO5JR_NEW:
            case LOTTO5P5:
                headerBg = R.color.lotto5jrSelection;
                break;
            case LOTTO3:
                headerBg = R.color.lotto3Selection;
                break;
            case LOTTO4:
                headerBg = R.color.lotto4Selection;
                break;
            case LOTTO5:
                headerBg = R.color.lotto5Selection;
                break;
            case ROYAL5:
                headerBg = R.color.royal5Selection;
                break;
            case BOLOTO:
            default:
                headerBg = R.color.bolotoSelection;
                break;
        }
        return headerBg;
    }

    public static int getTicketBall(String name) {
        int headerBg;
        switch (name.toLowerCase()) {
            case BOLET:
                headerBg = R.drawable.ic_ball_bolet;
                break;
            case MARIAGE:
                headerBg = R.drawable.ic_ball_mariaj;
                break;
            case LOTTO5JR:
            case LOTTO5JR_NEW:
            case LOTTO5P5:
                headerBg = R.drawable.ic_ball_lotto5jr;
                break;
            case LOTTO3:
                headerBg = R.drawable.ic_ball_lotto3;
                break;
            case LOTTO4:
                headerBg = R.drawable.ic_ball_lotto4;
                break;
            case LOTTO5:
                headerBg = R.drawable.ic_ball_lotto5;
                break;
            case BOLOTO:
            default:
                headerBg = R.drawable.ic_ball_boloto;
                break;
        }
        return headerBg;
    }

    public static int getGameNumCount(String gameName) {
        int gameNumCount = 0;
        switch (gameName.toLowerCase()) {
            case BOLOTO:
                gameNumCount = 6;
                break;
            case Draw.BOLET:
                gameNumCount = 2;
                break;
            case Draw.LOTTO3:
                gameNumCount = 3;
                break;
            case Draw.LOTTO4:
            case Draw.MARIAGE:
                gameNumCount = 4;
                break;
            case LOTTO5:
            case Draw.LOTTO5JR:
            case LOTTO5JR_NEW:
            case LOTTO5P5:
            case ROYAL5:
                gameNumCount = 5;
                break;

        }
        return gameNumCount;

    }

    public static int getGameGuidePageCount(String gameName) {
        int pageCount = 0;
        switch (gameName.toLowerCase()) {

            case BOLOTO:
            case LOTTO3:
            case LOTTO5JR:
            case LOTTO5JR_NEW:
            case LOTTO5P5:
            case ROYAL5:
                pageCount = 6;
                break;

            case LOTTO4:
            case BOLET:
            case MARIAGE:
            case LOTTO5:
                pageCount = 5;
                break;
        }
        return pageCount;
    }

    public static int getTicketHeaderByName(String name, int position, Context context) {
        int drawable;
        boolean isFr = LisaApp.getInstance().isFrench(context);
        boolean isEn = !LisaApp.getInstance().isKreyol(context) && !LisaApp.getInstance().isFrench(context);
        switch (name.toLowerCase()) {
            case BOLOTO:
                drawable = isEn ? R.drawable.ticket_header_boloto : isFr ? R.drawable.ticket_header_boloto_fr : R.drawable.ticket_header_boloto_kr;
                break;
            case BOLET:
                drawable = isEn ? R.drawable.ticket_header_bolet : isFr ? R.drawable.ticket_header_bolet_fr : R.drawable.ticket_header_bolet_kr;
                break;
            case MARIAGE:
                drawable = isEn ? R.drawable.ticket_header_maryaj : isFr ? R.drawable.ticket_header_maryaj_fr : R.drawable.ticket_header_maryaj_kr;
                break;
            case LOTTO5JR:
            case LOTTO5JR_NEW:
            case LOTTO5P5:
                drawable = isEn ? R.drawable.ticket_header_lotto5jr : isFr ? R.drawable.ticket_header_lotto5jr_fr : R.drawable.ticket_header_lotto5jr_kr;
                break;
            case LOTTO3:
                drawable = isEn ? R.drawable.ticket_header_lotto3 : isFr ? R.drawable.ticket_header_lotto3_fr : R.drawable.ticket_header_lotto3_kr;
                break;
            case LOTTO4:
                if (position == 1)
                    drawable = isEn ? R.drawable.ticket_header_lotto4 : isFr ? R.drawable.ticket_header_lotto4_fr : R.drawable.ticket_header_lotto4_kr;
                else
                    drawable = isEn ? R.drawable.ticket_header_lotto4_full : isFr ? R.drawable.ticket_header_lotto4_full_fr : R.drawable.ticket_header_lotto4_full_kr;
                break;
            case LOTTO5:
                if (position == 1)
                    drawable = isEn ? R.drawable.ticket_header_lotto5 : isFr ? R.drawable.ticket_header_lotto5_fr : R.drawable.ticket_header_lotto5_kr;
                else
                    drawable = isEn ? R.drawable.ticket_header_lotto5_full : isFr ? R.drawable.ticket_header_lotto5_full_fr : R.drawable.ticket_header_lotto5_full_kr;
                break;
            case ROYAL5:
                drawable = isEn ? R.drawable.ticket_header_lotto5royal : isFr ? R.drawable.ticket_header_lotto5royal_fr : R.drawable.ticket_header_lotto5royal_kr;
                break;
            default:
                drawable = 0;
                break;
        }
        return drawable;
    }

    public static boolean isBetNecessary(String gameName) {
        return gameName.equals(Draw.LOTTO3) || gameName.equals(Draw.LOTTO4) || gameName.equals(Draw.LOTTO5) || gameName.equals(Draw.BOLET) || gameName.equals(Draw.MARIAGE);
    }


}
