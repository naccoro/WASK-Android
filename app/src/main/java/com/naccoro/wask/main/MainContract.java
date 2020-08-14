package com.naccoro.wask.main;

import android.content.Context;

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

        void changeUsePeriodMessage();
    }

    interface Presenter {

        void start();

        void clickSettingButton();

        void clickCalendarButton();

        void changeMask(Context context);

        void cancelChanging(Context context);

    }

}
