package com.naccoro.wask.calendar;

public interface CalendarContract {
    interface View {
        void finishCalendarView();
    }

    interface Presenter {
        void clickBackButton();
    }
}
