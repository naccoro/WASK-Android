package com.naccoro.wask.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.content.ContextCompat;

import com.naccoro.wask.notification.WaskService;
import com.naccoro.wask.preferences.AlarmPreferenceManager;
import com.naccoro.wask.preferences.SettingPreferenceManager;
import com.naccoro.wask.replacement.model.Injection;
import com.naccoro.wask.replacement.repository.ReplacementHistoryRepository;
import com.naccoro.wask.setting.SettingActivity;
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

            int period = getMaskPeriod(context);

            //교체일자가 없다면 실행하지 말것
            if (period < 0) {
                return;
            }

            //나중에 교체하기 실행 중이었다면 나중에 교체하기 알람을 재조정
            if (AlarmPreferenceManager.getIsReplacementLater()) {
                AlarmUtil.setReplacementLaterAlarm(context, true);
            } else {
                //일반 교체하기 알람을 실행 중이었다면 알람을 재조정
                AlarmUtil.setReplacementCycleAlarm(context);
            }

            if (SettingPreferenceManager.getIsShowNotificationBar()) {
                AlarmUtil.showForegroundService(context, period);
                AlarmUtil.setForegroundAlarm(context);
            }
        }
    }


    /**
     * 마스크 착용일 설정하는 함수
     * */
    private int getMaskPeriod(Context context) {
        ReplacementHistoryRepository replacementHistoryRepository = Injection.replacementHistoryRepository(context);
        int lastReplacement = replacementHistoryRepository.getLastReplacement();
        if (lastReplacement == -1) {
            //교체 기록이 없을 경우
            return 0;
        }
        return DateUtils.calculateDateGapWithToday(lastReplacement) + 1;
    }
}