package com.naccoro.wask.ui.calendar;

import java.util.ArrayList;

public interface CalendarContract {
    interface View {
        void finishCalendarView();
        void showCalendarDateTextView();
        void initCalendarList(ArrayList<DayItem> dayItems);
        void showModifyModeTextView(boolean isChecked);
    }

    interface Presenter {
        void clickBackButton();
        void clickChangeDateButton(Date selectDate);
        void changeCalendarList(Date selectDate);
        void changeModifyMode(boolean isChecked, DayAdapter dayAdapter);
    }
}
