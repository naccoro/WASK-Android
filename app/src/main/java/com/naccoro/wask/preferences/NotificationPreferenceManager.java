package com.naccoro.wask.preferences;

/**
 * 핸드폰이 종료되고 부팅되었을 때 사용자가 선택한 교체주기에 알람이 작동하게 만들기 위해
 * 처음 알람을 등록한 날짜를 저장
 */
public class NotificationPreferenceManager {
    // preference key
    private static final String PREF_KEY_REPLACE_CYCLE = "replace_cycle_date"; // 교체주기 알람 등록 일자
    private static final String PREF_KEY_DELAY_CYCLE = "delay_cycle_date"; // 미루기주기 알람 등록 일자

    // default Value
    private static final int DEFAULT_REPLACEMENT_CYCLE = 0;
    private static final int DEFAULT_REPLACEMENT_LATER = 0;

    public static void setReplacementCycleDate(int date) {
        SharedPreferenceManager.getInstance().setInt(PREF_KEY_REPLACE_CYCLE, date);
    }

    public static int getReplacementCycleDate() {
        return SharedPreferenceManager.getInstance().getInt(PREF_KEY_REPLACE_CYCLE, DEFAULT_REPLACEMENT_CYCLE);
    }

    public static void setReplaceLaterDate(int date) {
        SharedPreferenceManager.getInstance().setInt(PREF_KEY_DELAY_CYCLE, date);
    }

    public static int getReplaceLaterDate() {
        return SharedPreferenceManager.getInstance().getInt(PREF_KEY_REPLACE_CYCLE, DEFAULT_REPLACEMENT_LATER);
    }


}
