package com.naccoro.wask.setting;

import android.content.Context;

import com.naccoro.wask.mock.MockDatabase;
import com.naccoro.wask.preferences.SettingPreferenceManager;
import com.naccoro.wask.utils.AlarmUtil;
import com.naccoro.wask.utils.NotificationUtil;

import static com.naccoro.wask.preferences.SettingPreferenceManager.SettingPushAlertType.getPushAlertTypeWithIndex;
import static com.naccoro.wask.preferences.SettingPreferenceManager.SettingPushAlertType.getPushAlertTypeWithValue;

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
    public void changePushAlertValue(Context context, String value) {
        SettingPreferenceManager.SettingPushAlertType pushAlertType = getPushAlertTypeWithValue(value);
        SettingPreferenceManager.setPushAlert(pushAlertType.getTypeIndex());

        MockDatabase.MockNotificationData pushAlertData = MockDatabase.getReplacementCycleData(context);

        //기존 Channel 삭제
        NotificationUtil.deleteNotificationChannel(context, pushAlertData.getChannelId());
        //새롭게 변경된 설정 적용하여 channel 생성
        NotificationUtil.createNotificationChannel(context, pushAlertData);

        settingView.showPushAlertValue(value);
    }

    /**
     * 마스크 교체주기 설정하는 함수
     * @param cycleValue : 교체 주기
     */
    @Override
    public void changeReplacementCycleValue(Context context, int cycleValue) {
        SettingPreferenceManager.setReplaceCycle(cycleValue);

        //Alarm 다시 설정
        AlarmUtil.cancelReplacementCycleAlarm(context);
        AlarmUtil.setReplacementCycleAlarm(context, cycleValue);

        settingView.showReplacementCycleValue(cycleValue);
    }

    /**
     * 나중에 교체하기 주기 설정하는 함수
     * @param laterValue : 나중에 교체 주기
     */
    @Override
    public void changeReplaceLaterValue(Context context, int laterValue) {
        SettingPreferenceManager.setDelayCycle(laterValue);

        //Alarm 다시 설정
        AlarmUtil.cancelReplacementCycleAlarm(context);
        AlarmUtil.setReplacementLaterAlarm(context, laterValue);

        settingView.showReplaceLaterValue(laterValue);
    }
}
