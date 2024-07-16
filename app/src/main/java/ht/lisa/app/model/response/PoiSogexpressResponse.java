package ht.lisa.app.model.response;

import java.util.List;

import ht.lisa.app.model.Poi;

public class PoiSogexpressResponse extends BaseResponse {

    private List<Poi> poiSogexpress;

    public PoiSogexpressResponse(List<Poi> poiSogexpress) {
        this.poiSogexpress = poiSogexpress;
    }

    public List<Poi> getPoiSogexpress() {
        return poiSogexpress;
    }

    public void setPoiSogexpress(List<Poi> poiSogexpress) {
        this.poiSogexpress = poiSogexpress;
    }
}
