package com.naccoro.wask.utils;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.regex.Pattern;

/**
 * Date format 관련 Util
 * @author KIM SEONGGYU
 * @since 2020.08.08
 */
public class DateUtils {

    /**
     * 오늘 날짜와의 일수 차이를 구함
     * @param date 비교 대상 날짜
     * @return 정수형의 차이 일수
     */
    public static int calculateDateGapWithToday(int date) {
        return calculateDateGap(date, getToday());
    }

    /**
     * 두 일자 간의 일수 차이를 구함
     * @param date 계산할 날짜
     * @param targetDate 비교 대상이 되는 날짜
     * @return 정수형의 차이 일수
     */
    public static int calculateDateGap(int date, int targetDate) {
        GregorianCalendar dateCalendar =
                new GregorianCalendar(getYear(date), getMonth(date), getDay(date), 0, 0);
        GregorianCalendar targetDateCalendar =
                new GregorianCalendar(getYear(targetDate), getMonth(targetDate), getDay(targetDate), 0, 0);

        dateCalendar.set(Calendar.SECOND, 0);
        dateCalendar.set(Calendar.MILLISECOND, 0);

        targetDateCalendar.set(Calendar.SECOND, 0);
        targetDateCalendar.set(Calendar.MILLISECOND, 0);

        long diff = targetDateCalendar.getTimeInMillis() - dateCalendar.getTimeInMillis();

        return (int) (diff / (24 * 60 * 60 * 1000));
    }

    /**
     * 입력받을 날짜를 입력받은 타입을 파싱하여 리턴
     * @param type DateType.YEAR, DateType.MONTH, DateType.DAY 중 하나
     * @param date 파싱할 날짜
     * @return 파싱된 정수형
     */
    private static int getParsedDate(DateType type, int date) {
        switch (type) {
            case YEAR:
                return parseDateToInt(date, 0, 4);

            case MONTH:
                return parseDateToInt(date, 4, 6);

            case DAY:
                return parseDateToInt(date, 6, 8);

            default:
                return -1;
        }
    }

    /**
     * 입력받은 날짜를 파싱
     * @param date 파싱할 날짜
     * @param start 피상할 시작 인덱스
     * @param end 파싱할 마지막 인덱스
     * @return 파싱된 날짜의 정수형
     */
    private static int parseDateToInt(int date, int start, int end) {
        String dateString = checkDateFormat(date);
        return Integer.parseInt(dateString.substring(start, end));
    }

    /**
     * 오늘 날짜를 YYYYMMDD 형식으로 가져오기
     * @return YYYYMMDD 형식의 오늘 날짜
     */
    public static int getToday() {
        GregorianCalendar calendar = new GregorianCalendar();
        return getDateFromGregorianCalendar(calendar);
    }

    /**
     * GregorianCalendar를 이용하여 YYYYMMDD 형태의 날짜 구하기
     * @param calendar 날짜를 계산할 GregorianCalendar
     * @return YYYYMMDD 형태의 GregorianCalendar가 가진 날짜
     */
    public static int getDateFromGregorianCalendar(GregorianCalendar calendar) {
        String dateString = calendar.get(Calendar.YEAR) +
                convertMonthToString(calendar.get(Calendar.MONTH) + 1) +
                convertDayToString(calendar.get(Calendar.DAY_OF_MONTH));
        return Integer.parseInt(dateString);
    }

    /**
     * YYYYMMDD 포맷에서 연도에 해당하는 정보를 리턴
     * @param date 연도 정보를 조회할 YYYYMMDD 정수
     * @return int 타입의 연도
     */
    private static int getYear(int date) {
        return getParsedDate(DateType.YEAR, date);
    }

    /**
     * YYYYMMDD 포맷에서 "월"에 해당하는 정보를 리턴
     * @param date "월" 정보를 조회할 YYYYMMDD 정수
     * @return int 타입의 월
     */
    public static int getMonth(int date) {
        return getParsedDate(DateType.MONTH, date);
    }

