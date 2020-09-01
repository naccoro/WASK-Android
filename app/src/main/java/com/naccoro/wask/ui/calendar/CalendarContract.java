package com.naccoro.wask.ui.calendar;

import java.util.ArrayList;
import com.naccoro.wask.ui.calendar.CalendarActivity.Date;

public interface CalendarContract {
    interface View {
        void finishCalendarView();
        void showCalendarDateTextView(int month);
        void initCalendarList(ArrayList<CalendarItem> calendarItems);
        void showModifyModeTextView(boolean isChecked);
    }

    interface Presenter {
        void clickBackButton();
        void clickChangeDateButton(Date selectDate);
        void changeCalendarList(Date selectDate);
        void changeCalendarDateTextView(Date selectDate);
        void changeModifyMode(boolean isChecked, CalendarAdapter calendarAdapter);
    }
}
