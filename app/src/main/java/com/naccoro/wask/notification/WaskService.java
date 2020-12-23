package com.naccoro.wask.notification;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.widget.RemoteViews;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.naccoro.wask.R;
import com.naccoro.wask.WaskApplication;
import com.naccoro.wask.preferences.SettingPreferenceManager;
import com.naccoro.wask.ui.main.MainActivity;

public class WaskService extends Service {
    private final String CHANNEL_ID = WaskApplication.CHANNEL_ID;
    private int maskPeriod;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    /**
     * foregroundService 실행 메서드
     *
     * @return onStartCommand 메서드 종료 시 서비스 재 실행하지 않음 (START_NOT_STICKY Option)
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        maskPeriod = intent.getIntExtra("maskPeriod", 1);
        startForegroundService();

        return START_NOT_STICKY;
    }

    /**
     * foregroundService ( notification ) 선언부
     * TODO : foregroundService 기타 기능 추가 ex) 마스크 교체하기 기능 등
     */
    private void startForegroundService() {
        Intent notificationIntent = new Intent(this, MainActivity.class);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        // 노티피케이션을 터치했을 때 이동하는 액티비티 설정
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);


        //2. 커스텀한 뷰를 가져온다.
        RemoteViews contentView = new RemoteViews(getPackageName(), R.layout.notification_foreground);
        contentView.setImageViewResource(R.id.imageview_notification_logo, R.drawable.ic_notification_logo);
        String content;
        //Todo: Service 내에서는 getString이 설정된 Locale에 따라가지 않고 디바이스 설정을 무조건 따라감.
        //Todo: 우선 배포를 위해 아래와 같이 언어 설정 값에 따라 명시적으로 문자열을 강제 변경하였으나 개선이 필요.
        switch (SettingPreferenceManager.getLanguage()) {
            default: //Default
                if (maskPeriod == 1) {
                    content = String.format(getString(R.string.notification_alert_singular), maskPeriod);
                } else {
                    content = String.format(getString(R.string.notification_alert_plural), maskPeriod);
                }
                break;
            case 1: //Korean
                content = String.format(getString(R.string.notification_alert_singular_kr), maskPeriod);
                break;

            case 2: //English
                if (maskPeriod == 1) {
                    content = String.format(getString(R.string.notification_alert_singular_en), maskPeriod);
                } else {
                    content = String.format(getString(R.string.notification_alert_plural_en), maskPeriod);
                }
                break;
        }
        contentView.setTextViewText(R.id.textview_notification_content, content);

        // 노티피케이션 관련 변수
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notification_logo)
                .setColor(this.getColor(R.color.waskBlue))
                .setCustomContentView(contentView)
                .setShowWhen(false)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        startForeground(1, builder.build());
    }
}
