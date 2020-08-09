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

    /**
     * 사용자가 Foregorund Alert Visible Switch의 값을 변경했을 때
     * @param value : 변경된 값
     */
    @Override
    public void changeAlertVisibleSwitch(boolean value) {

    }


    @Override
    public void changePushAlertValue(String value) {

    }

    @Override
    public void changeReplacementCycleValue(int cycleValue) {

    }

    @Override
    public void changeReplaceLaterValue(int laterValue) {

    }
}
