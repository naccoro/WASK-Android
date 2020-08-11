package com.naccoro.wask.main;

import android.util.Log;

import com.naccoro.wask.replacement.model.ReplacementHistory;
import com.naccoro.wask.replacement.repository.ReplacementHistoryRepository;
import com.naccoro.wask.utils.DateUtils;

public class MainPresenter implements MainContract.Presenter {
    private static final String TAG = "MainPresenter";

    private ReplacementHistoryRepository replacementHistoryRepository;

    private boolean isChanged = false;

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

        setIsChanged();
    }

    private void setIsChanged() {
        replacementHistoryRepository.get(DateUtils.getToday(), new ReplacementHistoryRepository.GetHistoriesCallback() {
            @Override
            public void onTaskLoaded(ReplacementHistory history) {
                isChanged = true;
                mainView.disableReplaceButton();
            }

            @Override
            public void onDataNotAvailable() {
                isChanged = false;
                mainView.enableReplaceButton();
            }
        });
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
        if (!isChanged) {
            replacementHistoryRepository.insertToday(new ReplacementHistoryRepository.InsertHistoryCallback() {
                @Override
                public void onSuccess() {
                    mainView.showReplaceToast();
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

    @Override
    public void cancelChanging() {
        isChanged = false;
        replacementHistoryRepository.deleteToday();
        mainView.enableReplaceButton();
    }
}
