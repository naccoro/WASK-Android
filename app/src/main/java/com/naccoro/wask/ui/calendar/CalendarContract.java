package com.naccoro.wask.ui.calendar;

public interface CalendarContract {
    interface View {
        void finishCalendarView();
        void showCalendarDateTextView();
        void showCalendarViewPager(Date selectDate);
        void showModifyModeTextView(boolean isChecked);
    }

    interface Presenter {
        void clickBackButton();
        void setCalendar(Date selectDate);
        void scrolledPage(Date selectDate, int position);
        void changeModifyMode(boolean isChecked, DayAdapter dayAdapter);
    }
}
