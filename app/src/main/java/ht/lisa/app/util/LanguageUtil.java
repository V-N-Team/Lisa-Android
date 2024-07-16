package ht.lisa.app.util;

import android.app.Activity;
import android.content.res.Configuration;

import java.util.Locale;

public class LanguageUtil {

    public static void initAppLanguage(Activity activity) {
        String lang = SharedPreferencesUtil.getLanguage() == null ? Constants.LANGUAGE_EN : SharedPreferencesUtil.getLanguage();
        Locale defaultLocale = new Locale(lang);
        Locale.setDefault(defaultLocale);
        Configuration config = activity.getBaseContext().getResources().getConfiguration();
        config.locale = defaultLocale;
        activity.getBaseContext().getResources().updateConfiguration(config,
                activity.getBaseContext().getResources().getDisplayMetrics());
    }
}
