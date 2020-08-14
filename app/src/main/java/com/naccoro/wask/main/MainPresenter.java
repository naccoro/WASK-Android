package com.naccoro.wask.main;

import android.util.Log;

import com.naccoro.wask.replacement.repository.ReplacementHistoryRepository;
import com.naccoro.wask.utils.DateUtils;

public class MainPresenter implements MainContract.Presenter {
  
    private static final String TAG = "MainPresenter";

    private ReplacementHistoryRepository replacementHistoryRepository;

    private boolean isNoData = true;

    private boolean isChanged = false;

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
        mainView.setPeriodTextValue(period);

        if (period == 0) {
            //교체 기록이 없음
            mainView.showNoReplaceData();
            mainView.enableReplaceButton();
        } else if (period > 1) {
            //교체한지 하루 이상 지남
            mainView.showBadMainView();
        } else {
            //교체한 당일
            isChanged = true;
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
    public void changeMask() {
        if (!isChanged) {
            checkIsFirstReplacement();

            replacementHistoryRepository.insertToday(new ReplacementHistoryRepository.InsertHistoryCallback() {
                @Override
                public void onSuccess() {
                    mainView.showReplaceToast();
                    mainView.enableReplaceButton();
                    isChanged = true;
                }

                @Override
                public void onDuplicated() {
                    Log.d(TAG, "onDuplicated: true");
                }
            });
            start();
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
        isChanged = false;
        start();
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

        //20200710
        return DateUtils.calculateDateGapWithToday(lastReplacement) + 1;
    }
}
