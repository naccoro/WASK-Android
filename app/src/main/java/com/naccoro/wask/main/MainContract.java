package com.naccoro.wask.main;

public interface MainContract {

    interface View {
        void showSettingView();

        void showCalendarView();

        void showReplaceToast();

        void disableReplaceButton();

        void showGoodMainView();

        void showBadMainView();

        void setPeriodTextValue(int period);

        void enableReplaceButton();

        void showCancelDialog();
    }

    interface Presenter {

        void start();

        void clickSettingButton();

        void clickCalendarButton();

        void changeMask();

        void cancelChanging();
    }

}
