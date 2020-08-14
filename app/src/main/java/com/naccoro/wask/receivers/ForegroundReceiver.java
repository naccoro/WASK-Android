package com.naccoro.wask.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.naccoro.wask.preferences.SettingPreferenceManager;
import com.naccoro.wask.replacement.model.Injection;
import com.naccoro.wask.replacement.repository.ReplacementHistoryRepository;
import com.naccoro.wask.utils.AlarmUtil;
import com.naccoro.wask.utils.DateUtils;


public class ForegroundReceiver extends BroadcastReceiver {

    /**
     * Alarm이 동작했을 때 작동하는 리시버
     */
    @Override
    public void onReceive(Context context, Intent intent) {

        if (SettingPreferenceManager.getIsShowNotificationBar()) {
            int period = getMaskPeriod(context);
            
            //교체일자가 없다면 실행하지 말것
            if (period > 0) {
                AlarmUtil.dismissForegroundService(context);
                AlarmUtil.showForegroundService(context, getMaskPeriod(context));
            } else {
                AlarmUtil.cancelForegroundAlarm(context);
            }
        }
    }


    /**
     * 마스크 착용일 설정하는 함수
     */
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
