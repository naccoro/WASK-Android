package com.naccoro.wask.setting;

import android.content.Context;

import com.naccoro.wask.preferences.SettingPreferenceManager;

public interface SettingContract {
    interface View {
        void showReplacementCycleDialog();

        void showReplaceLaterDialog();

        void showPushAlertDialog();

        void showReplacementCycleValue(int cycleValue);

        void showReplaceLaterValue(int laterValue);

        void showPushAlertValue(String pushAlertValue);

        void showForegroundAlert(int maskPeriod);

        void showLanguageDialog();

        void showLanguageLabel(String language);

        void dismissForegroundAlert();

        void setAlertVisibleSwitchValue(boolean isChecked);

        void finishSettingView();

        void refresh();
    }

    interface Presenter {
        void start(Context context);

        void clickHomeButton();

        void clickReplacementCycle();

        void clickReplaceLater();

        void clickPushAlert();

        void clickLanguage();

        void changeAlertVisibleSwitch(Context context, boolean isChecked);

        void changePushAlertValue(Context context, String value);

        void changeReplacementCycleValue(Context context, int cycleValue);

        void changeReplaceLaterValue(Context context, int laterValue);

        void changeLanguage(Context context, SettingPreferenceManager.SettingLanguage language);
    }
}