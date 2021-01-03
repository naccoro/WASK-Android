package com.naccoro.wask;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

import com.naccoro.wask.mock.MockDatabase;
import com.naccoro.wask.preferences.SettingPreferenceManager;
import com.naccoro.wask.preferences.SharedPreferenceManager;
import com.naccoro.wask.utils.NotificationUtil;

import static com.naccoro.wask.preferences.SettingPreferenceManager.SettingPushAlertType.getPushAlertTypeWithIndex;

public class WaskApplication extends Application {

    public static final String CHANNEL_ID = "WaskChannel";

    public static boolean isChanged;

    private static Application application;

    @Override
    public void onCreate() {
        super.onCreate();

        init();

        createPushNotificationChannel();

        createNotificationChannel();
    }

    private void init() {
        SharedPreferenceManager.getInstance().initInstance(getApplicationContext());
        application = this;
    }

    /**
      * OREO API 26 이상에서는 채널이 필수
     * notification 설정 값
     * */
    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(
                    CHANNEL_ID,
                    "Wask Service",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            serviceChannel.setSound(null, null);
            serviceChannel.enableVibration(false);
            serviceChannel.setVibrationPattern(null);
            serviceChannel.setShowBadge(false); //뱃지 제거

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(serviceChannel);
        }
    }

    /**
     * 앱이 처음 켜졌을 때, Notification channel을 생성한다.
     */
    private void createPushNotificationChannel() {
        MockDatabase.MockNotificationData pushNotificationData = MockDatabase.getReplacementCycleData(this);

        NotificationUtil.createNotificationChannel(this, pushNotificationData);
    }

    public static Application getApplication() {
        return application;
    }
}
