package com.naccoro.wask.calendar;

import com.naccoro.wask.calendar.CalendarActivity.Date;

public class CalendarPresenter implements CalendarContract.Presenter {

    CalendarContract.View calendarView;
    CalendarModel calendarModel;

    CalendarPresenter(CalendarContract.View calendarView) {
        this.calendarView = calendarView;
        this.calendarModel = new CalendarModel();
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
        changeCalendarDateTextView(selectDate);
    }


    @Override
    public void changeCalendarList(Date selectDate) {
        calendarView.initCalendarList(calendarModel.updateCalendarList(selectDate));
    }

    @Override
    public void changeCalendarDateTextView(Date selectDate) {
        int month = selectDate.getMonth() + 1;
        calendarView.showCalendarDateTextView(month);
    }

    @Override
    public void changeModifyMode(boolean isChecked) {
        if (isChecked) {
            // 수정가능하게 바꾸어야 함

        } else {
            // 수정불가
        }
        calendarView.showModifyModeTextView(isChecked);
    }

}
