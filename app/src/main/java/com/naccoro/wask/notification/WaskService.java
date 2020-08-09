package com.naccoro.wask.notification;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.naccoro.wask.R;
import com.naccoro.wask.WaskApplication;
import com.naccoro.wask.main.MainActivity;

public class WaskService extends Service {
    private final String WASK_CHANNEL_ID = WaskApplication.CHANNEL_ID;
    private int count = 0;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    /**
     * foregroundService 실행 메서드
     * @return onStartCommand 메서드 종료 시 서비스 재 실행하지 않음
     * */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        startForegroundService(); // foregroundService (notification) 생성

        return START_NOT_STICKY;
    }

    private void startForegroundService() {
        Intent notificationIntent = new Intent(this, MainActivity.class);
        notificationIntent.putExtra("notiCountTest", count); // 전달할 테스트 값
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, WASK_CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_main_logo)
                .setContentTitle("Wask Notification")
                .setContentText("Test")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        startForeground(1, builder.build());
    }
}
