package com.naccoro.wask.utils;

import android.content.Context;
import android.content.res.Configuration;

import com.naccoro.wask.R;
import com.naccoro.wask.preferences.SettingPreferenceManager;
import com.naccoro.wask.ui.splash.SplashActivity;

import java.util.Locale;

public class LanguageUtil {

    public static void initLanguage(Context context) {
        changeLocale(context, SettingPreferenceManager.getLanguage());
    }

    public static String getLanguageString(Context context, int languageIndex) {
        return context.getResources().getStringArray(R.array.LANGUASE)[languageIndex];
    }

    private static void changeLocale(Context context, int languageIndex) {
        Locale locale;
        String localeIdentifier = getLanguageIdentifier(context, languageIndex);

        if (localeIdentifier.isEmpty()) {
            locale = Locale.getDefault();
        } else {
            locale = new Locale(localeIdentifier);
        }

        Locale.setDefault(locale);
        Configuration configuration = new Configuration();
        configuration.locale = locale;
        context.getResources().updateConfiguration(configuration, context.getResources().getDisplayMetrics());
    }

    private static String getLanguageIdentifier(Context context, int languageIndex) {
        return context.getResources().getStringArray(R.array.LANGUASE_IDENTIFIER)[languageIndex];
    }
}
