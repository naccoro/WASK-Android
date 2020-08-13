package com.naccoro.wask.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.naccoro.wask.mock.MockDatabase;
import com.naccoro.wask.notification.PushNotificationService;
import com.naccoro.wask.utils.AlarmUtil;

import static com.naccoro.wask.utils.AlarmUtil.ALERT_TYPE;

public class AlarmReceiver extends BroadcastReceiver {


    /**
     * Alarm이 동작했을 때 작동하는 리시버
     */
    @Override
    public void onReceive(Context context, Intent intent) {

        String type = intent.getStringExtra(ALERT_TYPE);

        if (type != null) {
            Intent notificationIntent = new Intent(context, PushNotificationService.class);
            notificationIntent.putExtra(ALERT_TYPE, type);

            context.startService(notificationIntent);
        }
    }

}