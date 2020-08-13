package com.naccoro.wask.calendar;

import com.naccoro.wask.calendar.CalendarActivity.Date;

public class CalendarPresenter implements CalendarContract.Presenter {

    CalendarContract.View calendarView;
    CalendarModel calendarModel;

    private int prevMonth; // 기존 month저장

    CalendarPresenter(CalendarContract.View calendarView) {
        this.calendarView = calendarView;
        this.calendarModel = new CalendarModel();
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
        calendarView.initSelectDate(selectDate);
        changeCalendarList(selectDate);
        changeCalendarDateTextView(selectDate);
    }

    /**
     * 화면에 표시되는 달력 데이터 변경
     *
     * @param selectDate
     */
    @Override
    public void changeCalendarList(Date selectDate) {
        if (selectDate.getMonth() == prevMonth) {
            return; // 기존 month와 새로운 month가 같으면 data갱신할 필요 X
        }
        prevMonth = selectDate.getMonth(); // 기존 (감사할)월 갱신
        calendarView.initCalendarList(calendarModel.updateCalendarList(selectDate));
    }

    /**
     * 0000년 00월 표시
     *
     * @param selectDate
     */
    @Override
    public void changeCalendarDateTextView(Date selectDate) {
        int month = selectDate.getMonth() + 1;
        calendarView.showCalendarDateTextView(month);
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
