package ht.lisa.app.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import ht.lisa.app.LisaApp;
import ht.lisa.app.util.DateTimeUtil;

public class DrawDetailRate {

    private int draw;
    private String num;
    private String region;
    @SerializedName("draw_iso")
    private String drawIso;
    @SerializedName("extra")
    private ArrayList<DrawRate> drawRates;

    public int getDraw() {
        return draw;
    }

    public String getDrawIso() {
        return drawIso;
    }

    public String getNum() {
        return num;
    }

    public String getRegion() {
        return region;
    }

    public ArrayList<DrawRate> getDrawRates() {
        return drawRates;
    }

    public static List<Integer> getRateArray(ArrayList<DrawRate> drawRates) {
        List<Integer> rates = new ArrayList<>();
        for (DrawRate drawRate : drawRates) {
            rates.add(drawRate.getRate());
        }
        return rates;
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
