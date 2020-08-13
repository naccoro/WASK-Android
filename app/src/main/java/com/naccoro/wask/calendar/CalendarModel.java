package com.naccoro.wask.calendar;

import android.icu.util.Calendar;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import com.naccoro.wask.calendar.CalendarActivity.Date;


public class CalendarModel {

    private Date selectDate;

    /**
     * 화면에 표시되는 달력 데이터 설정
     *
     * @param selectDate    선택된 날짜 (default: today)
     * @return  갱신된 날짜가 저장된 리스트
     */
    public ArrayList<CalendarItem> updateCalendarList(Date selectDate) {
        ArrayList<CalendarItem> dateList = new ArrayList<CalendarItem>();
        dateList.clear();
        this.selectDate = selectDate;

        try {
            GregorianCalendar calendar = new GregorianCalendar(selectDate.getYear(), selectDate.getMonth(), 1, 0, 0, 0);

            int startDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1; // 해당 월에 시작하는 요일
            int lastDayOfMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH); // 이번 달의 말일

            updateLastMonth(dateList, calendar, startDayOfWeek);
            updateCurrentMonth(dateList, calendar, lastDayOfMonth);
            updateNextMonth(dateList, calendar, startDayOfWeek, lastDayOfMonth);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return dateList;
    }

    /**
     * 지난 달 데이터 설정
     *
     * @param dateList  데이터의 리스트
     * @param calendar  오늘의 year, month가 저장된 달력객체
     * @param startDayOfWeek    이번달의 시작 요일 -> 필요한 지난 달의 개수를 알 수 있다.
     */
    private void updateLastMonth(ArrayList<CalendarItem> dateList, GregorianCalendar calendar, int startDayOfWeek) {
        calendar.add(Calendar.MONTH, - 1);
        int lastDayOfPreMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

        for (int j = startDayOfWeek - 1; j >= 0; j--) {
            GregorianCalendar item = new GregorianCalendar(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), lastDayOfPreMonth - j);
            dateList.add(new CalendarItem(false, false, item));
        }
    }

    /**
     * 이번 달 데이터 설정
     *
     * @param dateList  데이터의 리스트
     * @param calendar  오늘의 year, month가 저장된 달력객체
     * @param lastDayOfMonth    이번달의 마지막 날짜
     */
    private void updateCurrentMonth(ArrayList<CalendarItem> dateList, GregorianCalendar calendar, int lastDayOfMonth) {
        calendar.add(Calendar.MONTH, + 1);
        for (int j = 1; j <= lastDayOfMonth; j++) {
            GregorianCalendar item = new GregorianCalendar(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), j);
            if (selectDate.getDay() == j) {
                // 선택일자와 같으면 select 체크
                dateList.add(new CalendarItem(true, true, item));
            } else {
                dateList.add(new CalendarItem(false, true, item));
            }
        }
    }

    /**
     * 다음 달 데이터 설정
     *
     * @param dateList  데이터의 리스트
     * @param calendar  오늘의 year, month가 저장된 달력객체
     * @param startDayOfWeek    이번달의 시작 요일
     * @param lastDayOfMonth    이번달의 마지막 날짜
     * nextMonthDay를 구하기 위함. (필요한 다음 달 날짜의 개수)
     */
    private void updateNextMonth(ArrayList<CalendarItem> dateList, GregorianCalendar calendar, int startDayOfWeek, int lastDayOfMonth) {
        calendar.add(Calendar.MONTH, + 1); // 다음달
        int nextMonthDay = 42 - startDayOfWeek - lastDayOfMonth;
        for (int j = 1; j <= nextMonthDay; j++) {
            GregorianCalendar item = new GregorianCalendar(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, j);
            dateList.add(new CalendarItem(false, false, item));
        }
    }

}
