package com.naccoro.wask.setting;

import com.naccoro.wask.preferences.SettingPreferenceManager;
import com.naccoro.wask.replacement.repository.ReplacementHistoryRepository;
import com.naccoro.wask.utils.DateUtils;

import static com.naccoro.wask.preferences.SettingPreferenceManager.SettingPushAlertType.getPushAlertTypeWithIndex;

public class SettingPresenter implements SettingContract.Presenter {

    SettingContract.View settingView;
    ReplacementHistoryRepository repository;

    SettingPresenter(SettingContract.View settingView, ReplacementHistoryRepository replacementHistoryRepository) {
        this.settingView = settingView;
        this.repository = replacementHistoryRepository;
    }

    @Override
    public void start() {
        int replaceCycleValue = SettingPreferenceManager.getReplaceCycle();
        settingView.showReplacementCycleValue(replaceCycleValue);

        int replaceLaterValue = SettingPreferenceManager.getDelayCycle();
        settingView.showReplaceLaterValue(replaceLaterValue);

        int pushAlertIndex = SettingPreferenceManager.getPushAlert();
        settingView.showPushAlertValue(getPushAlertTypeString(pushAlertIndex));

        int languageIndex = SettingPreferenceManager.getLanguage();
        settingView.showLanguageLabel(settingView.getLanguageString(languageIndex));

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

    @Override
    public void clickLanguage() {
        settingView.showLanguageDialog();
    }

    /**
     * 사용자가 Foregorund Alert Visible Switch의 값을 변경했을 때
     *
     * @param isChecked : 변경된 값
     */
    @Override
    public void changeAlertVisibleSwitch(boolean isChecked) {
        SettingPreferenceManager.setIsShowNotificationBar(isChecked);
        if (isChecked) {
            int period = getMaskPeriod();

            //교체일자가 없다면 실행하지 말것
            if (period > 0) {
                settingView.showForegroundAlert(getMaskPeriod());
            }
        }
        else {
            settingView.dismissForegroundAlert();
        }
    }

    /**
     * pushAlert 설정 값을 변경하는 함수
     *
     * @param value: 사용자가 설정한 값  e.g 소리, 진동, 소리+진동, 없음
     */
    @Override
    public void changePushAlertValue(SettingPreferenceManager.SettingPushAlertType value) {
        int oldValue = SettingPreferenceManager.getPushAlert();
        int newValue = value.getTypeIndex();

        SettingPreferenceManager.setPushAlert(newValue);

        settingView.updateNotificationChanel(getPushAlertTypeWithIndex(oldValue));

        //설정 값에 대응하는 문자열을 보여주기
        settingView.showPushAlertValue(getPushAlertTypeString(newValue));
    }

    /**
     * 마스크 교체주기 설정하는 함수
     *
     * @param cycleValue : 교체 주기
     */
    @Override
    public void changeReplacementCycleValue(int cycleValue) {
        SettingPreferenceManager.setReplaceCycle(cycleValue);
        settingView.refreshAlarm();
        settingView.showReplacementCycleValue(cycleValue);
    }

    /**
     * 나중에 교체하기 주기 설정하는 함수
     *
     * @param laterValue : 나중에 교체 주기
     */
    @Override
    public void changeReplaceLaterValue(int laterValue) {
        SettingPreferenceManager.setDelayCycle(laterValue);
        settingView.refreshAlarmInSnooze();
        settingView.showReplaceLaterValue(laterValue);
    }

    @Override
    public void changeLanguage(SettingPreferenceManager.SettingLanguage language) {
        SettingPreferenceManager.setLanguage(language);
        settingView.refresh();
    }

    /**
     * 마스크 착용일 설정하는 함수
     * */
    private int getMaskPeriod() {
        int lastReplacement = repository.getLastReplacement();
        if (lastReplacement == -1) {
            //교체 기록이 없을 경우
            return 0;
        }
        return DateUtils.calculateDateGapWithToday(lastReplacement) + 1;
    }

    //StringArray에서 인덱스에 해당하는 푸시 알림 방식 문자열을 불러오기
    private String getPushAlertTypeString(int index) {
        return settingView.getPushAlertTypeString(index);
    }
}
