package com.naccoro.wask.calendar;

import android.icu.util.Calendar;
import android.util.Log;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

import com.naccoro.wask.calendar.CalendarActivity.Date;
import com.naccoro.wask.replacement.repository.ReplacementHistoryRepository;
import com.naccoro.wask.utils.DateUtils;


public class CalendarModel {

    private static final String TAG = "CalendarModel";

    private Date selectDate;

    private int cachingMonth;

    private ReplacementHistoryRepository replacementHistoryRepository;

    private List<Integer> replacementHistories;

    public CalendarModel(ReplacementHistoryRepository replacementHistoryRepository) {
        this.replacementHistoryRepository = replacementHistoryRepository;
    }

    /**
     * 화면에 표시되는 달력 데이터 설정
     *
     * @param selectDate    선택된 날짜 (default: today)
     * @param callback      선택된 날짜 전후로 3달 간의 일자 데이터 리스트를 처리하는 콜백
     */
    public void updateCalendarList(Date selectDate, LoadCalendarDateCallback callback) {
        this.selectDate = selectDate;

        if (replacementHistories == null || cachingMonth != selectDate.getMonth() + 1) {
            replacementHistoryRepository.getDateAroundThreeMonth(selectDate.getMonth() + 1, new ReplacementHistoryRepository.LoadHistoriesAsDateCallback() {
                @Override
                public void onHistoriesLoaded(List<Integer> histories) {
                    Log.d(TAG, "onHistoriesLoaded: Success");
                    cachingMonth = selectDate.getMonth() + 1;
                    replacementHistories = histories;

                    callback.onCalendarDateLoaded(updateCalendarList());
                }

                @Override
                public void onDataNotAvailable() {
                    Log.d(TAG, "onDataNotAvailable: No data");
                    replacementHistories = null;

                    callback.onCalendarDateLoaded(updateCalendarList());
                }
            });
        } else {
            Log.d(TAG, "initReplacementHistoriesAsDate: Already latest data");
            callback.onCalendarDateLoaded(updateCalendarList());
        }
    }

    private ArrayList<CalendarItem> updateCalendarList() {
        ArrayList<CalendarItem> dateList = new ArrayList<>();
        dateList.clear();

        try {
            GregorianCalendar calendar = new GregorianCalendar(selectDate.getYear(), selectDate.getMonth(), 1, 0, 0, 0);

            int startDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1; // 해당 월에 시작하는 요일
            int lastDayOfMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH); // 이번 달의 말일

            updateLastMonth(dateList, calendar, startDayOfWeek);
            updateCurrentMonth(dateList, calendar, lastDayOfMonth, selectDate);
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
        boolean isChangedMask;

        int lastDayOfPreMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

        for (int j = startDayOfWeek - 1; j >= 0; j--) {
            GregorianCalendar item = new GregorianCalendar(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) - 1, lastDayOfPreMonth - j);
            isChangedMask = replacementHistories.contains(DateUtils.getDateFromGregorianCalendar(item));

            dateList.add(new CalendarItem(false, isChangedMask,false, item));
        }

    }

    /**
     * 이번 달 데이터 설정
     *  @param dateList  데이터의 리스트
     * @param calendar  오늘의 year, month가 저장된 달력객체
     * @param lastDayOfMonth    이번달의 마지막 날짜
     * @param selectDate    선택한 날짜
     */
    private void updateCurrentMonth(ArrayList<CalendarItem> dateList, GregorianCalendar calendar, int lastDayOfMonth, Date selectDate) {
        boolean isChangedMask;

        for (int j = 1; j <= lastDayOfMonth; j++) {
            GregorianCalendar item = new GregorianCalendar(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), j);
            isChangedMask = replacementHistories.contains(DateUtils.getDateFromGregorianCalendar(item));

            if (selectDate.getDay() == j) {
                // 선택일자와 같으면 select 체크
                dateList.add(new CalendarItem(true, isChangedMask, true, item));
            } else {
                dateList.add(new CalendarItem(false, isChangedMask, true, item));
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
        boolean isChangedMask;

        int nextMonthDay = 42 - startDayOfWeek - lastDayOfMonth;
        for (int j = 1; j <= nextMonthDay; j++) {
            GregorianCalendar item = new GregorianCalendar(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, j);
            isChangedMask = replacementHistories.contains(DateUtils.getDateFromGregorianCalendar(item));

            dateList.add(new CalendarItem(false, isChangedMask, false, item));
        }
    }

    interface LoadCalendarDateCallback {
        void onCalendarDateLoaded(ArrayList<CalendarItem> dateList);
    }
}
