package com.naccoro.wask.setting;

public class SettingPresenter implements SettingContract.Presenter {
    SettingActivity settingView;

    SettingPresenter(SettingActivity settingView) {
        this.settingView = settingView;
    }
    @Override
    public void clickHomeButton() {
        settingView.finish();
    }

    @Override
    public void clickReplacementCycle() {
        settingView.showReplacementCycleDialog();
    }

    @Override
    public void clickReplaceLater() {
        settingView.showReplaceLaterDialog();
    }

    @Override
    public void clickPushAlert() {
        settingView.showPushAlertDialog();
    }

    @Override
    public void changeAlertVisibleSwitch(boolean value) {

    }
}
