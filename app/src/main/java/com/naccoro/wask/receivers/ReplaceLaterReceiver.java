package com.naccoro.wask.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

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
        String date = DateUtils.getStringOfCalendar(Calendar.getInstance());

        //나중에 교체하기 알람을 등록한 날짜를 저장
        NotificationPreferenceManager.setReplaceLaterDate(date);

        int period = SettingPreferenceManager.getDelayCycle();

        //나중에 교체하기 알람 등록
        AlarmUtil.setReplacementLaterAlarm(context, period);
    }

}