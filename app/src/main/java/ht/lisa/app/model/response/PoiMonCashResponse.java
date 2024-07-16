package ht.lisa.app.model.response;


import java.util.List;

import ht.lisa.app.model.Poi;

public class PoiMonCashResponse extends BaseResponse {

    private List<Poi> poiMoncCash;

    public PoiMonCashResponse(List<Poi> poiMoncCash) {
        this.poiMoncCash = poiMoncCash;
    }

    public List<Poi> getPoiMoncCash() {
        return poiMoncCash;
    }

    public void setPoiMoncCash(List<Poi> poiMoncCash) {
        this.poiMoncCash = poiMoncCash;
    }
}
