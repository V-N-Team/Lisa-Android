package ht.lisa.app.model.response;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class RegionDrawResponse implements Serializable {

    private long prev;
    private long draw;
    private long next;
    private long stop;
    private String region;
    @SerializedName("draw_left")
    private long drawLeft;
    @SerializedName("stop_left")
    private long stopLeft;
    private long lastLeft;
    private long lastStop;

    public void setLastLeft(long lastLeft) {
        this.lastLeft = lastLeft;
    }

    public void setLastStop(long lastStop) {
        this.lastStop = lastStop;
    }

    public long getLastLeft() {
        return lastLeft;
    }

    public long getLastStop() {
        return lastStop;
    }

    public long getPrev() {
        return prev;
    }

    public long getDraw() {
        return draw;
    }

    public void setDrawLeft(long drawLeft) {
        this.drawLeft = drawLeft;
    }

    public long getNext() {
        return next;
    }

    public long getStop() {
        return stop;
    }

    public String getRegion() {
        return region;
    }

    public long getDrawLeft() {
        return drawLeft;
    }

    public long getStopLeft() {
        return stopLeft;
    }
}
