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

    @Override
    public void onCreate() {
        super.onCreate();

        init();

        createPushNotificationChannel();
    }

    private void init() {
        SharedPreferenceManager.getInstance().initInstance(getApplicationContext());
    }

    /**
     * 앱이 처음 켜졌을 때, Notification channel을 생성한다.
     */
    private void createPushNotificationChannel() {
        MockDatabase.MockNotificationData pushNotificationData = MockDatabase.getReplacementCycleData(this);

        NotificationUtil.createNotificationChannel(this, pushNotificationData);
    }
}
