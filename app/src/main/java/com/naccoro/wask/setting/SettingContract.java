package com.naccoro.wask.setting;

public interface SettingContract {
    interface View {
        void showReplacementCycleDialog();

        void showReplaceLaterDialog();

        void showPushAlertDialog();
    }

    interface Presenter {
        void clickHomeButton();

        void clickReplacementCycle();

        void clickReplaceLater();

        void clickPushAlert();

        void changeAlertVisibleSwitch(boolean value);
    }
}
