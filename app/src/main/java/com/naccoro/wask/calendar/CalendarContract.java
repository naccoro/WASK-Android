package com.naccoro.wask.calendar;

import java.util.ArrayList;
import java.util.GregorianCalendar;

public interface CalendarContract {
    interface View {
        void finishCalendarView();
        void showCalendarDateTextView(int month);
        void initCalendarList(ArrayList<CalendarItem> calendarItems);
    }

    interface Presenter {
        void clickBackButton();
        void clickChangeDateButton();
        void changeCalendarList(GregorianCalendar selectDate);
        void changeCalendarDateTextView(GregorianCalendar calendar);
    }
}
