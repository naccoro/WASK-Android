package com.naccoro.wask.notification;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.RemoteViews;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.naccoro.wask.R;
import com.naccoro.wask.main.MainActivity;
import com.naccoro.wask.mock.MockDatabase;
import com.naccoro.wask.preferences.NotificationPreferenceManager;
import com.naccoro.wask.preferences.SettingPreferenceManager;
import com.naccoro.wask.receivers.ReplaceLaterReceiver;
import com.naccoro.wask.receivers.ReplaceMaskReceiver;
import com.naccoro.wask.utils.AlarmUtil;
import com.naccoro.wask.utils.NotificationUtil;

import static com.naccoro.wask.preferences.SettingPreferenceManager.SettingPushAlertType.ALL;
import static com.naccoro.wask.preferences.SettingPreferenceManager.SettingPushAlertType.NONE;
import static com.naccoro.wask.preferences.SettingPreferenceManager.SettingPushAlertType.SOUND;
import static com.naccoro.wask.preferences.SettingPreferenceManager.SettingPushAlertType.VIBRATION;
import static com.naccoro.wask.preferences.SettingPreferenceManager.SettingPushAlertType.getPushAlertTypeWithIndex;


public class PushNotificationService extends Service {

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    /**
     * @return 서비스가 강제 종료되어도 다시 시작하지 않음
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Log.d("notification", "작동");
        String type = intent.getStringExtra(AlarmUtil.ALERT_TYPE);
        MockDatabase.MockNotificationData data;

        //0. 올리고자 하는 Notification Data를 가져온다.
        if (type == null) {
            //서비스 종료
            stopSelf();
            return START_NOT_STICKY;
        } else if (type.equals(AlarmUtil.REPLACEMENT_CYCLE_VALUE)) {
            data = MockDatabase.getReplacementCycleData(this);
        } else {
            data = MockDatabase.getReplaceLaterData(this);
        }

        //1. String channel Id 값을 가져온다.
        String channelId = NotificationUtil.createNotificationChannel(this, data);

        //2. 커스텀한 뷰를 가져온다.
        RemoteViews contentView = new RemoteViews(getPackageName(), R.layout.notification_replacementcycle);
        contentView.setImageViewResource(R.id.imageview_notification_logo, R.drawable.ic_good);
        contentView.setTextViewText(R.id.textview_notification_name, getString(R.string.app_name));
        contentView.setTextViewText(R.id.textview_notification_content, data.getContentText());

        Intent okIntent = new Intent(this, ReplaceMaskReceiver.class);
        PendingIntent pendingOkIntent = PendingIntent.getBroadcast(this, 0,
                okIntent, 0);
        contentView.setOnClickPendingIntent(R.id.button_notification_ok, pendingOkIntent);

        Intent xIntent = new Intent(this, ReplaceLaterReceiver.class);
        PendingIntent pendingXIntent = PendingIntent.getBroadcast(this, 0,
                xIntent, 0);
        contentView.setOnClickPendingIntent(R.id.button_notification_ok, pendingXIntent);


        //3. 알림이 눌러졌을 때 이동할 Activity를 선언한다.

        Intent notificationIntent = new Intent(this, MainActivity.class);

        //WASK Activity Task를 비우고 새로 생성한다.
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this
                , 0, notificationIntent, 0); //알람을 눌렀을 때 해당 엑티비티로

        //3. Notification을 생성한다. Android Orea 이전 버전에서는 channelId는 무시될 것 입니다.
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, channelId)
                .setCustomContentView(contentView)
                .setContentIntent(pendingIntent)
                .setPriority(data.getPriority())
                .setSmallIcon(R.drawable.ic_main_logo)
                .setVisibility(data.getChannelLockscreenVisibility())
                .setAutoCancel(true);// 사용자가 알람을 탭했을 때, 알람이 사라짐

        if (!data.isChannelEnableSound()) {

            notificationBuilder.setSound(null);
        }
        if (!data.isChannelEnableVibrate()) {

            notificationBuilder.setVibrate(null);
        }


        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(getApplicationContext());

        //4. type 별로 Notification 을 독립적으로 등록해준다.
        notificationManagerCompat.notify(data.getNotification_id(), notificationBuilder.build());

        // startForeground(data.getNotification_id() ,notificationBuilder.build());
        return START_NOT_STICKY;
    }
}
