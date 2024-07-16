package ht.lisa.app.model.response;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import ht.lisa.app.model.DrawDetailRate;

public class DrawLotto4Response extends BaseLottoResponse {

    @SerializedName("dataset")
    private ArrayList<DrawDetailRate> drawRates;

    public ArrayList<DrawDetailRate> getDrawRates() {
        return drawRates;
    }

    @Override
    public boolean isDayDraw() {
        return drawRates != null && drawRates.size() > 0 && drawRates.get(0).isDayDraw();
    }
}
