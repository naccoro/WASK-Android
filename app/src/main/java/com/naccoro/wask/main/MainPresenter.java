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

        if (period == 0) {
            //Todo: 교체 기록이 없을 경우 로직 고안
            Log.d(TAG, "start: No replacement data");
        } else if (period > 1) {
            mainView.showBadMainView();
        } else {
            mainView.showGoodMainView();
        }

        setIsChanged();
    }

    /**
     * 오늘 마스크를 교체하였는지 확인 후 버튼 상태를 변경
     */
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

    /**
     * 교체 취소 다이얼로그에서 확인을 눌렀을 때 호출되는 함수
     */
    @Override
    public void cancelChanging() {
        replacementHistoryRepository.deleteToday();
        start();
    }

    /**
     * WaskDatabase에서 현재 마스크 교체 상태를 가져오는 함수
     *
     * @return [오늘 날짜 - 마지막 교체 일자 + 1]
     */
    private int getMaskPeriod() {
        String lastReplacement = replacementHistoryRepository.getLastReplacement();
        if (lastReplacement == null) {
            //교체 기록이 없을 경우
            return 0;
        }
        return DateUtils.getTodayToInt() - DateUtils.getDateToInt(replacementHistoryRepository.getLastReplacement()) + 1;
    }
}
