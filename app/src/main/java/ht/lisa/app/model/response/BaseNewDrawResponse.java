package ht.lisa.app.model.response;

import java.io.Serializable;
import java.util.ArrayList;

import ht.lisa.app.util.DateTimeUtil;

public class BaseNewDrawResponse implements Serializable {
    private String name;
    private ArrayList<BaseDrawBody> NY;
    private ArrayList<BaseDrawBody> FL;

    public ArrayList<BaseDrawBody> getFL() {
        return FL;
    }

    public ArrayList<BaseDrawBody> getNY() {
        return NY;
    }

    public BaseDrawBody getDayDraw(boolean ny) {
        if (ny) {
            return DateTimeUtil.isDayDrawNew(NY.get(0).getDate()) ? NY.get(0) : NY.get(1);
        } else {
            return DateTimeUtil.isDayDrawNew(FL.get(0).getDate()) ? FL.get(0) : FL.get(1);
        }
    }

    public BaseDrawBody getNightDraw(boolean ny) {
        if (ny) {
            return !DateTimeUtil.isDayDrawNew(NY.get(0).getDate()) ? NY.get(0) : NY.get(1);
        } else {
            return !DateTimeUtil.isDayDrawNew(FL.get(0).getDate()) ? FL.get(0) : FL.get(1);
        }
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }


}
