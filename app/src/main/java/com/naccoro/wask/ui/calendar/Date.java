package com.naccoro.wask.ui.calendar;

import java.io.Serializable;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * SelectDate를 저장하기 위한 클래스 (year, month, day 저장)
 */
@SuppressWarnings("serial")
public class Date implements Serializable {
    private int year;
    private int month;
    private int day;

    public Date(int year, int month, int day) {
        this.year = year;
        this.month = month;
        this.day = day;
    }

    public void setDate(int year, int month, int day) {
        this.year = year;
        this.month = month;
        this.day = day;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public boolean isSameDate(GregorianCalendar cal) {
        if (cal.get(Calendar.YEAR) != year) {
            return false;
        }
        if (cal.get(Calendar.MONTH) != month) {
            return false;
        }
        if (cal.get(Calendar.DATE) != day) {
            return false;
        }
        return true;
    }

    public void setNextMonth() {
        if (getMonth() != 11) {
            setMonth(month+1);
        }
        else {
            setMonth(0);
            setYear(year+1);
        }
    }

    public void setPrevMonth() {
        if (getMonth() != 0) {
            setMonth(month-1);
        }
        else {
            setMonth(11);
            setYear(year-1);
        }
    }
}