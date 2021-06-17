package com.naccoro.wask.ui.calendar;

import android.util.Log;

import java.util.Calendar;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

import com.naccoro.wask.replacement.repository.ReplacementHistoryRepository;
import com.naccoro.wask.utils.DateUtils;


public class CalendarModel {

    private static final String TAG = "CalendarModel";

    private Date selectDate;
    private int prevPosition; // 이전 position 저장

    private int cachingMonth;

    private ReplacementHistoryRepository replacementHistoryRepository;

    private List<Integer> replacementHistories;

    public CalendarModel(ReplacementHistoryRepository replacementHistoryRepository) {
        this.replacementHistoryRepository = replacementHistoryRepository;
        this.prevPosition = 0;
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

                    if (callback != null) {
                        callback.onCalendarDateLoaded(updateCalendarList());
                    }
                }

                @Override
                public void onDataNotAvailable() {
                    Log.d(TAG, "onDataNotAvailable: No data");
                    replacementHistories = null;

                    if (callback != null) {
                        callback.onCalendarDateLoaded(updateCalendarList());
                    }
                }
            });
        } else {
            Log.d(TAG, "initReplacementHistoriesAsDate: Already latest data");
            callback.onCalendarDateLoaded(updateCalendarList());
        }
    }

    /**
     * @return (이전+현재+다음) 달력 데이터
     */
    private ArrayList<DayItem> updateCalendarList() {
        ArrayList<DayItem> dayItems = new ArrayList<>();
        dayItems.clear();

        try {
            GregorianCalendar calendar = new GregorianCalendar(selectDate.getYear(), selectDate.getMonth(), 1, 0, 0, 0);

            // 해당 월에 시작하는 요일의 위치 1 : 일요일 ~ 7 : 토요일
            int startDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1;
            if (startDayOfWeek == 0) startDayOfWeek = 7;

            int lastDayOfMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH); // 이번 달의 말일

            updateLastMonth(dayItems, calendar, startDayOfWeek);
            updateCurrentMonth(dayItems, calendar, lastDayOfMonth, selectDate);
            updateNextMonth(dayItems, calendar, startDayOfWeek, lastDayOfMonth);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return dayItems;
    }

    /**
     * 지난 달 데이터 설정
     *
     * @param dayItems  데이터의 리스트
     * @param calendar  오늘의 year, month가 저장된 달력객체
     * @param startDayOfWeek    이번달의 시작 요일 -> 필요한 지난 달의 개수를 알 수 있다. (7개가 필요하면 생략)
     */
    private void updateLastMonth(ArrayList<DayItem> dayItems, GregorianCalendar calendar, int startDayOfWeek) {
        if (startDayOfWeek == 7) return;

        boolean isChangedMask;
        Calendar prevCalendar = new GregorianCalendar(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) - 1, 1);
        int lastDayOfPreMonth = prevCalendar.getActualMaximum(Calendar.DAY_OF_MONTH);

        for (int j = 0; j < startDayOfWeek; j++) {
            GregorianCalendar item = new GregorianCalendar(prevCalendar.get(Calendar.YEAR), prevCalendar.get(Calendar.MONTH), lastDayOfPreMonth - j);
            isChangedMask = replacementHistories.contains(DateUtils.getDateFromGregorianCalendar(item));

            dayItems.add(0, new DayItem(isChangedMask,false, item));
        }

    }

    /**
     * 이번 달 데이터 설정
     *  @param dayItems  데이터의 리스트
     * @param calendar  오늘의 year, month가 저장된 달력객체
     * @param lastDayOfMonth    이번달의 마지막 날짜
     * @param selectDate    선택한 날짜
     */
    private void updateCurrentMonth(ArrayList<DayItem> dayItems, GregorianCalendar calendar, int lastDayOfMonth, Date selectDate) {
        boolean isChangedMask;

        for (int j = 1; j <= lastDayOfMonth; j++) {
            GregorianCalendar item = new GregorianCalendar(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), j);
            isChangedMask = replacementHistories.contains(DateUtils.getDateFromGregorianCalendar(item));

            dayItems.add(new DayItem(isChangedMask, true, item));
        }
    }

    /**
     * 다음 달 데이터 설정
     *
     * @param dayItems  데이터의 리스트
     * @param calendar  오늘의 year, month가 저장된 달력객체
     * @param startDayOfWeek    이번달의 시작 요일
     * @param lastDayOfMonth    이번달의 마지막 날짜
     * 파라미터 : nextMonthDay를 구하기 위함. (필요한 다음 달 날짜의 개수 % 7)
     */
    private void updateNextMonth(ArrayList<DayItem> dayItems, GregorianCalendar calendar, int startDayOfWeek, int lastDayOfMonth) {
        boolean isChangedMask;

        int nextMonthDay = (42 - startDayOfWeek - lastDayOfMonth) % 7;
        for (int j = 1; j <= nextMonthDay; j++) {
            GregorianCalendar item = new GregorianCalendar(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, j);
            isChangedMask = replacementHistories.contains(DateUtils.getDateFromGregorianCalendar(item));

            dayItems.add(new DayItem(isChangedMask, false, item));
        }
    }

    /**
     * viewPager에서 swipe로 인한 position 변화에 따라 Date 변경 (prevPosition 활용)
     * - position 커지면 다음 달, 작아지면 이전 달로 이동
     * - prevPosition이 설정되어있지 않으면, position으로 초기화 됨.
     * @param currentDate 기존에 선택된 날짜 (스와이프 이전)
     * @param position 현재 위치 (스와이프 후)
     * @param callback 변경된 날짜를 반영하기 위한 콜백
     */
    public void updateDateByPosition(Date currentDate, int position, LoadSelectDateCallback callback) {
        this.selectDate = currentDate;

        if (prevPosition < position) {
            selectDate.setNextMonth();
        } else if (prevPosition > position){
            selectDate.setPrevMonth();
        }
        prevPosition = position;

        callback.onSelectDateLoaded(selectDate);
    }

    interface LoadCalendarDateCallback {
        void onCalendarDateLoaded(ArrayList<DayItem> dateList);
    }

    interface LoadSelectDateCallback {
        void onSelectDateLoaded(Date date);
    }
}
