package com.naccoro.wask.utils;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.naccoro.wask.notification.PushNotificationService;
import com.naccoro.wask.preferences.SettingPreferenceManager;

import java.util.Calendar;

public class AlarmUtil {
    private static int REPLACEMENT_CYCLE_START_HOUR = 6;
    private static int REPLACE_LATER_START_HOUR = 22;

    public static String ALERT_TYPE = "alert_type";
    public static String REPLACEMENT_CYCLE_VALUE = "replacement_cycle";
    public static String REPLACE_LATER_VALUE = "replace_later";

    /**
     * 교체 주기 Alarm을 등록하는 함수
     *
     * @param periodDelay 며칠 후 Notification이 작동 할지
     */
    public static void setReplacementCycleAlarm(Context context, int periodDelay) {

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, periodDelay);
        calendar.set(Calendar.HOUR_OF_DAY, REPLACEMENT_CYCLE_START_HOUR); //오전 6시 알람

        Intent intent = new Intent(context, PushNotificationService.class);
        intent.putExtra(ALERT_TYPE, REPLACEMENT_CYCLE_VALUE);

        //처음에 Notification 이 작동 후 며칠 단위로 작동할지
        int period = SettingPreferenceManager.getReplaceCycle();
        setAlertManager(context, calendar, intent, period);
    }


    /**
     * 나중에 교체 주기 Alarm을 등록하는 함수
     *
     * @param periodDelay 며칠 후 Notification이 작동 할지
     */
    public static void setReplacementLaterAlarm(Context context, int periodDelay) {

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, periodDelay);
        calendar.set(Calendar.HOUR_OF_DAY, REPLACE_LATER_START_HOUR); //오후 10시 알람

        Intent intent = new Intent(context, PushNotificationService.class);
        intent.putExtra(ALERT_TYPE, REPLACE_LATER_VALUE);

        //처음에 Notification 이 작동 후 며칠 단위로 작동할지
        int period = SettingPreferenceManager.getReplaceCycle();
        setAlertManager(context, calendar, intent, period);
    }

    /**
     * os에 등록된 ReplacementCycle AlarmManager 를 종료한다.
     */
    public static void cancelReplacementCycleAlarm(Context context) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(context, PushNotificationService.class);
        intent.putExtra(ALERT_TYPE, REPLACEMENT_CYCLE_VALUE);
        PendingIntent alarmIntent = PendingIntent.getBroadcast(context, 0, intent, 0);

        alarmManager.cancel(alarmIntent);
    }

    /**
     * os에 등록된 ReplaceLater AlarmManager 를 종료한다.
     */
    public static void cancelReplaceLaterAlarm(Context context) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        ;

        Intent intent = new Intent(context, PushNotificationService.class);
        intent.putExtra(ALERT_TYPE, REPLACE_LATER_VALUE);
        PendingIntent alarmIntent = PendingIntent.getBroadcast(context, 0, intent, 0);

        alarmManager.cancel(alarmIntent);
    }

    /**
     * 실제로 AlertManager 등록하는 함수
     */
    private static void setAlertManager(Context context, Calendar calendar, Intent intent, int period) {

        //분 초 0으로 셋팅
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

        AlarmManager alarmManager;
        PendingIntent alarmIntent;

        alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        alarmIntent = PendingIntent.getBroadcast(context, 0, intent, 0);

        long interval = 1000 * 60 * 60 * 24 * period;

        //처음 Calendar 날짜에 AlertManager가 작동되고 이후 interval 차이를 두며 작동됩니다.
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                interval, alarmIntent);
    }
}
