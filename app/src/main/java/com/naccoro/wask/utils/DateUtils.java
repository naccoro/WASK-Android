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
                convertMonthIntToString(calendar.get(Calendar.MONTH) + 1) +
                calendar.get(Calendar.DAY_OF_MONTH);
        return Integer.parseInt(dateString);
    }

    /**
     * YYYYMMDD 포맷에서 "월"에 해당하는 정보를 리턴
     * @param date "월" 정보를 조회할 YYYYMMDD 정수
     * @return int 타입의 월
     */
    public static int getMonth(int date) {
        String dateString = checkDateFormat(date);
        return Integer.parseInt(dateString.substring(5, 7));
    }

    /**
     * 입력받은 '월" 정보의 유효성 검사
     * @param month 검사할 월
     */
    public static void checkMonthFormat(int month) {
        if (!isLegalMonth(month)) {
            throw new IllegalArgumentException("month parameter should be 0 to 12");
        }
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

        String[] dateSplitArray = {dataString.substring(0, 3), dataString.substring(3, 5), dataString.substring(5, 7)};

        StringBuilder newReplaceDate = new StringBuilder()
                .append(dateSplitArray[0])
                .append(DateUtils.convertMonthIntToString(month))
                .append(dateSplitArray[2]);

        return Integer.parseInt(newReplaceDate.toString());
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
     * 입력받은 YYYYMMDD 문자열의 형태가 유효한지 검사하여 리턴
     * @param date 검사할 문자열
     * @return 유효한지 아닌지 boolean 형태로 리턴
     */
    private static boolean isLegalDate(String date) {
        return Pattern.matches("^\\d{4}(0[1-9]|1[012])(0[1-9]|[12][0-9]|3[01])$", date);
    }
}
