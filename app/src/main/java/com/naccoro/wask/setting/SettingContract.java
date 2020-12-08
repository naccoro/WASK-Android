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

        void dismissForegroundAlert();

        void setAlertVisibleSwitchValue(boolean isChecked);

        void finishSettingView();
    }

    interface Presenter {
        void start(Context context);

        void clickHomeButton();

        void clickReplacementCycle();

        void clickReplaceLater();

        void clickPushAlert();

        void changeAlertVisibleSwitch(Context context, boolean isChecked);

        void changePushAlertValue(Context context, SettingPreferenceManager.SettingPushAlertType value);

        void changeReplacementCycleValue(Context context, int cycleValue);

        void changeReplaceLaterValue(Context context, int laterValue);
    }
}