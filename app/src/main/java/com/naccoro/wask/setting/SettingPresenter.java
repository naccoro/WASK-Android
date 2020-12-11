package com.naccoro.wask.setting;

import android.content.Context;

import com.naccoro.wask.R;
import com.naccoro.wask.mock.MockDatabase;
import com.naccoro.wask.preferences.SettingPreferenceManager;
import com.naccoro.wask.replacement.model.Injection;
import com.naccoro.wask.replacement.repository.ReplacementHistoryRepository;
import com.naccoro.wask.utils.AlarmUtil;
import com.naccoro.wask.utils.DateUtils;
import com.naccoro.wask.utils.LanguageUtil;
import com.naccoro.wask.utils.NotificationUtil;

import static com.naccoro.wask.preferences.SettingPreferenceManager.SettingPushAlertType.getPushAlertTypeWithIndex;

public class SettingPresenter implements SettingContract.Presenter {

    SettingContract.View settingView;

    SettingPresenter(SettingContract.View settingView) {
        this.settingView = settingView;
    }

    @Override
    public void start(Context context) {
        int replaceCycleValue = SettingPreferenceManager.getReplaceCycle();
        settingView.showReplacementCycleValue(replaceCycleValue);

        int replaceLaterValue = SettingPreferenceManager.getDelayCycle();
        settingView.showReplaceLaterValue(replaceLaterValue);

        int pushAlertIndex = SettingPreferenceManager.getPushAlert();
        settingView.showPushAlertValue(getPushAlertTypeString(context, pushAlertIndex));

        int languageIndex = SettingPreferenceManager.getLanguage();
        settingView.showLanguageLabel(LanguageUtil.getLanguageString(context, languageIndex));

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
    public void changeAlertVisibleSwitch(Context context, boolean isChecked) {
        SettingPreferenceManager.setIsShowNotificationBar(isChecked);
        if (isChecked) {
            int period = getMaskPeriod(context);

            //교체일자가 없다면 실행하지 말것
            if (period > 0) {
                settingView.showForegroundAlert(getMaskPeriod(context));
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
    public void changePushAlertValue(Context context, SettingPreferenceManager.SettingPushAlertType value) {
        int oldValue = SettingPreferenceManager.getPushAlert();
        int newValue = value.getTypeIndex();

        SettingPreferenceManager.setPushAlert(newValue);

        MockDatabase.MockNotificationData pushAlertData = MockDatabase.getReplacementCycleData(context);

        //기존 Channel 삭제
        NotificationUtil.deleteNotificationChannel(context, getPushAlertTypeWithIndex(oldValue));
        //새롭게 변경된 설정 적용하여 channel 생성
        NotificationUtil.createNotificationChannel(context, pushAlertData);

        //설정 값에 대응하는 문자열을 보여주기
        settingView.showPushAlertValue(getPushAlertTypeString(context,newValue));
    }

    /**
     * 마스크 교체주기 설정하는 함수
     *
     * @param cycleValue : 교체 주기
     */
    @Override
    public void changeReplacementCycleValue(Context context, int cycleValue) {
        SettingPreferenceManager.setReplaceCycle(cycleValue);

        //Alarm 다시 설정
        AlarmUtil.cancelReplacementCycleAlarm(context);
        AlarmUtil.setReplacementCycleAlarm(context);

        settingView.showReplacementCycleValue(cycleValue);
    }

    /**
     * 나중에 교체하기 주기 설정하는 함수
     *
     * @param laterValue : 나중에 교체 주기
     */
    @Override
    public void changeReplaceLaterValue(Context context, int laterValue) {
        SettingPreferenceManager.setDelayCycle(laterValue);

        //나중에 교체하기 알람 중이었다면 재설정
        if (AlarmUtil.isLaterAlarmExist(context)) {
            AlarmUtil.cancelReplaceLaterAlarm(context);
            AlarmUtil.setReplacementLaterAlarm(context, true);
        }

        settingView.showReplaceLaterValue(laterValue);
    }

    @Override
    public void changeLanguage(Context context, SettingPreferenceManager.SettingLanguage language) {
        SettingPreferenceManager.setLanguage(language);
        settingView.refresh();
    }

    /**
     * 마스크 착용일 설정하는 함수
     * */
    private int getMaskPeriod(Context context) {
        ReplacementHistoryRepository replacementHistoryRepository = Injection.replacementHistoryRepository(context);
        int lastReplacement = replacementHistoryRepository.getLastReplacement();
        if (lastReplacement == -1) {
            //교체 기록이 없을 경우
            return 0;
        }
        return DateUtils.calculateDateGapWithToday(lastReplacement) + 1;
    }

    //StringArray에서 인덱스에 해당하는 푸시 알림 방식 문자열을 불러오기
    private String getPushAlertTypeString(Context context, int index) {
        return context.getResources().getStringArray(R.array.ALERT_TYPE)[index];
    }
}
