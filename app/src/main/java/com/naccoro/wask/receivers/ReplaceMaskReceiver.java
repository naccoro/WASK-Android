package com.naccoro.wask.receivers;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.naccoro.wask.replacement.model.Injection;
import com.naccoro.wask.replacement.repository.ReplacementHistoryRepository;
import com.naccoro.wask.utils.AlarmUtil;
import com.naccoro.wask.utils.DateUtils;

public class ReplaceMaskReceiver extends BroadcastReceiver {


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

        insertMaskChangeHistory(context);

    }

    /**
     * Notification으로 사용자가 교체하기를 작동
     */
    private void insertMaskChangeHistory(Context context) {
        ReplacementHistoryRepository replacementHistoryRepository = Injection.replacementHistoryRepository(context);

        replacementHistoryRepository.insertToday(new ReplacementHistoryRepository.InsertHistoryCallback() {
            @Override
            public void onSuccess() {

                //알람을 지운다.
                AlarmUtil.cancelReplaceLaterAlarm(context);
                AlarmUtil.cancelReplacementCycleAlarm(context);

                //알람 재등록
                AlarmUtil.setReplacementCycleAlarm(context);
            }

            @Override
            public void onDuplicated() {

            }
        });
    }

}