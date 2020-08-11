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

    @Override
    public void clickSettingButton() {
        mainView.showSettingView();
    }

    @Override
    public void clickCalendarButton() {
        mainView.showCalendarView();
    }

    @Override
    public void clickReplaceButton() {
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
    }
}
