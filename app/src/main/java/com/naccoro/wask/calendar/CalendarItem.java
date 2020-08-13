package com.naccoro.wask.calendar;

import java.util.GregorianCalendar;

public class CalendarItem {
    private boolean isSelect; // 오늘인지
    private boolean isChangeMask; // 마스크 교체했는지
    private boolean isCurrentMonth; // 이번달인지

    private GregorianCalendar date; // 날짜 데이터

    public CalendarItem(boolean isSelect, boolean isCurrentMonth, GregorianCalendar date) {
        this.isSelect = isSelect;
        this.isChangeMask = false;
        this.isCurrentMonth = isCurrentMonth;
        this.date = date;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setToday(boolean today) {
        isSelect = today;
    }

    public boolean isChangeMask() {
        return isChangeMask;
    }

    public void setChangeMask(boolean changeMask) {
        isChangeMask = changeMask;
    }

    public boolean isCurrentMonth() {
        return isCurrentMonth;
    }

    public void setCurrentMonth(boolean currentMonth) {
        isCurrentMonth = currentMonth;
    }

    public GregorianCalendar getDate() {
        return date;
    }

    public void setDate(GregorianCalendar date) {
        this.date = date;
    }
}
