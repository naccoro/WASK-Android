package com.naccoro.wask.main;

public interface MainContract {

    interface View {
        void showSettingView();
        void showCalendarView();
    }

    interface Presenter {
        void clickSettingButton();
        void clickCalendarButton();
    }

}
