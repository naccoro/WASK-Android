package com.naccoro.wask.calendar;

public class CalendarPresenter implements CalendarContract.Presenter {

    CalendarActivity calendarView;

    CalendarPresenter(CalendarActivity calendarView) {
        this.calendarView = calendarView;
    }

    @Override
    public void clickBackButton() {
        calendarView.finishCalendarView();
    }
}
