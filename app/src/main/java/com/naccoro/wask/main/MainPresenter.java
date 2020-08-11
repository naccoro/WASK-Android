package com.naccoro.wask.main;

public class MainPresenter implements MainContract.Presenter {
    MainContract.View mainView;

    MainPresenter(MainContract.View mainView) {
        this.mainView = mainView;
    }

    @Override
    public void clickSettingButton() {
        mainView.showSettingView();
    }

    @Override
    public void clickCalendarButton() {
        mainView.showCalendarView();
    }
}
