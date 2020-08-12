package com.naccoro.wask;

import com.naccoro.wask.utils.DateUtils;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class DateUtilsTest {
    @Test
    public void regularExpression_isCorrect() {
        assertThat(DateUtils.getMonth(20200311), is(3));
        assertThat(DateUtils.getMonth(20210621), is(6));
        assertThat(DateUtils.getMonth(20191231), is(12));
        assertThat(DateUtils.getMonth(20200101), is(1));
    }

    @Test
    public void updateMonth_isCorrect() {
        assertThat(DateUtils.replaceMonthOfDateFormat(20200112, 4), is(20200412));
        assertThat(DateUtils.replaceMonthOfDateFormat(20200414, 1), is(20200114));
        assertThat(DateUtils.replaceMonthOfDateFormat(20201201, 5), is(20200501));
        assertThat(DateUtils.replaceMonthOfDateFormat(20200222, 12), is(20201222));
        assertThat(DateUtils.replaceMonthOfDateFormat(20201102, 12), is(20201202));
    }

    @Test
    public void getTodayString_isCorrect() {
        assertThat(DateUtils.getToday(), is(20200813));
    }
}
