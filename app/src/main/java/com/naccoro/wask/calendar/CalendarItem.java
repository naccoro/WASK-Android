package com.naccoro.wask.calendar;

import java.util.GregorianCalendar;

public class CalendarItem {
    private boolean isToday; // 오늘인지
    private boolean isChangeMask; // 마스크 교체했는지
    private boolean isThisMonth; // 이번달인지

    private GregorianCalendar date; // 날짜 데이터

    public CalendarItem(boolean isToday, boolean isThisMonth, GregorianCalendar date) {
        this.isToday = isToday;
        this.isChangeMask = false;
        this.isThisMonth = isThisMonth;
        this.date = date;
    }

    public boolean isToday() {
        return isToday;
    }

    public void setToday(boolean today) {
        isToday = today;
    }

    public boolean isChangeMask() {
        return isChangeMask;
    }

    public void setChangeMask(boolean changeMask) {
        isChangeMask = changeMask;
    }

    public boolean isThisMonth() {
        return isThisMonth;
    }

    public void setThisMonth(boolean thisMonth) {
        isThisMonth = thisMonth;
    }

    public GregorianCalendar getDate() {
        return date;
    }

    public void setDate(GregorianCalendar date) {
        this.date = date;
    }
}
