package com.naccoro.wask.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.naccoro.wask.preferences.NotificationPreferenceManager;
import com.naccoro.wask.preferences.SettingPreferenceManager;
import com.naccoro.wask.utils.AlarmUtil;
import com.naccoro.wask.utils.DateUtils;

import java.util.Calendar;

public class ReplaceMaskReceiver extends BroadcastReceiver {


    /**
     * Notification에서 교체하기 버튼을 눌렀을 때 교체하기 기능을 수행한다.
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        //TODO: foreground 사용일자를 변경하는 작업도 할 수 있을 것 같다.

        //혹여나 핸드폰이 종료되어 BootReceiver 가 작동되어도 ReplaceLater Alarm 을 작동되지 않게 하기 위해 null 을 넣는다.
        NotificationPreferenceManager.setReplaceLaterDate(null);

        String todayDate = DateUtils.getStringOfCalendar(Calendar.getInstance());

        //교체하기 Date 를 등록한다. BootReceiver 가 작동되어도 등록한 날짜 기준으로 period 후에 alarm 이 동작하게 만든다.
        NotificationPreferenceManager.setReplacementCycleDate(todayDate);

        AlarmUtil.setReplacementCycleAlarm(context);
    }

}