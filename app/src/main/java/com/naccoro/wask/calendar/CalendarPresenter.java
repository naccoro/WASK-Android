package com.naccoro.wask.calendar;

import android.icu.util.Calendar;

import java.util.GregorianCalendar;

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

    @Override
    public void clickChangeDateButton() {
        // date picker 실행
        // selectDate 바꾸고
//        calendarModel.setCalendarList();
    }

    @Override
    public void changeCalendarList(GregorianCalendar selectDate) {
        calendarView.initCalendarList(calendarModel.updateCalendarList(selectDate));
    }

    @Override
    public void changeCalendarDateTextView(GregorianCalendar calendar) {
        int month = calendar.get(Calendar.MONTH) + 1;
        calendarView.showCalendarDateTextView(month);
    }

}
