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

        void showSnoozeInfoDialog();
    }

    interface Presenter {
        void start();

        void clickHomeButton();

        void clickReplacementCycle();

        void clickReplaceLater();

        void clickPushAlert();

        void changeAlertVisibleSwitch(boolean isChecked);

        void changePushAlertValue(SettingPreferenceManager.SettingPushAlertType value);

        void changeReplacementCycleValue(int cycleValue);

        void changeReplaceLaterValue(int laterValue);
    }
}