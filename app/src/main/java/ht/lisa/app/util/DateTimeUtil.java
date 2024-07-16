package ht.lisa.app.util;

import static ht.lisa.app.model.response.DrawResponse.DAY;
import static ht.lisa.app.model.response.DrawResponse.NIGHT;

import android.annotation.SuppressLint;
import android.content.Context;

import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import ht.lisa.app.LisaApp;

public class DateTimeUtil {

    public static final int SECOND = 1000;
    public static final String HR = "HH";
    public static final String HR_AM_PM = "hh";
    public static final String MIN = "mm";
    public static final String SEC = "ss";

    private static final String TRANSACTION_DATE_FORMAT = "HH:mm / dd.MM.yyy";
    private static final String SIMPLE_DATE_FORMAT = "mm:ss";
    private static final String WINNING_NUMBERS_DATE_FORMAT = "EEEE, dd MMMM, yyyy";
    private static final String WINNING_NUMBERS_DATE_FORMAT_EN = "EEEE, MMMM dd, yyyy";
    private static final String GAME_LIST_DATE_FORMAT = "dd MMMM, yyyy";
    private static final String GAME_LIST_DATE_FORMAT_EN = "MMMM dd, yyyy";
    private static final String DOB_FORMAT = "yyyyMMdd";
    private static final int EVENING = 16;

    public static String getFormattedTime(long milliseconds, String format) {
        long formattedTime = 0;
        switch (format) {
            case HR:
                formattedTime = TimeUnit.MILLISECONDS.toHours(milliseconds);
                break;
            case MIN:
                formattedTime = TimeUnit.MILLISECONDS.toMinutes(milliseconds) % TimeUnit.HOURS.toMinutes(1);
                break;
            case SEC:
                formattedTime = TimeUnit.MILLISECONDS.toSeconds(milliseconds) % TimeUnit.MINUTES.toSeconds(1);
                break;
            default:
                formattedTime = milliseconds;
                break;
        }
        return String.format(Locale.getDefault(), "%02d", formattedTime);
    }


    public static String getSimpleDateFormatTime(long milliseconds, Context context) {
        return getDateFromMilliseconds(milliseconds, SIMPLE_DATE_FORMAT, context);
    }

    public static String getTicketDateFormatTime(long milliseconds, String format, Context context) {
        return getDateFromMilliseconds(milliseconds * SECOND, format, context);
    }

    public static String getDobFormat(long milliseconds) {
        return new SimpleDateFormat(DOB_FORMAT, Locale.getDefault()).format(milliseconds);
    }

    public static String getWinningNumbersDateFormat(long milliseconds, Context context) {
        return TextUtil.toCamelCase(getDateFromMilliseconds(milliseconds * SECOND, LisaApp.getInstance().isFrench(context) || LisaApp.getInstance().isKreyol(context) ? WINNING_NUMBERS_DATE_FORMAT : WINNING_NUMBERS_DATE_FORMAT_EN, context));
    }

    public static String getGameListDateFormat(long milliseconds, Context context) {
        //return TextUtil.toCamelCase(getDateFromMilliseconds(milliseconds * SECOND, LisaApp.getInstance().isFrench(context) || LisaApp.getInstance().isKreyol(context) ? GAME_LIST_DATE_FORMAT : GAME_LIST_DATE_FORMAT_EN, context));
        return TextUtil.toCamelCase(getFormattedFixedDateFromMilliseconds(milliseconds * SECOND, LisaApp.getInstance().isFrench(context) || LisaApp.getInstance().isKreyol(context) ? GAME_LIST_DATE_FORMAT : GAME_LIST_DATE_FORMAT_EN, context));
    }

    public static String getTransactionDateFormat(String milliseconds, Context context) {
        return getDateFromMilliseconds(Long.parseLong(milliseconds) * SECOND, TRANSACTION_DATE_FORMAT, context);
    }

    /*public static boolean isDayDraw(long time, Context context) {
        return Integer.parseInt(DateTimeUtil.getTicketDateFormatTime(time, DateTimeUtil.HR, context)) <= EVENING;
    }*/

    @SuppressLint("SimpleDateFormat")
    public static boolean isDayDrawNew(String date) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy hh:mm a");
        Date d = new Date();
        try {
            d = sdf.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar c = Calendar.getInstance();
        c.setTime(d);
        return c.get(Calendar.HOUR_OF_DAY) <= EVENING;
    }

