package com.naccoro.wask.receivers;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.naccoro.wask.preferences.NotificationPreferenceManager;
import com.naccoro.wask.preferences.SettingPreferenceManager;
import com.naccoro.wask.utils.AlarmUtil;
import com.naccoro.wask.utils.DateUtils;

import java.util.Calendar;

public class ReplaceLaterReceiver extends BroadcastReceiver {


    /**
     * Notification에서 나중에 교체하기 버튼을 눌렀을 때, 나중에 교체하기 Alarm을 작동 시킨다.
     */
    @Override
    public void onReceive(Context context, Intent intent) {

        int notificationId = intent.getIntExtra("notificationId",  -1);
        if (notificationId != -1) {
            //알람 종료
            NotificationManager manager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
            manager.cancel(notificationId);
        }

        String todayDate = DateUtils.getStringOfCalendar(Calendar.getInstance());
        //나중에 교체하기 Date 를 등록한다. BootReceiver 가 작동되어도 등록한 날짜 기준으로 period 후에 alarm 이 동작하게 만든다.
        NotificationPreferenceManager.setReplaceLaterDate(todayDate);

        //나중에 교체하기 알람 등록
        AlarmUtil.setReplacementLaterAlarm(context);
    }

}