package com.naccoro.wask.main;

import android.util.Log;

import com.naccoro.wask.replacement.repository.ReplacementHistoryRepository;

public class MainPresenter implements MainContract.Presenter {
    private static final String TAG = "MainPresenter";

    private ReplacementHistoryRepository replacementHistoryRepository;

    MainActivity mainView;

    MainPresenter(MainActivity mainView, ReplacementHistoryRepository replacementHistoryRepository) {
        this.mainView = mainView;
        this.replacementHistoryRepository = replacementHistoryRepository;
    }

    /**
     * MainView에서 보여줘야 할 Data를 가져오는 함수
     */
    @Override
    public void start() {
        int period = getMaskPeriod();
        mainView.setPeriodTextValue(period);

        if (period > 1) {
            mainView.showBadMainView();
        } else {
            mainView.showGoodMainView();
        }
    }

    /**
     * WaskDatabase에서 현재 마스크 교체 상태를 가져오는 함수
     *
     * @return [오늘 날짜 - 마지막 교체 일자]
     */
    private int getMaskPeriod() {
        //TODO: WaskDatabase에서 교체일자를 비교하여 현재 상태를 가져온다.
        return 3;
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
            replacementHistoryRepository.insertToday(new ReplacementHistoryRepository.InsertHistoryCallback() {
                @Override
                public void onSuccess() {
                    mainView.showReplaceToast();
                    mainView.disableReplaceButton();
                }

                @Override
                public void onDuplicated() {
                    Log.d(TAG, "onDuplicated: true");
                }
            });
        start();
    }
}
