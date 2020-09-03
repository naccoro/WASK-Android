package com.naccoro.wask.receivers;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.naccoro.wask.R;
import com.naccoro.wask.mock.MockDatabase;
import com.naccoro.wask.preferences.AlarmPreferenceManager;
import com.naccoro.wask.utils.AlarmUtil;
import com.naccoro.wask.utils.NotificationUtil;

import static com.naccoro.wask.utils.AlarmUtil.ALERT_TYPE;

public class AlarmReceiver extends BroadcastReceiver {

    /**
     * Alarm이 동작했을 때 작동하는 리시버
     */
    @Override
    public void onReceive(Context context, Intent intent) {

        String type = intent.getStringExtra(ALERT_TYPE);

        if (type != null) {
            MockDatabase.MockNotificationData data;
//            if (type.equals(AlarmUtil.REPLACEMENT_CYCLE_VALUE)) {
//
//                data = MockDatabase.getReplacementCycleData(context);
//            } else {
//
//                data = MockDatabase.getReplaceLaterData(context);
//            }

            //나중에 교체하기 알림은 끝이 난 걸로 저장
            AlarmPreferenceManager.setIsReplacementLater(false);

            //동일한 형상의 Notification
            data = MockDatabase.getReplacementCycleData(context);

            showPushNotification(context, data);
        }
    }

    private void showPushNotification(Context context,  MockDatabase.MockNotificationData data) {
        //1. String channel Id 값을 가져온다.
        String channelId = NotificationUtil.createNotificationChannel(context, data);
        if (channelId == null) {
            return;
        }

        //2. 커스텀한 뷰를 가져온다.
        RemoteViews contentView = new RemoteViews(context.getPackageName(), data.getLayoutId());
        contentView.setImageViewResource(R.id.imageview_notification_logo, R.drawable.ic_notification_logo);
        contentView.setTextViewText(R.id.textview_notification_content, data.getContentText());

        Intent okIntent = new Intent(context, ReplaceMaskReceiver.class);
        okIntent.putExtra("notificationId", data.getNotification_id());
        PendingIntent pendingOkIntent = PendingIntent.getBroadcast(context, 0,
                okIntent, 0);
        contentView.setOnClickPendingIntent(R.id.button_notification_ok, pendingOkIntent);

        Intent xIntent = new Intent(context, ReplaceLaterReceiver.class);
        xIntent.putExtra("notificationId", data.getNotification_id());
        PendingIntent pendingXIntent = PendingIntent.getBroadcast(context, 0,
                xIntent, 0);

        contentView.setOnClickPendingIntent(R.id.button_notification_x, pendingXIntent);


        //3. Notification을 생성한다. Android Orea 이전 버전에서는 channelId는 무시될 것 입니다.
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context, channelId)
                .setCustomContentView(contentView)
                .setPriority(data.getPriority())
                .setSmallIcon(R.drawable.ic_main_logo)
                .setColor(context.getColor(R.color.waskBlue))
                .setVisibility(data.getChannelLockscreenVisibility())
                .setAutoCancel(true);// 사용자가 알람을 탭했을 때, 알람이 사라짐

        if (!data.isChannelEnableSound()) {

            notificationBuilder.setSound(null);
        }
        if (!data.isChannelEnableVibrate()) {

            notificationBuilder.setVibrate(null);
        }

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context.getApplicationContext());

        //4. type 별로 Notification 을 독립적으로 등록해준다.
        notificationManagerCompat.notify(data.getNotification_id(), notificationBuilder.build());
    }
}