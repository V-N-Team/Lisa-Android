package ht.lisa.app.model.response;

import java.io.Serializable;

import static ht.lisa.app.model.response.DrawResponse.SEPARATOR;

public class BaseDrawBody implements Serializable {
    private long unix;
    private String date;
    private String num;

    public String getDate() {
        return date;
    }

    public long getUnix() {
        return unix;
    }

    public String getNum() {
        return num;
    }


    public String[] getDrawArray() {
        return num.split(SEPARATOR);
    }
}
