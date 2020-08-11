package com.naccoro.wask.main;

public interface MainContract {

    interface View {
        void showSettingView();
        void showCalendarView();
        void showReplaceToast();
        void disableReplaceButton();
    }

    interface Presenter {
        void clickSettingButton();
        void clickCalendarButton();
        void clickReplaceButton();
    }

}
