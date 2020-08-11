package com.naccoro.wask.notification;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.naccoro.wask.R;
import com.naccoro.wask.WaskApplication;
import com.naccoro.wask.main.MainActivity;
import com.naccoro.wask.main.MainPresenter;

public class WaskService extends Service {
    private final String CHANNEL_ID = WaskApplication.CHANNEL_ID;
    private MainPresenter mainPresenter;
    private int maskPeriod = mainPresenter.getMaskPeriod();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    /**
     * foregroundService 실행 메서드
     * @return onStartCommand 메서드 종료 시 서비스 재 실행하지 않음 (START_NOT_STICKY Option)
     * */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        startForegroundService();

        return START_NOT_STICKY;
    }
    /**
     * foregroundService ( notification ) 선언부
     * TODO : foregroundService 기타 기능 추가 ex) 마스크 교체하기 기능 등
     * */
    private void startForegroundService() {
        Intent notificationIntent = new Intent(this, MainActivity.class);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        // 노티피케이션을 터치했을 때 이동하는 액티비티 설정
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        // 노티피케이션 관련 변수
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_main_logo) // OREO 이상에서 mipmap 사용시 시스템 UI 에러납니다.
                .setContentTitle(String.format("마스크를 %d일 째 사용중입니다.", maskPeriod))
                .setContentText("WASK 열기")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        startForeground(1, builder.build());
    }
}
