package ht.lisa.app.model.response;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import ht.lisa.app.model.DrawDetailPay;

public class DrawLotto5jrResponse extends BaseLottoResponse {

    @SerializedName("dataset")
    private ArrayList<DrawDetailPay> drawPays;

    public ArrayList<DrawDetailPay> getDrawPays() {
        return drawPays;
    }

    @Override
    public boolean isDayDraw() {
        return drawPays != null && drawPays.size() > 0 && drawPays.get(0).isDayDraw();
    }
}
