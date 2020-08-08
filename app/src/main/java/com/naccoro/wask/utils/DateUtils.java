package com.naccoro.wask.utils;

import java.util.regex.Pattern;

public class DateUtils {
    public static int getMonth(String date) {
        checkDateFormat(date);
        return Integer.parseInt(date.substring(5, 7));
    }

    public static void checkMonthFormat(int month) {
        if (!isLegalMonth(month)) {
            throw new IllegalArgumentException("month parameter should be 0 to 12");
        }
    }

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

    public static String convertMonthIntToString(int month) {
        checkMonthFormat(month);

        StringBuilder newMonth = new StringBuilder();

        if (month < 10) {
            newMonth.append(0);
        }

        newMonth.append(month);

        return newMonth.toString();
    }

    public static boolean isLegalMonth(int month) {
        return month > 0 && month <= 12;
    }

    private static void checkDateFormat(String date) {
        if (!isLegalDate(date)) {
            throw new IllegalArgumentException("date parameter should be YYYY-MM-DD format");
        }
    }

    private static boolean isLegalDate(String date) {
        return Pattern.matches("^\\d{4}-(0[1-9]|1[012])-(0[1-9]|[12][0-9]|3[01])$", date);
    }
}
