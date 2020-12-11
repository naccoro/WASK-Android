package com.naccoro.wask.ui.main;

import android.util.Log;

import com.naccoro.wask.WaskApplication;
import com.naccoro.wask.preferences.SettingPreferenceManager;
import com.naccoro.wask.replacement.repository.ReplacementHistoryRepository;
import com.naccoro.wask.utils.DateUtils;

public class MainPresenter implements MainContract.Presenter {

    private static final String TAG = "MainPresenter";

    private ReplacementHistoryRepository replacementHistoryRepository;

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
            setUsingPeriodMessage(period);
            WaskApplication.isChanged = false;
            mainView.showBadMainView();
            mainView.enableReplaceButton();
        } else {
            //교체한 당일
            setUsingPeriodMessage(period);
            WaskApplication.isChanged = true;
            mainView.showGoodMainView();
            mainView.disableReplaceButton();
        }
    }

    /**
     * 교체 일자에 따른 사용 메시지 변경
     */
    private void setUsingPeriodMessage(int period) {
        mainView.changeUsePeriodMessage(period);
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
    public void changeMask() {

        if (!WaskApplication.isChanged) {
//            checkIsFirstReplacement(getMaskPeriod());

            replacementHistoryRepository.insertToday(new ReplacementHistoryRepository.InsertHistoryCallback() {
                @Override
                public void onSuccess() {
                    mainView.showReplaceToast();
                    mainView.enableReplaceButton();
                    WaskApplication.isChanged = true;

                    start();

                    setMaskReplaceNotification();
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
    public void cancelChanging() {
        replacementHistoryRepository.deleteToday();
        WaskApplication.isChanged = false;

        //메인화면 갱신
        start();

        setMaskReplaceNotification();
    }

    /**
     * Foreground  변경점을 반영한다.
     */
    public void showForegroundNotification() {
        if (SettingPreferenceManager.getIsShowNotificationBar()) {
            int period = getMaskPeriod();
            mainView.showForegroundNotification(period);
        }
    }

    /**
     * 등록되어 있는 알람을 종료하고 새로운 교체하기 알람을 등록한다.
     */
    private void setMaskReplaceNotification() {
        mainView.setMaskReplaceNotification();
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
            return 0;
        }
        return DateUtils.calculateDateGapWithToday(lastReplacement) + 1;
    }
}