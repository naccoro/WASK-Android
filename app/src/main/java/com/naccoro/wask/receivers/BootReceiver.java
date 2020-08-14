package com.naccoro.wask.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.content.ContextCompat;

import com.naccoro.wask.notification.WaskService;
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

            AlarmUtil.setReplacementCycleAlarm(context);

            AlarmUtil.setReplacementCycleAlarm(context);

            if (SettingPreferenceManager.getIsShowNotificationBar()) {
                int period = getMaskPeriod(context);

                //교체일자가 없다면 실행하지 말것
                if (period > 0) {
                    AlarmUtil.showForegroundService(context, getMaskPeriod(context));

                    AlarmUtil.setForegroundAlarm(context);
                }
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