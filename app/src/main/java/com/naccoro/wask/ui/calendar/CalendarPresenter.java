package com.naccoro.wask.ui.calendar;

import com.naccoro.wask.replacement.repository.ReplacementHistoryRepository;

public class CalendarPresenter implements CalendarContract.Presenter {

    CalendarContract.View calendarView;
    CalendarModel calendarModel;

    CalendarPresenter(CalendarContract.View calendarView, ReplacementHistoryRepository replacementHistoryRepository) {
        this.calendarView = calendarView;
        this.calendarModel = new CalendarModel(replacementHistoryRepository);
    }

    @Override
    public void clickBackButton() {
        calendarView.finishCalendarView();
    }

    /**
     * 캘린더(날짜버튼, 달력 viewPager) contents 설
     * 1. calendar list 갱신 (selectDate 기준으로)
     * 2. 화면 갱신 (날짜버튼, 달력 viewPager)
     *
     * @param selectDate
     */
    @Override
    public void setCalendar(Date selectDate) {
        calendarView.showCalendarViewPager(selectDate);
        calendarView.showCalendarDateTextView();
    }

    /**
     * 수정모드 ON/OFF
     *
     * @param isChecked
     */
    @Override
    public void changeModifyMode(boolean isChecked, DayAdapter dayAdapter) {
        dayAdapter.setModifyMode(isChecked);
        calendarView.showModifyModeTextView(isChecked);
    }

}
