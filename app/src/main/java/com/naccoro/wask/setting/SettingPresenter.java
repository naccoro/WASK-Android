package com.naccoro.wask.setting;

import com.naccoro.wask.preferences.SettingPreferenceManager;

public class SettingPresenter implements SettingContract.Presenter {

    SettingContract.View settingView;

    SettingPresenter(SettingContract.View settingView) {
        this.settingView = settingView;
    }

    @Override
    public void start() {
        int replaceCycleValue = SettingPreferenceManager.getReplaceCycle();
        settingView.showReplacementCycleValue(replaceCycleValue);

        int replaceLaterValue = SettingPreferenceManager.getDelayCycle();
        settingView.showReplaceLaterValue(replaceLaterValue);

        int pushAlertIndex = SettingPreferenceManager.getPushAlert();
        SettingPreferenceManager.SettingPushAlertType pushAlertType = getPushAlertTypeWithIndex(pushAlertIndex);
        settingView.showPushAlertValue(pushAlertType.getTypeValue());

        boolean isShowNotificationBar = SettingPreferenceManager.getIsShowNotificationBar();
        settingView.setAlertVisibleSwitchValue(isShowNotificationBar);
    }

    @Override
    public void clickHomeButton() {
        settingView.finishSettingView();
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
     * 마스크 착용일 설정하는 함수
     * @param maskPeriod : 사용자가 연속해서 마스크를 착용한 일수
     * */
    public void getMaskPeriod(int maskPeriod) {
        // TODO : DB에서 값 가져오기
        maskPeriod = 20;
        settingView.showForegroundAlert(maskPeriod);
    }

    /**
     * 사용자가 Foregorund Alert Visible Switch의 값을 변경했을 때
     * @param isChecked : 변경된 값
     */
    @Override
    public void changeAlertVisibleSwitch(boolean isChecked) {
        SettingPreferenceManager.setIsShowNotificationBar(isChecked);
    }

    /**
     * pushAlert 설정 값을 변경하는 함수
     * @param value: 사용자가 설정한 값  e.g 소리, 진동, 소리+진동, 없음
     */
    @Override
    public void changePushAlertValue(String value) {
        SettingPreferenceManager.SettingPushAlertType pushAlertType = getPushAlertTypeWithValue(value);
        SettingPreferenceManager.setPushAlert(pushAlertType.getTypeIndex());

        settingView.showPushAlertValue(value);
    }

    /**
     * 마스크 교체주기 설정하는 함수
     * @param cycleValue : 교체 주기
     */
    @Override
    public void changeReplacementCycleValue(int cycleValue) {
        SettingPreferenceManager.setReplaceCycle(cycleValue);

        settingView.showReplacementCycleValue(cycleValue);
    }

    /**
     * 나중에 교체하기 주기 설정하는 함수
     * @param laterValue : 나중에 교체 주기
     */
    @Override
    public void changeReplaceLaterValue(int laterValue) {
        SettingPreferenceManager.setDelayCycle(laterValue);

        settingView.showReplaceLaterValue(laterValue);
    }

    /**
     * enum class인 SettngPushAlertType을 index 매개변수로 구하는 함수
     * @param index : 구하고자 하는 index
     * @return : 구한 SettingPushAlertType 객체
     */
    private SettingPreferenceManager.SettingPushAlertType getPushAlertTypeWithIndex(int index) {
        switch (index) {
            case 0:
                return SettingPreferenceManager.SettingPushAlertType.SOUND;

            case 1:
                return SettingPreferenceManager.SettingPushAlertType.VIBRATION;

            case 2:
                return SettingPreferenceManager.SettingPushAlertType.ALL;

            default:
                return SettingPreferenceManager.SettingPushAlertType.NONE;
        }
    }

    /**
     * enum class인 SettngPushAlertType을 value 매개변수로 구하는 함수
     * @param value : 구하고자 하는 value
     * @return : 구한 SettingPushAlertType 객체
     */
    private SettingPreferenceManager.SettingPushAlertType getPushAlertTypeWithValue(String value) {
        switch (value) {
            case "소리":
                return SettingPreferenceManager.SettingPushAlertType.SOUND;

            case "진동":
                return SettingPreferenceManager.SettingPushAlertType.VIBRATION;

            case "소리+진동":
                return SettingPreferenceManager.SettingPushAlertType.ALL;

            default:
                return SettingPreferenceManager.SettingPushAlertType.NONE;
        }
    }
}
