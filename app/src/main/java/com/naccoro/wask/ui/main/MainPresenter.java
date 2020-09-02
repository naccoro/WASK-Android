package com.naccoro.wask.ui.main;


import android.content.Context;

import com.naccoro.wask.WaskApplication;
import com.naccoro.wask.preferences.SettingPreferenceManager;
import com.naccoro.wask.utils.AlarmUtil;
import com.naccoro.wask.utils.DateUtils;

import android.util.Log;

import com.naccoro.wask.replacement.repository.ReplacementHistoryRepository;

public class MainPresenter implements MainContract.Presenter {

    private static final String TAG = "MainPresenter";

    private ReplacementHistoryRepository replacementHistoryRepository;

    private boolean isNoData = true;

    MainContract.View mainView;

    MainPresenter(MainContract.View mainView, ReplacementHistoryRepository replacementHistoryRepository) {
        this.mainView = mainView;
        this.replacementHistoryRepository = replacementHistoryRepository;
    }

    /**
     * MainView에서 보여줘야 할 Data를 가져오는 함수
     */
    @Override
    public void start() {
        int period;

        period = getMaskPeriod();
        Log.d("period", period + "");
        mainView.setPeriodTextValue(period);

        if (period == 0) {
            //교체 기록이 없음
            mainView.showNoReplaceData();
            mainView.enableReplaceButton();
        } else if (period > 1) {

            checkIsFirstReplacement();

            mainView.showBadMainView();
            mainView.enableReplaceButton();
        } else {
            //교체한 당일
            checkIsFirstReplacement();
            WaskApplication.isChanged = true;
            mainView.showGoodMainView();
            mainView.disableReplaceButton();
        }
    }

    /**
     * 첫 번째 교체인지 확인 후 멘트 변경
     */
    private void checkIsFirstReplacement() {
        if (isNoData) {
            mainView.changeUsePeriodMessage();
            isNoData = false;
        }
    }

    @Override
    public void clickSettingButton() {
        mainView.showSettingView();
    }

    @Override
    public void clickCalendarButton() {
        mainView.showCalendarView();
    }

    /**
     * 사용자가 교체하기 버튼을 누를 경우 호출되는 함수
     */
    @Override
    public void changeMask(Context context) {

        if (!WaskApplication.isChanged) {
            checkIsFirstReplacement();

            replacementHistoryRepository.insertToday(new ReplacementHistoryRepository.InsertHistoryCallback() {
                @Override
                public void onSuccess() {
                    mainView.showReplaceToast();
                    mainView.enableReplaceButton();
                    WaskApplication.isChanged = true;

                    start();

                    setMaskReplaceNotification(context);
                }

                @Override
                public void onDuplicated() {
                    Log.d(TAG, "onDuplicated: true");
                }
            });
        } else {
            mainView.showCancelDialog();
        }
    }

    /**
     * 교체 취소 다이얼로그에서 확인을 눌렀을 때 호출되는 함수
     */
    @Override
    public void cancelChanging(Context context) {
        replacementHistoryRepository.deleteToday();
        WaskApplication.isChanged = false;

        //메인화면 갱신
        start();

        setMaskReplaceNotification(context);
    }

    /**
     * Foreground  변경점을 반영한다.
     */
    private void showForegroundNotification(Context context) {

        if (SettingPreferenceManager.getIsShowNotificationBar()) {
            int period = getMaskPeriod();
            if (period > 0) {
                AlarmUtil.showForegroundService(context, period);
                AlarmUtil.setForegroundAlarm(context);
            } else {
                AlarmUtil.dismissForegroundService(context);
                AlarmUtil.cancelForegroundAlarm(context);
            }
        }
    }

    /**
     * 등록되어 있는 알람을 종료하고 새로운 교체하기 알람을 등록한다.
     */
    private void setMaskReplaceNotification(Context context) {
        //기본 교체하기 알람이 있었다면 제거
        if (AlarmUtil.isCycleAlarmExist(context)) {
            AlarmUtil.cancelReplacementCycleAlarm(context);

        //나중에 교체하기 알림 중이었다면 제거
        } else if (AlarmUtil.isLaterAlarmExist(context)) {
            AlarmUtil.cancelReplaceLaterAlarm(context);
        }

        showForegroundNotification(context);
        AlarmUtil.setReplacementCycleAlarm(context);
    }

    /**
     * WaskDatabase에서 현재 마스크 교체 상태를 가져오는 함수
     *
     * @return [오늘 날짜 - 마지막 교체 일자 + 1]
     */
    private int getMaskPeriod() {
        int lastReplacement = replacementHistoryRepository.getLastReplacement();
        if (lastReplacement == -1) {
            //교체 기록이 없을 경우
            isNoData = true;
            return 0;
        }

        return DateUtils.calculateDateGapWithToday(lastReplacement) + 1;
    }
}