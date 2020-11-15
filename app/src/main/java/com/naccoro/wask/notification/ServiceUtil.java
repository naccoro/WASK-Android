package com.naccoro.wask.notification;

import android.content.Context;
import android.content.Intent;

import androidx.core.content.ContextCompat;

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
}
