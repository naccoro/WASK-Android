package com.naccoro.wask.utils;

import android.content.Context;
import android.content.res.Configuration;

import java.util.Locale;

public class LanguageUtil {

    public static void changeLocale(Context context, String localeIdentifier) {
        Locale locale;

        if (localeIdentifier.isEmpty()) {
            locale = Locale.getDefault();
        } else {
            locale = new Locale(localeIdentifier);
        }

        Locale.setDefault(locale);
        Configuration configuration = context.getResources().getConfiguration();
        configuration.locale = locale;
        context.getResources().updateConfiguration(configuration, context.getResources().getDisplayMetrics());
    }
}
