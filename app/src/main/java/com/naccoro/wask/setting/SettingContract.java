package com.naccoro.wask.setting;

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

        void showSnoozeInfoDialog();

        void updateNotificationChanel(SettingPreferenceManager.SettingPushAlertType pushAlertTypeWithIndex);

        void refreshAlarm();

        void refreshAlarmInSnooze();

        String getPushAlertTypeString(int index);
    }

    interface Presenter {
        void start();

        void clickHomeButton();

        void clickReplacementCycle();

        void clickReplaceLater();

        void clickPushAlert();

        void changeAlertVisibleSwitch(boolean isChecked);

        void clickLanguage();

        void changeAlertVisibleSwitch(boolean isChecked);

        void changePushAlertValue(SettingPreferenceManager.SettingPushAlertType value);

        void changeReplacementCycleValue(int cycleValue);

        void changeReplaceLaterValue(int laterValue);

        void changeReplaceLaterValue(int laterValue);

        void changeLanguage(SettingPreferenceManager.SettingLanguage language);
    }
}