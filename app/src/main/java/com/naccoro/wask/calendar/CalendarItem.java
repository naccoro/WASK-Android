package com.naccoro.wask.calendar;

import java.util.GregorianCalendar;

/**
 * 캘린더에 표시되는 '일(Day)'이 하나씩 들어가는 객체
 */
public class CalendarItem {
    private boolean isSelect; // 오늘인지
    private boolean isChangeMask; // 마스크 교체했는지
    private boolean isCurrentMonth; // 이번달인지

    private GregorianCalendar date; // 날짜 데이터

    public CalendarItem(boolean isSelect, boolean isChangeMask, boolean isCurrentMonth, GregorianCalendar date) {
        this.isSelect = isSelect;
        this.isChangeMask = isChangeMask;
        this.isCurrentMonth = isCurrentMonth;
        this.date = date;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
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
