package com.naccoro.wask.utils;

import java.util.regex.Pattern;

public class DateUtils {
    public static int getMonth(String date) {
        checkDateFormat(date);
        return Integer.parseInt(date.substring(5, 7));
    }

    private static void checkDateFormat(String date) {
        if (!isLegalArgument(date)) {
            throw new IllegalArgumentException("date parameter should be YYYY-MM-DD format");
        }
    }

    private static boolean isLegalArgument(String date) {
        return Pattern.matches("^\\d{4}-(0[1-9]|1[012])-(0[1-9]|[12][0-9]|3[01])$", date);
    }
}
