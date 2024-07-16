package ht.lisa.app.util;

import java.util.Set;

public abstract class SharedPreferencesUtil {

    private static long cashOutCodeTime;

    public static String getDeviceId() {
        return Settings.loadString(Settings.DEVICE_ID);
    }

    public static void setDeviceId(String deviceId) {
        Settings.saveString(Settings.DEVICE_ID, deviceId);
    }

    public static String getLanguage() {
        return Settings.loadString(Settings.KEY_LANGUAGE);
    }

    public static void setLanguage(String language) {
        Settings.saveString(Settings.KEY_LANGUAGE, language);
    }

    public static String getPhone() {
        return Settings.loadString(Settings.KEY_PHONE);
    }

    public static void setPhone(String phone) {
        Settings.saveString(Settings.KEY_PHONE, phone);
    }

    public static String getTickets() {
        return Settings.loadString(Settings.TICKETS);
    }

    public static void setTickets(String tickets) {
        Settings.saveString(Settings.TICKETS, tickets);
    }

    public static String getToken() {
        return Settings.loadString(Settings.KEY_TOKEN);
    }

    public static void setToken(String token) {
        Settings.saveString(Settings.KEY_TOKEN, token);
    }

    public static int getVisits() {
        return Settings.loadInt(Settings.KEY_SESSIONS);
    }

    public static void setVisits(int count) {
        Settings.saveInt(Settings.KEY_SESSIONS, count);

    }

    public static long getLastSessionDuration() {
        return Settings.loadLong(Settings.KEY_SESSION_LENGTH);
    }

    public static void setLastSessionDuration(long count) {
        Settings.saveLong(Settings.KEY_SESSION_LENGTH, count);
    }

    public static boolean isLisaSoundNeeded() {
        return Settings.loadBoolean(Settings.LISA_SOUND_NEEDED);
    }

    public static void setLisaSoundNeeded(boolean needed) {
        Settings.saveBoolean(Settings.LISA_SOUND_NEEDED, needed);
    }

    public static boolean isNotificationsAllowed() {
        return Settings.loadBoolean(Settings.NOTIFICATION_ALLOWED);
    }

    public static void setNotificationsAllowed(boolean allowed) {
        Settings.saveBoolean(Settings.NOTIFICATION_ALLOWED, allowed);
    }

    public static long getSessionsLastDate() {
        return Settings.loadLong(Settings.KEY_SESSION_DATE);
    }

    public static boolean isFirstAppLaunch() {
        return getSessionsLastDate() == 0;
    }

    public static void setSessionsDate(long date) {
        Settings.saveLong(Settings.KEY_SESSION_DATE, date);
    }

    public static boolean isNotNeedToShowProfileDialog() {
        return Settings.loadBoolean(Settings.NOT_NEED_TO_SHOW_PROFILE_DIALOG);
    }

    public static void setNotNeedToShowProfileDialog(boolean needToShowProfileDialog) {
        Settings.saveBoolean(Settings.NOT_NEED_TO_SHOW_PROFILE_DIALOG, needToShowProfileDialog);
    }


    public static boolean isNotNeedToShowBdayAlert() {
        return Settings.loadBoolean(Settings.NOT_NEED_TO_SHOW_BDAY_ALERT);
    }

    public static void setNotNeedToShowBdayAlert(boolean needToShowBdayAlert) {
        Settings.saveBoolean(Settings.NOT_NEED_TO_SHOW_BDAY_ALERT, needToShowBdayAlert);
    }

    public static void clearToken() {
        Settings.remove(Settings.KEY_TOKEN);
    }

    public static boolean isAuthorized() {
        return getToken() != null;
    }

    public static String getSecretWord() {
        return Settings.loadString(Settings.SECRET_WORD);
    }

    public static void setSecretWord(String secretWord) {
        Settings.saveString(Settings.SECRET_WORD, secretWord);
    }

