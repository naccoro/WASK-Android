package com.naccoro.wask.main;

public class MainPresenter implements MainContract.Presenter {
    MainActivity mainView;

    MainPresenter(MainActivity mainView) {
        this.mainView = mainView;
    }

    @Override
    public void clickSettingButton() {
        mainView.showSettingView();
    }
}
