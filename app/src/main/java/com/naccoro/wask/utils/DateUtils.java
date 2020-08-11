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
     * 오늘 날짜를 YYYY-MM-DD 형식으로 가져오기
     * @return YYYY-MM-DD 형식의 오늘 날짜
     */
    public static String getToday() {
        GregorianCalendar calendar = new GregorianCalendar();
        return getDateFromGregorianCalendar(calendar);
    }

    /**
     * 오늘 날짜를 YYYYMMDD 정수형으로 가져오기
     * @return YYYYMMDD 정수형의 오늘 날짜
     */
    public static int getTodayToInt() {
        return getDateToInt(getToday());
    }

    /**
     * 입력받은 YYYY-MM-DD 형태의 날짜를 정수형으로 전환
     * @param date 정수형으로 바꿀 YYYY-MM-DD 문자열
     * @return 정수형의 YYYY-MM-DD 문자열
     */
    public static int getDateToInt(String date) {
        checkDateFormat(date);
        return Integer.parseInt(date.replaceAll("-", ""));
    }

    /**
     * GregorianCalendar를 이용하여 YYYY-MM-DD 형태의 날짜 구하기
     * @param calendar 날짜를 계산할 GregorianCalendar
     * @return YYYY-MM-DD 형태의 GregorianCalendar가 가진 날짜
     */
    public static String getDateFromGregorianCalendar(GregorianCalendar calendar) {
        return calendar.get(Calendar.YEAR) + "-" +
                convertMonthIntToString(calendar.get(Calendar.MONTH) + 1) + "-" +
                calendar.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * YYYY-MM-DD 포멧에서 "월"에 해당하는 정보를 리턴
     * @param date "월" 정보를 조회할 YYYY-MM-DD 문자열
     * @return int 타입의 월
     */
    public static int getMonth(String date) {
        checkDateFormat(date);
        return Integer.parseInt(date.substring(5, 7));
    }

    /**
     * 입력받은 '월" 정보의 유효성 검사
     * @param month 감사할 월
     */
    public static void checkMonthFormat(int month) {
        if (!isLegalMonth(month)) {
            throw new IllegalArgumentException("month parameter should be 0 to 12");
        }
    }

    /**
     * YYYY-MM-DD 포멧에서 월 정보를 수정
     * @param date 수정할 대상 YYYY-MM-DD 문자열
     * @param month 수정할 월
     * @return 수정된 YYYY-MM-DD 문자열
     */
    public static String replaceMonthOfDateFormat(String date, int month) {
        DateUtils.checkMonthFormat(month);

        String[] dateSplitArray = date.split("-");

        StringBuilder newReplaceDate = new StringBuilder()
                .append(dateSplitArray[0])
                .append("-")
                .append(DateUtils.convertMonthIntToString(month))
                .append("-")
                .append(dateSplitArray[2]);

        return newReplaceDate.toString();
    }

    /**
     * int 타입의 월을 MM 형태의 문자열로 변환
     * @param month 변환할 int 형태의 월
     * @return MM 형태로 변환된 문자열
     */
    public static String convertMonthIntToString(int month) {
        checkMonthFormat(month);

        StringBuilder newMonth = new StringBuilder();

        if (month < 10) {
            newMonth.append(0);
        }

        newMonth.append(month);

        return newMonth.toString();
    }

    /**
     * 입력받은 '월" 정보의 유효성 검사하여 리턴
     * @param month 감사할 월
     * @return 유효한지 아닌지 boolean 형태로 리턴
     */
    public static boolean isLegalMonth(int month) {
        return month > 0 && month <= 12;
    }

    /**
     * 입력받은 YYYY-MM-DD 문자열의 형태가 유효한지 검사
     * @param date 검사할 문자열
     */
    private static void checkDateFormat(String date) {
        if (!isLegalDate(date)) {
            throw new IllegalArgumentException("date parameter should be YYYY-MM-DD format");
        }
    }

    /**
     * 입력받은 YYYY-MM-DD 문자열의 형태가 유효한지 검사하여 리턴
     * @param date 검사할 문자열
     * @return 유효한지 아닌지 boolean 형태로 리턴
     */
    private static boolean isLegalDate(String date) {
        return Pattern.matches("^\\d{4}-(0[1-9]|1[012])-(0[1-9]|[12][0-9]|3[01])$", date);
    }
}
