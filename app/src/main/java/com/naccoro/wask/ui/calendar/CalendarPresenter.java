package com.naccoro.wask.ui.calendar;

import com.naccoro.wask.replacement.repository.ReplacementHistoryRepository;

public class CalendarPresenter implements CalendarContract.Presenter {

    CalendarContract.View calendarView;
    CalendarModel calendarModel;

    private int prevMonth; // 기존 month저장

    CalendarPresenter(CalendarContract.View calendarView, ReplacementHistoryRepository replacementHistoryRepository) {
        this.calendarView = calendarView;
        this.calendarModel = new CalendarModel(replacementHistoryRepository);
        prevMonth = -1; // 처음엔 기존 month가 없음
    }

    @Override
    public void clickBackButton() {
        calendarView.finishCalendarView();
    }

    /**
     * 1. calendar list 갱신 (selectDate 기준으로)
     * 2. 화면 갱신
     *
     * @param selectDate
     */
    @Override
    public void clickChangeDateButton(Date selectDate) {
        changeCalendarList(selectDate);
    }

    /**
     * 화면에 표시되는 달력 데이터 변경
     *
     * @param selectDate
     */
    @Override
    public void changeCalendarList(Date selectDate) {
        calendarModel.updateCalendarList(selectDate, dateList -> calendarView.initCalendarList(dateList));
    }

    /**
     * 수정모드 ON/OFF
     *
     * @param isChecked
     */
    @Override
    public void changeModifyMode(boolean isChecked, CalendarAdapter calendarAdapter) {
        calendarAdapter.setModifyMode(isChecked);
        calendarView.showModifyModeTextView(isChecked);
    }

}
