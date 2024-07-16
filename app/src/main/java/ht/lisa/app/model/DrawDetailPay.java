package ht.lisa.app.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import ht.lisa.app.LisaApp;
import ht.lisa.app.util.DateTimeUtil;

public class DrawDetailPay {

    private long draw;
    private String num;
    @SerializedName("draw_iso")
    private String drawIso;
    private String region;
    @SerializedName("extra")
    private ArrayList<DrawPay> drawPays;

    public static List<Long> getPayArray(ArrayList<DrawPay> drawPays) {
        List<Long> pays = new ArrayList<>();
        for (DrawPay drawPay : drawPays) {
            if (!pays.contains(drawPay.getPay())) {
                pays.add(drawPay.getPay());
            }
        }
        return pays;
    }

    public String getDrawIso() {
        return drawIso;
    }

    public long getDraw() {
        return draw;
    }

    public String getNum() {
        return num;
    }

    public String getRegion() {
        return region;
    }

    public ArrayList<DrawPay> getDrawPays() {
        return drawPays;
    }

    public boolean isDayDraw() {
        return DateTimeUtil.isDayDraw(drawIso, LisaApp.getInstance());
    }
    public boolean isNightDraw() {
        return DateTimeUtil.isNightDraw(drawIso, LisaApp.getInstance());
    }
    public boolean isEveningDraw() {
        return DateTimeUtil.isEveningDraw(drawIso, LisaApp.getInstance());
    }
}