    public static void setBiometricAuthentication(boolean b) {
        Settings.saveBoolean(Settings.BIOMETRIC_AUTHENTIFICATION, b);
    }

    public static boolean isBiometricAuthenticated() {
        return Settings.loadBoolean(Settings.BIOMETRIC_AUTHENTIFICATION);
    }

    public static String getTemporaryToken() {
        return Settings.loadString(Settings.TEMPORARY_TOKEN);
    }

    public static void setTemporaryToken(String token) {
        Settings.saveString(Settings.TEMPORARY_TOKEN, token);
    }

    public static String getSalt() {
        return Settings.loadString(Settings.SALT);
    }

    public static void setSalt(String salt) {
        Settings.saveString(Settings.SALT, salt);
    }

    public static long getBindDeviceRequestTime() {
        return Settings.loadLong(Settings.BIND_DEVICE_REQUEST_TIME);
    }

    public static void setBindDeviceRequestTime(long currentTime) {
        Settings.saveLong(Settings.BIND_DEVICE_REQUEST_TIME, currentTime);
    }

    public static String getCashOutCode() {
        return Settings.loadString(Settings.CASH_OUT_CODE);
    }

    public static void setCashOutCode(String cashOutCode) {
        Settings.saveString(Settings.CASH_OUT_CODE, cashOutCode);
    }

    public static Set<String> getLastMsgIds() {
        return Settings.loadStringSet(Settings.LAST_MESSAGE_ID_SET);
    }

    public static String getLastMsgId() {
        return Settings.loadString(Settings.LAST_MESSAGE_ID);
    }

    public static void setLastMsgId(String msgId) {
      /*  Set<String> msgIds = SharedPreferencesUtil.getLastMsgIds() == null ? new HashSet<>() : SharedPreferencesUtil.getLastMsgIds();
        msgIds.add(msgId);*/
        Settings.saveString(Settings.LAST_MESSAGE_ID, msgId);
        //    Settings.saveStringSet(Settings.LAST_MESSAGE_ID_SET, msgIds);
    }

    public static long getCashOutCodeTime() {
        return Settings.loadLong(Settings.CASH_OUT_CODE_TIME);
    }

    public static void setCashOutCodeTime(long cashOutCodeTime) {
        Settings.saveLong(Settings.CASH_OUT_CODE_TIME, cashOutCodeTime);
    }

    public static void setGetStarted(boolean isGetStarted) {
        Settings.saveBoolean(Settings.GET_STARTED, isGetStarted);
    }

    public static boolean isGetStarted() {
        return Settings.loadBoolean(Settings.GET_STARTED);
    }

    public static Set<String> getGameGuideSet() {
        return Settings.loadStringSet(Settings.GAMES_GUIDE_SET);
    }

    public static void setGameGuideSet(Set<String> gameGuideSet) {
        Settings.saveStringSet(Settings.GAMES_GUIDE_SET, gameGuideSet);
    }


    public static Set<String> getReadMessages() {
        return Settings.loadStringSet(Settings.READ_MESSAGES);
    }

    public static void setReadMessages(Set<String> messagesIdSet) {
        Settings.saveStringSet(Settings.READ_MESSAGES, messagesIdSet);
    }

    public static Set<String> getDeletedMessages() {
        return Settings.loadStringSet(Settings.DELETED_MESSAGES);
    }

    public static void setDeletedMessages(Set<String> messagesIdSet) {
        Settings.saveStringSet(Settings.DELETED_MESSAGES, messagesIdSet);
    }

    public static boolean existUnreadMessage(Set<String> messageIdSet) {
        if (messageIdSet != null && !messageIdSet.isEmpty() && getReadMessages() == null) {
            return true;
        }
        if (messageIdSet != null && !messageIdSet.isEmpty()) {
            for (String messageId : messageIdSet) {
                if (!getReadMessages().contains(messageId)) {
                    return true;
                }
            }
        }
        return false;
    }
}
