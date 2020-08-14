package com.naccoro.wask.utils;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import androidx.core.content.ContextCompat;

import com.naccoro.wask.notification.WaskService;
import com.naccoro.wask.preferences.SettingPreferenceManager;
import com.naccoro.wask.receivers.AlarmReceiver;
import com.naccoro.wask.receivers.ForegroundReceiver;
import com.naccoro.wask.replacement.model.Injection;
import com.naccoro.wask.replacement.repository.ReplacementHistoryRepository;
import com.naccoro.wask.setting.SettingActivity;

import java.util.Calendar;

public class AlarmUtil {
    private static final int REPLACEMENT_CYCLE_START_HOUR = 6;
    private static final int REPLACE_LATER_START_HOUR = 22;

    //동시에 두 AlarmManager 를 등록해야 하기 때문에 고유 requestcode 값을 저장
    private static final int REQUEST_CODE_REPLACEMENT_CYCLE = 100;
    private static final int REQUEST_CODE_REPLACE_LATER = 200;
    private static final int REQUEST_CODE_FOREGROUND = 300;

    public static String ALERT_TYPE = "alert_type";
    public static String REPLACEMENT_CYCLE_VALUE = "replacement_cycle";
    public static String REPLACE_LATER_VALUE = "replace_later";

    /**
     * 교체 주기 Alarm을 등록하는 함수
     */
    public static void setReplacementCycleAlarm(Context context) {

        ReplacementHistoryRepository replacementHistoryRepository = Injection.replacementHistoryRepository(context);

        int replaceDate = replacementHistoryRepository.getLastReplacement();

        //사용자가 선택한 교체하기 period 를 가져온다.
        int period = SettingPreferenceManager.getReplaceCycle();

        //저장되어 있는 교체주기 알람 date가 오늘보다 얼마나 지났는지 체크한다.
        int periodDelay = DateUtils.calculateDateGapWithToday(replaceDate);

        period = period > periodDelay ? period - periodDelay : 0;

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, period);
        calendar.set(Calendar.HOUR_OF_DAY, REPLACEMENT_CYCLE_START_HOUR); //오전 6시 알람

        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.putExtra(ALERT_TYPE, REPLACEMENT_CYCLE_VALUE);
        PendingIntent alarmIntent = PendingIntent.getBroadcast(context, REQUEST_CODE_REPLACEMENT_CYCLE, intent, 0);

        //처음에 Notification 이 작동 후 며칠 단위로 작동할지
        setAlertManager(context, calendar, alarmIntent);
    }


    /**
     * 나중에 교체 주기 Alarm을 등록하는 함수
     */
    public static void setReplacementLaterAlarm(Context context) {

        ReplacementHistoryRepository replacementHistoryRepository = Injection.replacementHistoryRepository(context);

        int replaceDate = replacementHistoryRepository.getLastReplacement();

        //사용자가 선택한 교체하기 period 를 가져온다.
        int period = SettingPreferenceManager.getDelayCycle();

        //저장되어 있는 교체주기 알람 date가 오늘보다 얼마나 지났는지 체크한다.
        int periodDelay = DateUtils.calculateDateGapWithToday(replaceDate);

        period = period > periodDelay ? period - periodDelay : 0;

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, period);
        calendar.set(Calendar.HOUR_OF_DAY, REPLACE_LATER_START_HOUR); //오후 10시 알람

        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.putExtra(ALERT_TYPE, REPLACE_LATER_VALUE);
        PendingIntent alarmIntent = PendingIntent.getBroadcast(context, REQUEST_CODE_REPLACE_LATER, intent, 0);

        //처음에 Notification 이 작동 후 며칠 단위로 작동할지
        setAlertManager(context, calendar, alarmIntent);
    }

    /**
     * os에 등록된 ReplacementCycle AlarmManager 를 종료한다.
     */
    public static void cancelReplacementCycleAlarm(Context context) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.putExtra(ALERT_TYPE, REPLACEMENT_CYCLE_VALUE);
        PendingIntent alarmIntent = PendingIntent.getBroadcast(context, REQUEST_CODE_REPLACEMENT_CYCLE, intent, 0);

        alarmManager.cancel(alarmIntent);
    }

    /**
     * os에 등록된 ReplaceLater AlarmManager 를 종료한다.
     */
    public static void cancelReplaceLaterAlarm(Context context) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.putExtra(ALERT_TYPE, REPLACE_LATER_VALUE);
        PendingIntent alarmIntent = PendingIntent.getBroadcast(context, REQUEST_CODE_REPLACE_LATER, intent, 0);

        alarmManager.cancel(alarmIntent);
    }

    public static void cancelForegroundAlarm(Context context) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(context, ForegroundReceiver.class);
        PendingIntent alarmIntent = PendingIntent.getBroadcast(context, REQUEST_CODE_FOREGROUND, intent, 0);

        alarmManager.cancel(alarmIntent);
    }

    public static void setForegroundAlarm(Context context) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, 1); //다음날
        calendar.set(Calendar.HOUR, 0); //자정으로


        Intent intent = new Intent(context, ForegroundReceiver.class);
        PendingIntent alarmIntent = PendingIntent.getBroadcast(context, REQUEST_CODE_FOREGROUND, intent, 0);

        setAlertManager(context, calendar, alarmIntent);
    }

    /**
     * 실제로 AlertManager 등록하는 함수
     */
    private static void setAlertManager(Context context, Calendar calendar, PendingIntent alarmIntent) {

        //분 초 0으로 셋팅
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

        AlarmManager alarmManager;

        alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);


        //처음 Calendar 날짜에 AlertManager가 작동되고 이후 하루마다 작동됩니다.
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY, alarmIntent);
    }

    public static void showForegroundService(Context context, int maskPeriod) {
        Intent service = new Intent(context, WaskService.class);

        service.putExtra("maskPeriod", maskPeriod);
        ContextCompat.startForegroundService(context, service);

        //Foreground 알람 등록
        setForegroundAlarm(context);
    }

    public static void dismissForegroundService(Context context) {
        Intent service = new Intent(context, WaskService.class);
        context.stopService(service);

        //Foreground 알람 삭제
        cancelForegroundAlarm(context);
    }
}
