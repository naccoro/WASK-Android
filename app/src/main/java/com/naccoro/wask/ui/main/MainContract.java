package com.naccoro.wask.ui.main;

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

        void showNoReplaceData();

        void changeUsePeriodMessage(int period);

        void setMaskReplaceNotification();

        void showForegroundNotification(int period);
    }

    interface Presenter {

        void start();

        void clickSettingButton();

        void clickCalendarButton();

        void changeMask();

        void cancelChanging();

        void showForegroundNotification();
    }

}
