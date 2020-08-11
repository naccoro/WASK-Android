package com.naccoro.wask;

import com.naccoro.wask.utils.DateUtils;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class DateUtilsTest {
    @Test
    public void regularExpression_isCorrect() {
        assertThat(DateUtils.getMonth("2020-03-11"), is(3));
        assertThat(DateUtils.getMonth("2021-06-21"), is(6));
        assertThat(DateUtils.getMonth("2019-12-31"), is(12));
        assertThat(DateUtils.getMonth("2020-01-01"), is(1));
    }

    @Test
    public void updateMonth_isCorrect() {
        assertThat(DateUtils.replaceMonthOfDateFormat("2020-01-12", 4), is("2020-04-12"));
        assertThat(DateUtils.replaceMonthOfDateFormat("2020-04-14", 1), is("2020-01-14"));
        assertThat(DateUtils.replaceMonthOfDateFormat("2020-12-01", 5), is("2020-05-01"));
        assertThat(DateUtils.replaceMonthOfDateFormat("2020-02-22", 12), is("2020-12-22"));
        assertThat(DateUtils.replaceMonthOfDateFormat("2020-11-02", 12), is("2020-12-02"));
    }

    @Test
    public void getTodayString_isCorrect() {
        assertThat(DateUtils.getToday(), is("2020-08-11"));
    }

    @Test
    public void getDateToInt_isCorrect() {
        assertThat(DateUtils.getDateToInt("2020-08-12"), is(20200812));
        assertThat(DateUtils.getDateToInt("2022-10-10"), is(20221010));
        assertThat(DateUtils.getDateToInt("2010-12-31"), is(20101231));
    }
}
