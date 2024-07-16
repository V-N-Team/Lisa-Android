package ht.lisa.app.model;

import android.text.TextUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static ht.lisa.app.model.response.DrawResponse.SEPARATOR;

import ht.lisa.app.util.TextUtil;

public class FullSingleDraw implements Serializable {

    public static int DRAW_DAY = 0;
    public static int DRAW_EVENING = 1;
    public static int DRAW_NIGHT = 2;

    private String name;
    private String dayDraw;
    private int dayDate;
    private String nightDraw;
    private String eveningDraw;
    private int nightDate;
    private int eveningDate;

    public FullSingleDraw(String name, String dayDraw, int dayDate, String nightDraw, int nightDate, int eveningDate,
                          String eveningDraw) {
        this.name = name;
        this.dayDraw = dayDraw;
        this.dayDate = dayDate;
        this.nightDraw = nightDraw;
        this.nightDate = nightDate;
        this.eveningDate = eveningDate;
        this.eveningDraw = eveningDraw;
    }

    public String getName() {
        return name;
    }

    public String getDayDraw() {
        return dayDraw;
    }

    public int getDayDate() {
        return dayDate;
    }

    public int getEveningDate() {
        return eveningDate;
    }

    public String getNightDraw() {
        return nightDraw;
    }

    public int getNightDate() {
        return nightDate;
    }

    public String[] getDayDrawArray() {
        if (TextUtils.isEmpty(dayDraw))
            return null;
        return dayDraw.split(SEPARATOR);
    }

    public String[] getNightDrawArray() {
        if (TextUtils.isEmpty(dayDraw))
            return null;
        return nightDraw.split(SEPARATOR);
    }

    public String[] getEveningDrawArray() {
        if (TextUtils.isEmpty(eveningDraw))
            return null;
        return eveningDraw.split(SEPARATOR);
    }

    public String getEveningDraw() {
        return eveningDraw;
    }

    public String getNumberAtPos(int pos, int draw) {
        ArrayList<String> numbers = getNumberDrawMariyaj(draw);

        switch (pos) {
            case 0:
                return numbers.get(1) + "-" + numbers.get(2);
            case 1:
                return numbers.get(2) + "-" + numbers.get(1);
            case 2:
                return numbers.get(0).substring(1) + "-" + numbers.get(1);
            case 3:
                return numbers.get(1) + "-" + numbers.get(0).substring(1);
            case 4:
                return numbers.get(0).substring(1) + "-" + numbers.get(2);
            case 5:
                return numbers.get(2) + "-" + numbers.get(0).substring(1);
        }

        return "";
    }

    public String getNumberAtPosMaryaj(int pos, int draw) {
        ArrayList<String> numbers = getNumberDrawMariyaj(draw);
        switch (pos) {
            case 0:
                return buildNumber(numbers.get(1)) + "-" + buildNumber(numbers.get(2));
            case 3:
                return buildNumber(numbers.get(2)) + "-" + buildNumber(numbers.get(1));
            case 1:
                return buildNumber(numbers.get(0)) + "-" + buildNumber(numbers.get(1));
            case 4:
                return buildNumber(numbers.get(1)) + "-" + buildNumber(numbers.get(0));
            case 2:
                return buildNumber(numbers.get(0)) + "-" + buildNumber(numbers.get(2));
            case 5:
                return buildNumber(numbers.get(2)) + "-" + buildNumber(numbers.get(0));
        }
        return "";
    }

    public String getNumberAtPosLotto5Jr(int pos, int draw) {
        ArrayList<String> numbers = getNumberDrawMariyaj(draw);
        switch (pos) {
            case 0:
                return buildNumber(numbers.get(0)) + "-" + buildNumberSimple(numbers.get(1));
            case 1:
                return buildNumber(numbers.get(0)) + "-" + buildNumberSimple(numbers.get(1).substring(0, 1));
            case 2:
                return buildNumber(numbers.get(0));
            case 3:
                return buildNumber(numbers.get(0).substring(0, 2));
        }
        return "";
    }

    public String getNumberAtPosLotto5Royal(int pos, int draw) {
        ArrayList<String> numbers = getNumberDrawMariyaj(draw);
        switch (pos) {
            case 0:
                return buildNumber(numbers.get(0)) + "-" + buildNumberSimple(numbers.get(1));
            case 1:
                return buildNumber(numbers.get(0)) + "-" + buildNumberSimple(numbers.get(1).substring(0, 1));
            case 2:
                return buildNumber(numbers.get(0)).substring(1) + "-" + buildNumberSimple(numbers.get(1));
            case 3:
                return buildNumber(numbers.get(0));
            case 4:
                return buildNumber(numbers.get(0)).substring(2) + "-" + buildNumberSimple(numbers.get(1));
        }
        return "";
    }

    public String getNumberAtPosBolet(int pos, int draw) {
        ArrayList<String> numbers = getNumberDrawMariyaj(draw);
        switch (pos) {
            case 0:
                return "50X : " + buildNumber(numbers.get(0));
            case 1:
                return "20X : " + buildNumber(numbers.get(1));
            case 2:
                return "10X : " + buildNumber(numbers.get(2));
        }
        return "";
    }

    public String getNumberAtPosLotto3(int pos, int draw) {
        ArrayList<String> numbers = getNumberDrawMariyaj(draw);
        switch (pos) {
            case 0:
                return buildNumber(numbers.get(1)) + "-" + buildNumberSimple(numbers.get(2));
            case 3:
                return buildNumber(numbers.get(2)) + "-" + buildNumberSimple(numbers.get(1));
            case 1:
                return buildNumber(numbers.get(0)) + "-" + buildNumberSimple(numbers.get(1));
            case 4:
                return buildNumber(numbers.get(1)) + "-" + buildNumberSimple(numbers.get(0));
            case 2:
                return buildNumber(numbers.get(0)) + "-" + buildNumberSimple(numbers.get(2));
            case 5:
                return buildNumber(numbers.get(2)) + "-" + buildNumberSimple(numbers.get(0));
        }
        return "";
    }

    private String buildNumber(String number) {
        return number.length() > 1 ? number : "0" + number;
    }

    private String buildNumberSimple(String number) {
        return number;
    }


    public ArrayList<String> getNumberDrawMariyaj(int draw) {
        String[] drawValues = (draw == DRAW_DAY ? dayDraw : draw == DRAW_EVENING ? eveningDraw : nightDraw).split("-");
        return new ArrayList<>(Arrays.asList(drawValues));
    }
}
