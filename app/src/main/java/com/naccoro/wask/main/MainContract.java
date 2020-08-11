package com.naccoro.wask.main;

public interface MainContract {

    interface View {
        void showSettingView();

        void showCalendarView();

        void showGoodMainView();

        void showBadMainView();

        void setPeriodTextValue(int period);
    }

    interface Presenter {

        void start();

        void clickSettingButton();

        void clickCalendarButton();

        void changeMask();
    }

}
