package com.naccoro.wask.main;

import android.content.Context;

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

        void changeMask(Context context);
    }

}
