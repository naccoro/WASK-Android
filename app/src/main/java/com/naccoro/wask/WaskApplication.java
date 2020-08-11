package com.naccoro.wask;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

import com.naccoro.wask.preferences.SharedPreferenceManager;

public class WaskApplication extends Application {

    public static final String CHANNEL_ID="WaskChannel";

    @Override
    public void onCreate() {
        super.onCreate();

        init();
        createNotificationChannel();
    }

    private void init() {
        SharedPreferenceManager.getInstance().initInstance(getApplicationContext());
    }

    /**
     * OREO API 26 이상에서는 채널이 필수
     * notification 설정 값
     * */
    private void createNotificationChannel() {
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            NotificationChannel serviceChannel=new NotificationChannel(
                    CHANNEL_ID,
                    "Wask Service",
                    NotificationManager.IMPORTANCE_DEFAULT
            );

            NotificationManager manager= getSystemService(NotificationManager.class);
            manager.createNotificationChannel(serviceChannel);
        }
    }
}
