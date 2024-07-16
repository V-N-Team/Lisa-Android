package ht.lisa.app.util;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Set;

public class Settings {

    public static final String DEVICE_ID = "deviceId";
    public static final String KEY_LANGUAGE = "language";
    public static final String KEY_TOKEN = "keyToken";
    public static final String KEY_SESSIONS = "keySessions";
    public static final String KEY_SESSION_LENGTH = "keySessionLength";
    public static final String KEY_SESSION_DATE = "keySessionDate";
    public static final String KEY_MEDIUM_SESSION_LENGTH = "keyMediumSessionLength";
    public static final String KEY_PHONE = "keyPhone";
    public static final String KEY_NOTIFICATIONS = "keyNotifications";
    public static final String TOKEN_HEADER = "Authorization";
    public static final String TOKEN_MOVILOT = "Movilot";
    public static final String BIOMETRIC_AUTHENTIFICATION = "biometricAuthentication";
    public static final String TEMPORARY_TOKEN = "temporaryToken";
    public static final String READ_MESSAGES = "readMessages";
    public static final String PIN = "pin";
    public static final String SALT = "salt";
    public static final String TICKETS = "tickets";
    public static final String SECRET_WORD = "secretWord";
    public static final String BIND_DEVICE_REQUEST_TIME = "BindDeviceRequestTime";
    public static final String LAST_MESSAGE_ID_SET = "lastMessageIdSet";
    public static final String LAST_MESSAGE_ID = "lastMessageID";
    public static final String CASH_OUT_CODE = "cashOutCode";
    public static final String CASH_OUT_CODE_TIME = "cashOutCodeTime";
    public static final String NOT_NEED_TO_SHOW_BDAY_ALERT = "notNeedToShowBdayAlert";
    public static final String GET_STARTED = "getStarted";
    public static final String DELETED_MESSAGES = "deletedMessages";
    public static final String GAMES_GUIDE_SET = "gamesGuideSeen";
    public static final String NOTIFICATION_ALLOWED = "notificationAllow";
    public static final String LISA_SOUND_NEEDED = "lisaSoundNeeded";
    public static final String NOT_NEED_TO_SHOW_PROFILE_DIALOG = "needToShowProfileDialog";

    private static SharedPreferences sharedPreferences;

    public static void loadSettingsHelper(Context context) {
        sharedPreferences = context.getSharedPreferences("ht.lisa.app", Context.MODE_PRIVATE);
    }

    public static void saveString(String key, String value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public static String loadString(String key) {
        return sharedPreferences.getString(key, null);
    }

    public static void saveBoolean(String key, boolean value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    public static boolean loadBoolean(String key) {
        return sharedPreferences.getBoolean(key, false);
    }

    public static void saveDouble(String key, double value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, String.valueOf(value));
        editor.apply();
    }

    public static double loadDouble(String key) {
        return Double.parseDouble(sharedPreferences.getString(key, "0"));
    }

    public static void saveFloat(String key, float value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putFloat(key, value);
        editor.apply();
    }

    public static float loadFloat(String key) {
        return sharedPreferences.getFloat(key, 0);
    }

    public static void saveInt(String key, int value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(key, value);
        editor.apply();
    }

    public static int loadInt(String key) {
        return sharedPreferences.getInt(key, 0);
    }

    public static void saveLong(String key, long value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong(key, value);
        editor.apply();
    }

    public static long loadLong(String key) {
        return sharedPreferences.getLong(key, 0);
    }

    public static void remove(String keyToken) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(keyToken);
        editor.apply();
    }

    public static void saveStringSet(String key, Set<String> value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(key);
        editor.apply();
        editor.putStringSet(key, value);
        editor.apply();
    }

    public static Set<String> loadStringSet(String key) {
        return sharedPreferences.getStringSet(key, null);
    }
}