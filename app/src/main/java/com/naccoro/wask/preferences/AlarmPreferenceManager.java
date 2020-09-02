package com.naccoro.wask.preferences;

public class AlarmPreferenceManager {

    private static final String PREF_KEY_IS_REPLACEMENT_LATER = "is_replacement_later";

    private static final boolean DEFAULT_IS_REPLACEMENT_LATER = false;

    public static void setIsReplacementLater(boolean isReplacementLater) {
        SharedPreferenceManager.getInstance().setBoolean(PREF_KEY_IS_REPLACEMENT_LATER, isReplacementLater);
    }

    public static boolean getIsReplacementLater() {
        return SharedPreferenceManager.getInstance().getBoolean(PREF_KEY_IS_REPLACEMENT_LATER, DEFAULT_IS_REPLACEMENT_LATER);
    }
}