    public static long drawDateToTime(String date) {
        try {
            Date d = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss").parse(date);
            return d.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }


    public static boolean isDayDraw(String date, Context context) {
        /*try {
            Date d = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss").parse(date);
            int hour = d.getHours();
            return hour <= EVENING;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;*/
        return isDayDraw(drawDateToTime(date), context);
    }

    public static boolean isEveningDraw(String date, Context context) {
        //return !isDayDraw(date);
        return isEveningDraw(drawDateToTime(date), context);
    }

    public static boolean isNightDraw(String date, Context context) {
        //return !isDayDraw(date);
        return isNightDraw(drawDateToTime(date), context);
    }

    public static boolean isDayDraw(long time, Context context) {
        return Integer.parseInt(DateTimeUtil.getTicketDateFormatTime(time, DateTimeUtil.HR, context)) <= DAY;
    }

    public static boolean isEveningDraw(long time, Context context) {
        int hour = Integer.parseInt(DateTimeUtil.getTicketDateFormatTime(time, DateTimeUtil.HR, context));
        return hour <= EVENING && hour > DAY;
    }

    public static boolean isNightDraw(long time, Context context) {
        int hour = Integer.parseInt(DateTimeUtil.getTicketDateFormatTime(time, DateTimeUtil.HR, context));
        return hour > EVENING && hour <= NIGHT;
    }

    public static String getFormattedFixedDateFromMilliseconds(long milliseconds, String dateFormat, Context context) {
        Date date = new Date(milliseconds);
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        String[] months;
        if (LisaApp.getInstance().isKreyol(context)) {
            months = new String[]{"Janvye", "Fevriye", "Mas", "Avril", "Me",
                    "Jen", "Jiyè", "Out", "Septanm", "Oktòb", "Novanm", "Desanm"};
        } else if (LisaApp.getInstance().isFrench(context)) {
            months = new String[]{"Janvier", "Février", "Mars", "Avril", "Mai",
                    "Juin", "Juillet", "Août", "Septembre", "Octobre", "Novembre", "Décembre"};
        } else {
            months = new String[]{"January", "February", "March", "April", "May",
                    "June", "July", "August", "September", "October", "November", "December"};
        }
        String formattedDate;
        if (LisaApp.getInstance().isFrench(context) || LisaApp.getInstance().isKreyol(context)) {
            formattedDate = String.format(Locale.getDefault(), "%02d %s, %d", c.get(Calendar.DAY_OF_MONTH), months[c.get(Calendar.MONTH)], c.get(Calendar.YEAR));
        } else {
            formattedDate = String.format(Locale.getDefault(), "%s %02d, %d", months[c.get(Calendar.MONTH)], c.get(Calendar.DAY_OF_MONTH), c.get(Calendar.YEAR));
        }
        return formattedDate;
    }

    private static String getDateFromMilliseconds(long milliseconds, String dateFormat, Context context) {
        SimpleDateFormat dateFormatter = new SimpleDateFormat(dateFormat, new Locale(LisaApp.getInstance().getLanguage(context)));
        dateFormatter.setTimeZone(TimeZone.getTimeZone("GMT-4"));

        if (LisaApp.getInstance().isKreyol(context)) {
            Locale htLocale = new Locale(Constants.LANGUAGE_KR);
            String[] haitianMonths = {"Janvye", "Fevriye", "Mas", "Avril", "Me",
                    "Jen", "Jiyè", "Out", "Septanm", "Oktòb", "Novanm", "Desanm"};

            String[] haitianDaysOfWeek = {"", "Dimanch", "Lendi", "Madi", "Mèkredi", "Jedi", "Vandredi", "Samdi"};
            DateFormatSymbols dfs = DateFormatSymbols.getInstance(htLocale);
            dfs.setMonths(haitianMonths);
            dfs.setWeekdays(haitianDaysOfWeek);
            dateFormatter.setDateFormatSymbols(dfs);
        }
        Date date = new Date(milliseconds);
        return dateFormatter.format(date);
    }

    public static long getCurrentTime() {
        return System.currentTimeMillis();
    }

}
