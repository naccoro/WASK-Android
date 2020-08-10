package com.naccoro.wask.setting;

public interface SettingContract {
    interface View {
        void showReplacementCycleDialog();

        void showReplaceLaterDialog();

        void showPushAlertDialog();

        void showReplacementCycleValue(int cycleValue);

        void showReplaceLaterValue(int laterValue);

        void showPushAlertValue(String pushAlertValue);

        void setAlertVisibleSwitchValue(boolean isChecked);
    }

    interface Presenter {
        void start();

        void clickHomeButton();

        void clickReplacementCycle();

        void clickReplaceLater();

        void clickPushAlert();

        void changeAlertVisibleSwitch(boolean isChecked);

        void changePushAlertValue(String value);

        void changeReplacementCycleValue(int cycleValue);

        void changeReplaceLaterValue(int laterValue);
    }
}
