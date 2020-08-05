package com.naccoro.wask.utils;

import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;

public class MetricsUtil {
    public static float convertDpToPixel(float dp, Context context) {
        return dp * ((float) getDisplayMetrics(context).densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    }

    public static float convertPixelsToDp(float px, Context context) {
        return px / ((float) getDisplayMetrics(context).densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    }

    private static DisplayMetrics getDisplayMetrics(Context context) {
        DisplayMetrics metrics;
        if (context != null) {
            metrics = context.getResources().getDisplayMetrics();
        } else {
            metrics = Resources.getSystem().getDisplayMetrics();
        }
        return metrics;
    }
}