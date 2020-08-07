package com.naccoro.wask;

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
        return SharedPreferenceManager.getInstance().getInt(PREF_KEY_REPLACE_CYCLE);
    }

    public static int getDelayCycle() {
        return SharedPreferenceManager.getInstance().getInt(PREF_KEY_DELAY_CYCLE);
    }

    public static int getPushAlert() {
        return SharedPreferenceManager.getInstance().getInt(PREF_KEY_PUSH_ALERT);
    }

    public static boolean getIsShowNotificationBar() {
        return SharedPreferenceManager.getInstance().getBoolean(PREF_KEY_IS_SHOW_NOTIFICATION_BAR);
    }
}
