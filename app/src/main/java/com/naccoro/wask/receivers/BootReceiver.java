package com.naccoro.wask.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.naccoro.wask.preferences.NotificationPreferenceManager;
import com.naccoro.wask.preferences.SettingPreferenceManager;
import com.naccoro.wask.utils.AlarmUtil;
import com.naccoro.wask.utils.DateUtils;

public class BootReceiver extends BroadcastReceiver {


    /**
     * BOOT_COMPLETED는 사용자의 핸드폰이 종료되고 다시 켜졌을 때 호출되는 Receiver 이다.
     * 핸드폰이 종료되면 등록된 모든 Alarm이 종료되기 때문에 다시 Alarm을 등록해준다.
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            // Set the alarm here.
            //TODO: 처음 Notification이 등록한 날짜에서 period를 더한 날짜에 Alert가 등록되어야 함
            String replacementCycleDate = NotificationPreferenceManager.getReplacementCycleDate();
            String replaceLaterDate = NotificationPreferenceManager.getReplacementCycleDate();

            if (replacementCycleDate != null) {
                setReplacementCycleAlarm(context, replacementCycleDate);
            }

            if (replaceLaterDate != null) {
                setReplaceLaterAlarm(context, replaceLaterDate);
            }
        }
    }

    private void setReplacementCycleAlarm(Context context, String date) {
        //사용자가 선택한 교체하기 period 를 가져온다.
        int period = SettingPreferenceManager.getDelayCycle();

        //저장되어 있는 교체주기 알람 date가 오늘보다 얼마나 지났는지 체크한다.
        int periodDelay = period + DateUtils.getDelayDay(date);

        AlarmUtil.setReplacementCycleAlert(context, periodDelay);
    }

    private void setReplaceLaterAlarm(Context context, String date) {
        //사용자가 선택한 나중에 교체하기 period 를 가져온다.
        int period = SettingPreferenceManager.getDelayCycle();

        //저장되어 있는 나중에 교체주기 알람 date가 오늘보다 얼마나 지났는지 체크한다.
        int periodDelay = period + DateUtils.getDelayDay(date);

        AlarmUtil.setReplacementCycleAlert(context, periodDelay);
    }
}