    /**
     * YYYYMMDD 포맷에서 "일"에 해당하는 정보를 리턴
     * @param date "일" 정보를 조회할 YYYYMMDD 정수
     * @return int 타입의 일
     */
    private static int getDay(int date) {
        return getParsedDate(DateType.DAY, date);
    }

    /**
     * YYYYMMDD 포맷에서 월 정보를 수정
     * @param date 수정할 대상 YYYYMMDD 정수
     * @param month 수정할 월
     * @return 수정된 YYYYMMDD 정수
     */
    public static int replaceMonthOfDateFormat(int date, int month) {
        DateUtils.checkMonthFormat(month);

        String dataString = String.valueOf(date);

        String[] dateSplitArray = {dataString.substring(0, 4), dataString.substring(4, 6), dataString.substring(6)};

        StringBuilder newReplaceDate = new StringBuilder()
                .append(dateSplitArray[0])
                .append(DateUtils.convertMonthToString(month))
                .append(dateSplitArray[2]);

        return Integer.parseInt(newReplaceDate.toString());
    }

    /**
     * 정수형의 월 값을 문자열로 변경
     * @param month 변경할 월 값
     * @return 월 문자열
     */
    private static String convertMonthToString(int month) {
        checkMonthFormat(month);
        return convertDoubleDigitForm(month);
    }

    /**
     * 정수형의 월 값을 문자열로 변경
     * @param day 변경할 월 값
     * @return 월 문자열
     */
    private static String convertDayToString(int day) {
        checkDayFormat(day);
        return convertDoubleDigitForm(day);
    }

    /**
     * int 타입의 값을 두 자릿수 형태의 문자열로 변환
     * @param value 변환할 int 형태의 값
     * @return 두 자릿수 형태로 변환된 문자열
     */
    private static String convertDoubleDigitForm(int value) {
        StringBuilder newValue = new StringBuilder();

        if (value < 10) {
            newValue.append(0);
        }

        newValue.append(value);

        return newValue.toString();
    }

    /**
     * 입력받은 YYYYMMDD 정수 형태가 유효한지 검사
     * @param date 검사할 정수
     * @return 검사한 문자열
     */
    private static String checkDateFormat(int date) {
        String dateString = String.valueOf(date);
        if (!isLegalDate(dateString)) {
            throw new IllegalArgumentException("date parameter should be YYYYMMDD format");
        }
        return dateString;
    }

    /**
     * 입력받은 '월" 정보의 유효성 검사
     * @param month 검사할 월
     */
    public static void checkMonthFormat(int month) {
        if (!isLegalMonth(month)) {
            throw new IllegalArgumentException("month parameter should be 1 to 12");
        }
    }

    /**
     * 입력받은 '일" 정보의 유효성 검사
     * @param day 검사할 일
     */
    private static void checkDayFormat(int day) {
        if (!isLegalDay(day)) {
            throw new IllegalArgumentException("month parameter should be 1 to 31");
        }
    }

    /**
     * 입력받은 YYYYMMDD 문자열의 형태가 유효한지 검사하여 리턴
     * @param date 검사할 문자열
     * @return 유효한지 아닌지 boolean 형태로 리턴
     */
    private static boolean isLegalDate(String date) {
        return Pattern.matches("^\\d{4}(0[1-9]|1[012])(0[1-9]|[12][0-9]|3[01])$", date);
    }

    /**
     * 입력받은 '월" 정보의 유효성 검사하여 리턴
     * @param month 검사할 월
     * @return 유효한지 아닌지 boolean 형태로 리턴
     */
    private static boolean isLegalMonth(int month) {
        return month > 0 && month <= 12;
    }

    /**
     * 입력받은 '일" 정보의 유효성 검사하여 리턴
     * @param day 검사할 일
     * @return 유효한지 아닌지 boolean 형태로 리턴
     */
    private static boolean isLegalDay(int day) {
        return day > 0 && day <= 31;
    }

    public enum DateType {
        YEAR, MONTH, DAY
    }
}
