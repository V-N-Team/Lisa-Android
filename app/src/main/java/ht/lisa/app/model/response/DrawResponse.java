package ht.lisa.app.model.response;

import android.content.Context;

import com.google.gson.annotations.SerializedName;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import ht.lisa.app.model.Draw;
import ht.lisa.app.model.FullSingleDraw;
import ht.lisa.app.util.DateTimeUtil;

public class DrawResponse extends BaseResponse {

    public final static int FIRST_DRAW = 0;
    public final static int SECOND_DRAW = 1;
    public final static String SEPARATOR = "-";
    private static final String DRAW_NOT_FOUND = "draw is not found";
    public static final int NIGHT = 23;
    public static final int EVENING = 20;
    public static final int DAY = 14;

    @SerializedName("dataset")
    List<Draw> drawList;

    public String getErrorMessage() {
        String errorMessage;
        switch (getState()) {
            case -1:
                errorMessage = DRAW_NOT_FOUND;
                break;

            default:
                errorMessage = getBaseErrorMessage();
                break;

        }
        return errorMessage;
    }

    public int getDayDraw(Context context) {
        for (int i = 0; i < drawList.size(); i++) {
            if (isDayDraw(drawList.get(i).getDraw(), context)) {
                return i;
            }
        }
        return 0;
       /* if (drawList.size() == 0) {
            return 0;
        }
        return isDayDraw(drawList.get(0).getDraw(), context) ? FIRST_DRAW : SECOND_DRAW;*/
    }

    public int getNightDraw(Context context) {
        /*if (drawList.size() == 0) {
            return 1;
        }
        return isDayDraw(drawList.get(1).getDraw(), context) ? FIRST_DRAW :
                SECOND_DRAW;*/
        for (int i = 0; i < drawList.size(); i++) {
            if (isNightDraw(drawList.get(i).getDraw(), context)) {
                return i;
            }
        }
        return 0;
    }

    public int getEveningDraw(Context context) {
        /*if (drawList.size() == 0) {
            return 1;
        }
        return isDayDraw(drawList.get(1).getDraw(), context) ? FIRST_DRAW :
                SECOND_DRAW;*/
        for (int i = 0; i < drawList.size(); i++) {
            if (isEveningDraw(drawList.get(i).getDraw(), context)) {
                return i;
            }
        }
        return 0;
    }

    public void setDrawList(List<Draw> drawList) {
        this.drawList = drawList;
    }

    public List<Draw> getDrawList() {
        return drawList;
    }

    public String[] getDrawArray(int draw) {
        return drawList.get(draw).getNum().split(SEPARATOR);
    }

    public ArrayList<FullSingleDraw> getFullSingleDrawList(Context context) {
        ArrayList<FullSingleDraw> fullSingleDraws = new ArrayList<>();
        for (String name : Draw.getDrawResponseNameList()) {
            fullSingleDraws.add(new FullSingleDraw(name, getDrawValueByName(getDayDraw(context), name), getDrawList().get(getDayDraw(context)).getDraw(), getDrawValueByName(getNightDraw(context), name), getDrawList().get(getNightDraw(context)).getDraw(),
                    getDrawList().get(getDayDraw(context)).getDraw(), getDrawValueByName(getEveningDraw(context), name)));
        }
        return fullSingleDraws;
    }


    private String getDrawValueByName(int dateDraw, String name) {
        Field field = null;
        try {
            field = getDrawList().get(dateDraw).getClass().getDeclaredField(name);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        if (field != null) {
            field.setAccessible(true);
        }
        String drawValue = null;
        try {
            if (field != null) {
                drawValue = (String) field.get(getDrawList().get(dateDraw));
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return drawValue;
    }

    private boolean isDayDraw(long time, Context context) {
        return Integer.parseInt(DateTimeUtil.getTicketDateFormatTime(time, DateTimeUtil.HR, context)) <= DAY;
    }

    private boolean isEveningDraw(long time, Context context) {
        int hour = Integer.parseInt(DateTimeUtil.getTicketDateFormatTime(time, DateTimeUtil.HR, context));
        return hour <= EVENING && hour > DAY;
    }

    private boolean isNightDraw(long time, Context context) {
        int hour = Integer.parseInt(DateTimeUtil.getTicketDateFormatTime(time, DateTimeUtil.HR, context));
        return hour > EVENING && hour <= NIGHT;
    }

}