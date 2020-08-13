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

            AlarmUtil.setReplacementCycleAlarm(context);

            AlarmUtil.setReplacementCycleAlarm(context);

        }
    }
}