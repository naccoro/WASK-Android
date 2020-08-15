package com.naccoro.wask.utils;


import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;

import com.naccoro.wask.R;
import com.naccoro.wask.mock.MockDatabase;
import com.naccoro.wask.preferences.SettingPreferenceManager;

public class NotificationUtil {
    /**
     * Orea이상 Notification을 띄우려면 Channel을 생성해야함
     */
    public static String createNotificationChannel(
            Context context,
            MockDatabase.MockNotificationData mockNotificationData) {

        // NotificationChannels are required for Notifications on O (API 26) and above.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            // The id of the channel.
            String channelId = mockNotificationData.getChannelId();

            // The user-visible name of the channel.
            CharSequence channelName = mockNotificationData.getChannelName();
            // The user-visible description of the channel.
            String channelDescription = mockNotificationData.getChannelDescription();
            int channelImportance = mockNotificationData.getChannelImportance();
            boolean channelEnableVibrate = mockNotificationData.isChannelEnableVibrate();
            boolean channelEnableSound = mockNotificationData.isChannelEnableSound();
            int channelLockscreenVisibility =
                    mockNotificationData.getChannelLockscreenVisibility();

            // Initializes NotificationChannel.
            NotificationChannel notificationChannel =
                    new NotificationChannel(channelId, channelName, channelImportance);
            notificationChannel.setDescription(channelDescription);
            notificationChannel.enableVibration(channelEnableVibrate); //진동 켜고 끄기
            notificationChannel.setShowBadge(false); //뱃지 제거

            if (!channelEnableSound) {
                notificationChannel.setSound(null, null);
            }
            if (!channelEnableVibrate) {
                notificationChannel.setVibrationPattern(null);
            }
            notificationChannel.setLockscreenVisibility(channelLockscreenVisibility);

            // Adds NotificationChannel to system. Attempting to create an existing notification
            // channel with its original values performs no operation, so it's safe to perform the
            // below sequence.
            NotificationManager notificationManager =
                    (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(notificationChannel);

            return channelId;
        } else {
            // Returns null for pre-O (26) devices.
            return null;
        }
    }

    /**
     * Oreo 버전 이상일 경우
     * 사용자가 설정 값을 변경하면 이전에 있었던 채널을 삭제해야 소리, 진동 설정을 변경 할 수 있다.
     */
    public static void deleteNotificationChannel(Context context, SettingPreferenceManager.SettingPushAlertType type) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            String channelId = getChannelIdByPushAlertType(context, type);


            NotificationManager notificationManager =
                    (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.deleteNotificationChannel(channelId);
        }
    }

    public static String getChannelIdByPushAlertType(Context context, SettingPreferenceManager.SettingPushAlertType type) {
        String channelId;

        switch (type) {
            case SOUND:
                channelId = context.getString(R.string.notificationchannel_pushsound_id);
                break;
            case VIBRATION:
                channelId = context.getString(R.string.notificationchannel_pushvibration_id);
                break;
            case ALL:
                channelId = context.getString(R.string.notificationchannel_pushall_id);
                break;
            default:
                channelId = context.getString(R.string.notificationchannel_pushnone_id);
        }
        return channelId;
    }
}
