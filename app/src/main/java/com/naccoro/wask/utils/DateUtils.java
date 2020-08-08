package com.naccoro.wask.utils;

import java.util.regex.Pattern;

/**
 * Date format 관련 Util
 * @author KIM SEONGGYU
 * @since 2020.08.08
 */
public class DateUtils {
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
