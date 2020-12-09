package com.naccoro.wask.notification;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;

import androidx.core.content.ContextCompat;

import com.naccoro.wask.preferences.SettingPreferenceManager;
import com.naccoro.wask.utils.AlarmUtil;

public class ServiceUtil {
    public static void showForegroundService(Context context, int maskPeriod) {
        Intent service = new Intent(context, WaskService.class);

        service.putExtra("maskPeriod", maskPeriod);
        ContextCompat.startForegroundService(context, service);
    }

    public static void dismissForegroundService(Context context) {
        Intent service = new Intent(context, WaskService.class);
        context.stopService(service);

        //Foreground 알람 삭제
        AlarmUtil.cancelForegroundAlarm(context);
    }

    public static void initForegroundService(Context context, int maskPeriod) {
        if (maskPeriod < 1) {
            return;
        }
        if (SettingPreferenceManager.getIsShowNotificationBar() && !isServiceRunning(context)) {
            showForegroundService(context, maskPeriod);
        }
    }

    private static boolean isServiceRunning(Context context) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Activity.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (service.service.getClassName().equals(WaskService.class.getName())) {
                return true;
            }
        }
        return false;
    }
}
