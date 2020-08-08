package com.naccoro.wask;

import com.naccoro.wask.utils.DateUtils;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class RegularExpressionTest {
    @Test
    public void regularExpression_isCorrect() {
        assertThat(DateUtils.getMonth("2020-03-11"), is(3));
        assertThat(DateUtils.getMonth("2021-06-21"), is(6));
        assertThat(DateUtils.getMonth("2019-12-31"), is(12));
        assertThat(DateUtils.getMonth("2020-01-01"), is(1));
    }
}
