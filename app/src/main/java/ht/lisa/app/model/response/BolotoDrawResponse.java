package ht.lisa.app.model.response;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class BolotoDrawResponse extends BaseResponse {

    private static final String REGION_NY = "NY";
    private static final String REGION_FL = "FL";
    private static final String REGION_GA = "GA";

    @SerializedName("dataset")
    private ArrayList<RegionDrawResponse> regionDraws;

    public ArrayList<RegionDrawResponse> getRegionDraws() {
        return regionDraws;
    }

    public RegionDrawResponse getNyDraw() {
        for (RegionDrawResponse draw: regionDraws) {
            if (draw.getRegion().equals(REGION_NY)) {
                return draw;
            }
        }
        return null;
    }

    public RegionDrawResponse getFlDraw() {
        for (RegionDrawResponse draw: regionDraws) {
            if (draw.getRegion().equals(REGION_FL)) {
                return draw;
            }
        }
        return null;
    }

    public RegionDrawResponse getGaDraw() {
        for (RegionDrawResponse draw: regionDraws) {
            if (draw.getRegion().equals(REGION_GA)) {
                return draw;
            }
        }
        return null;
    }

}
