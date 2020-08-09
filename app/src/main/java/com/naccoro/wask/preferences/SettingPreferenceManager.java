package com.naccoro.wask.preferences;

/**
 * SharedPreference의 사용을 도와줌 (Setting값)
 * getter, setter 사용
 */
public class SettingPreferenceManager {

    // preference key
    private static final String PREF_KEY_REPLACE_CYCLE = "replace_cycle"; // 교체주기
    private static final String PREF_KEY_DELAY_CYCLE = "delay_cycle"; // 미루기주기
    private static final String PREF_KEY_PUSH_ALERT = "push_alert"; // 푸쉬알림
    private static final String PREF_KEY_IS_SHOW_NOTIFICATION_BAR = "is_show_notification_bar"; // boolean 알림바

    // default Value
    private static final int DEFAULT_REPLACEMENT_CYCLE = 3;
    private static final int DEFAULT_REPLACEMENT_LATER = 1;
    private static final SettingPushAlertType DEFAULT_PUSH_ALERT = SettingPushAlertType.ALL;
    private static final boolean DEFAULT_VISIBLE_ALERT = false;


    public static void setReplaceCycle(int replaceCycle) {
        SharedPreferenceManager.getInstance().setInt(PREF_KEY_REPLACE_CYCLE, replaceCycle);
    }

    public static void setDelayCycle(int delayCycle) {
        SharedPreferenceManager.getInstance().setInt(PREF_KEY_DELAY_CYCLE, delayCycle);
    }

    public static void setPushAlert(int pushAlert) {
        SharedPreferenceManager.getInstance().setInt(PREF_KEY_PUSH_ALERT, pushAlert);
    }

    public static void setIsShowNotificationBar(boolean isShowNotificationBar) {
        SharedPreferenceManager.getInstance().setBoolean(PREF_KEY_IS_SHOW_NOTIFICATION_BAR, isShowNotificationBar);
    }

    public static int getReplaceCycle() {
        return SharedPreferenceManager.getInstance().getInt(PREF_KEY_REPLACE_CYCLE, DEFAULT_REPLACEMENT_CYCLE);
    }

    public static int getDelayCycle() {
        return SharedPreferenceManager.getInstance().getInt(PREF_KEY_DELAY_CYCLE, DEFAULT_REPLACEMENT_LATER);
    }

    public static int getPushAlert() {
        return SharedPreferenceManager.getInstance().getInt(PREF_KEY_PUSH_ALERT, DEFAULT_PUSH_ALERT.getTypeIndex());
    }

    public static boolean getIsShowNotificationBar() {
        return SharedPreferenceManager.getInstance().getBoolean(PREF_KEY_IS_SHOW_NOTIFICATION_BAR, DEFAULT_VISIBLE_ALERT);
    }

    /**
     * pushalert 설정 type class
     * SOUND : 소리, VIBRATION : 진동, ALL : 소리+진동, NONE : 없음
     */
    public enum SettingPushAlertType {
        SOUND(0, "소리"), VIBRATION(1, "진동"), ALL(2, "소리+진동"), NONE(3, "없음");

        private int typeIndex;
        private String typeValue;

        SettingPushAlertType(int index, String value) {
            this.typeIndex = index;
            this.typeValue = value;
        }

        public int getTypeIndex() {
            return typeIndex;
        }

        public String getTypeValue() {
            return typeValue;
        }
    }
}
