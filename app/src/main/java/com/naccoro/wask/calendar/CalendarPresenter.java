package com.naccoro.wask.calendar;

public class CalendarPresenter implements CalendarContract.Presenter {

    CalendarContract.View calendarView;

    CalendarPresenter(CalendarContract.View calendarView) {
        this.calendarView = calendarView;
    }

    @Override
    public void clickBackButton() {
        calendarView.finishCalendarView();
    }
}
