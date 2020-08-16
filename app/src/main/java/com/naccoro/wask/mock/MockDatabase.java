package com.naccoro.wask.mock;

import android.app.NotificationManager;
import android.content.Context;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.naccoro.wask.R;
import com.naccoro.wask.preferences.SettingPreferenceManager;
import com.naccoro.wask.utils.NotificationUtil;

import static com.naccoro.wask.preferences.SettingPreferenceManager.SettingPushAlertType.ALL;
import static com.naccoro.wask.preferences.SettingPreferenceManager.SettingPushAlertType.SOUND;
import static com.naccoro.wask.preferences.SettingPreferenceManager.SettingPushAlertType.VIBRATION;
import static com.naccoro.wask.preferences.SettingPreferenceManager.SettingPushAlertType.getPushAlertTypeWithIndex;

/**
 * Notification 관련 예비 Data가 저장되어 있는 MockDatabase
 * //TODO LATER관련 Database 생성
 */
public class MockDatabase {

    public static ReplacementCycleAppData getReplacementCycleData(Context context) {
        return new ReplacementCycleAppData(context);
    }

    public static ReplaceLaterAppData getReplaceLaterData(Context context) {
        return new ReplaceLaterAppData(context);
    }

    static class ReplaceLaterAppData extends MockNotificationData {

        public ReplaceLaterAppData(Context context) {
            contentText = context.getString(R.string.notification_replacelater_text);
            priority = NotificationCompat.PRIORITY_LOW;
            notification_id = 300;
            layoutId = R.layout.notification_replacemlater;

            channelName = context.getString(R.string.notificationchannel_push_name);
            channelDescription = context.getString(R.string.notificationchannel_push_description);
            channelImportance = NotificationManager.IMPORTANCE_DEFAULT;
            channelLockscreenVisibility = NotificationCompat.VISIBILITY_PRIVATE;

            int pushAlertIndex = SettingPreferenceManager.getPushAlert();
            SettingPreferenceManager.SettingPushAlertType pushAlertType = getPushAlertTypeWithIndex(pushAlertIndex);
            channelEnableSound = getValueSoundWithType(pushAlertType);
            channelEnableVibrate = getValueVibrateWithType(pushAlertType);
            channelId = NotificationUtil.getChannelIdByPushAlertType(context, pushAlertType);
        }

        private boolean getValueSoundWithType(SettingPreferenceManager.SettingPushAlertType type) {
            return type == SOUND || type == ALL;
        }

        private boolean getValueVibrateWithType(SettingPreferenceManager.SettingPushAlertType type) {
            return type == VIBRATION || type == ALL;
        }
    }

    /**
     * 교체 주기 Notification 관련 Data
     */
    static class ReplacementCycleAppData extends MockNotificationData {

        public ReplacementCycleAppData(Context context) {
            contentText = context.getString(R.string.notification_replacementcycle_text);
            priority = NotificationCompat.PRIORITY_LOW;
            notification_id = 888;
            layoutId = R.layout.notification_replacementcycle;


            channelName = context.getString(R.string.notificationchannel_push_name);
            channelDescription = context.getString(R.string.notificationchannel_push_description);
            channelImportance = NotificationManager.IMPORTANCE_DEFAULT;
            channelLockscreenVisibility = NotificationCompat.VISIBILITY_PRIVATE;

            int pushAlertIndex = SettingPreferenceManager.getPushAlert();
            SettingPreferenceManager.SettingPushAlertType pushAlertType = getPushAlertTypeWithIndex(pushAlertIndex);
            channelEnableSound = getValueSoundWithType(pushAlertType);
            channelEnableVibrate = getValueVibrateWithType(pushAlertType);

            channelId = NotificationUtil.getChannelIdByPushAlertType(context, pushAlertType);
        }

        private boolean getValueSoundWithType(SettingPreferenceManager.SettingPushAlertType type) {
            return type == SOUND || type == ALL;
        }

        private boolean getValueVibrateWithType(SettingPreferenceManager.SettingPushAlertType type) {
            return type == VIBRATION || type == ALL;
        }
    }

    /**
     * Notification의 필요한 Data를 정의한 클래스
     */
    public abstract static class MockNotificationData {

        //Notification 값
        protected String contentText;
        protected int priority;
        protected int notification_id;
        protected int layoutId;
        protected int okButtonId; //버튼의 기능이 달라진다면 가지고 있어야할듯
        protected int xButtonId; //버튼의 기능이 달라진다면 가지고 있어야 할듯

        //NotificationChannel 값
        protected String channelId;
        protected CharSequence channelName;
        protected String channelDescription;
        protected int channelImportance;
        protected boolean channelEnableVibrate;
        protected boolean channelEnableSound;
        protected int channelLockscreenVisibility;

        //Notification get methods:
        public String getContentText() {
            return contentText;
        }

        public void setContentText(String contentText) {
            this.contentText = contentText;
        }

        public int getPriority() {
            return priority;
        }

        public void setPriority(int priority) {
            this.priority = priority;
        }

        public int getNotification_id() {
            return notification_id;
        }

        public void setNotification_id(int notification_id) {
            this.notification_id = notification_id;
        }

        public int getLayoutId() {
            return layoutId;
        }

        public void setLayoutId(int layoutId) {
            this.layoutId = layoutId;
        }

        public int getOkButtonId() {
            return okButtonId;
        }

        public void setOkButtonId(int okButtonId) {
            this.okButtonId = okButtonId;
        }

        public int getXButtonId() {
            return xButtonId;
        }

        public void setXButtonId(int xButtonId) {
            this.xButtonId = xButtonId;
        }

        //channel Value get methods:

        public String getChannelId() {
            return channelId;
        }

        public boolean isChannelEnableSound() {
            return channelEnableSound;
        }

        public void setChannelEnableSound(boolean channelEnableSound) {
            this.channelEnableSound = channelEnableSound;
        }

        public void setChannelId(String channelId) {
            this.channelId = channelId;
        }

        public CharSequence getChannelName() {
            return channelName;
        }

        public void setChannelName(CharSequence channelName) {
            this.channelName = channelName;
        }

        public String getChannelDescription() {
            return channelDescription;
        }

        public void setChannelDescription(String channelDescription) {
            this.channelDescription = channelDescription;
        }

        public int getChannelImportance() {
            return channelImportance;
        }

        public void setChannelImportance(int channelImportance) {
            this.channelImportance = channelImportance;
        }

        public boolean isChannelEnableVibrate() {
            return channelEnableVibrate;
        }

        public void setChannelEnableVibrate(boolean channelEnableVibrate) {
            this.channelEnableVibrate = channelEnableVibrate;
        }

        public int getChannelLockscreenVisibility() {
            return channelLockscreenVisibility;
        }

        public void setChannelLockscreenVisibility(int channelLockscreenVisibility) {
            this.channelLockscreenVisibility = channelLockscreenVisibility;
        }
    }
}
