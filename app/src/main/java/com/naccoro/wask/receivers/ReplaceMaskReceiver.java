package com.naccoro.wask.receivers;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.naccoro.wask.preferences.AlarmPreferenceManager;
import com.naccoro.wask.preferences.SettingPreferenceManager;
import com.naccoro.wask.replacement.model.Injection;
import com.naccoro.wask.replacement.repository.ReplacementHistoryRepository;
import com.naccoro.wask.utils.AlarmUtil;
import com.naccoro.wask.utils.DateUtils;

public class ReplaceMaskReceiver extends BroadcastReceiver {

    private ReplacementHistoryRepository replacementHistoryRepository;
    /**
     * Notification에서 교체하기 버튼을 눌렀을 때 교체하기 기능을 수행한다.
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        //TODO: foreground 사용일자를 변경하는 작업도 할 수 있을 것 같다.

        int notificationId = intent.getIntExtra("notificationId", -1);
        if (notificationId != -1) {
            //알람 종료
            NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            manager.cancel(notificationId);
        }

        replacementHistoryRepository = Injection.replacementHistoryRepository(context);

        insertMaskChangeHistory(context);

    }

    /**
     * Notification으로 사용자가 교체하기를 작동
     */
    private void insertMaskChangeHistory(Context context) {

        replacementHistoryRepository.insertToday(new ReplacementHistoryRepository.InsertHistoryCallback() {
            @Override
            public void onSuccess() {

                //알람을 지운다.
                if (AlarmUtil.isLaterAlarmExist(context)) {
                    AlarmUtil.cancelReplaceLaterAlarm(context);
                    AlarmPreferenceManager.setIsReplacementLater(false);
                }
                if (AlarmUtil.isCycleAlarmExist(context)) {
                    AlarmUtil.cancelReplacementCycleAlarm(context);
                }

                //알람 재등록
                AlarmUtil.setReplacementCycleAlarm(context);

                updateMaskAlarm(context);
            }

            @Override
            public void onDuplicated() {

            }
        });
    }

    /**
     * 사용자가 알림으로 교체일자를 수정하면 영구 노티도 변경해줍니다.
     */
    private void updateMaskAlarm(Context context) {

        if (SettingPreferenceManager.getIsShowNotificationBar()) {
            int period = getMaskPeriod();
            if (period > 0) {

                AlarmUtil.showForegroundService(context, period);

                AlarmUtil.setForegroundAlarm(context);
            } else {
                AlarmUtil.dismissForegroundService(context);

                AlarmUtil.cancelForegroundAlarm(context);
            }
        }
    }

    /**
     * WaskDatabase에서 현재 마스크 교체 상태를 가져오는 함수
     *
     * @return [오늘 날짜 - 마지막 교체 일자 + 1]
     */
    private int getMaskPeriod() {

        int lastReplacement = replacementHistoryRepository.getLastReplacement();

        if (lastReplacement == -1) {
            //교체 기록이 없을 경우
            return 0;
        }

        return DateUtils.calculateDateGapWithToday(lastReplacement) + 1;
    }
}