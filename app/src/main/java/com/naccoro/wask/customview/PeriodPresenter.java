package com.naccoro.wask.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;

import androidx.annotation.Nullable;

import com.naccoro.wask.R;

import java.util.Locale;

public class PeriodPresenter extends androidx.appcompat.widget.AppCompatTextView {
    private static final int REPLACEMENT_CYCLE = 0;
    private static final int SNOOZE_REMINDER = 1;
    private static final int CURRENT_USING = 2;

    private int format;
    private boolean isKoreanType;

    public PeriodPresenter(Context context) {
        super(context);
        init(null);
    }

    public PeriodPresenter(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public PeriodPresenter(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(@Nullable AttributeSet attrs) {
        if (attrs != null) {
            TypedArray typedArray = getContext().getTheme()
                    .obtainStyledAttributes(attrs, R.styleable.PeriodPresenter, 0, 0);
            if (typedArray.hasValue(R.styleable.PeriodPresenter_type)) {
                format = typedArray.getInt(R.styleable.PeriodPresenter_type, 0);
            }
        } else {
            format = REPLACEMENT_CYCLE;
        }
        setLocaleDateType();
    }

    //설정 언어에 맞게 어순과 형식 변경
    private void setLocaleDateType() {
        //설정된 국가 가져오기
        Locale locale = getResources().getConfiguration().locale;
        //국가로부터 언어 가져오기
        String language = locale.getLanguage();

        isKoreanType = !language.equals("en");
    }

    public void setPeriod(int period) {
        StringBuilder builder = new StringBuilder()
        .append(getPrefix())
                .append(getPeriod(period))
                .append(getSuffix(period));

        setText(builder.toString());
    }

    private String getPrefix() {
        if (isKoreanType) {
            return "";
        } else {
            switch (format) {
                case REPLACEMENT_CYCLE :
                    return getContext().getString(R.string.period_prefix_cycle);
                case SNOOZE_REMINDER :
                    return getContext().getString(R.string.period_prefix_snooze);
                case CURRENT_USING :
                    return "";
                default:
                    throw new IllegalArgumentException("format value must be in 0 ~ 2");
            }
        }
    }

    private String getPeriod(int period) {
        if (format == CURRENT_USING) {
            return "";
        }
        if (isKoreanType) {
            return period + "";
        } else {
            return period == 1 ? "" : period + "";
        }
    }

    private String getSuffix(int period) {
        if (isKoreanType) {
            switch (format) {
                case REPLACEMENT_CYCLE :
                    return getContext().getString(R.string.period_suffix_cycle);
                case SNOOZE_REMINDER :
                    return getContext().getString(R.string.period_suffix_snooze);
                case CURRENT_USING :
                    return getContext().getString(R.string.period_suffix_current_using);
                default:
                    throw new IllegalArgumentException("format value must be in 0 ~ 2");
            }
        } else {
            String suffix = period == 1 ? getContext().getString(R.string.period_suffix_singular)
                    : getContext().getString(R.string.period_suffix_plural);
            if (format == CURRENT_USING) {
                suffix += getContext().getString(R.string.period_suffix_used);
            }
            return suffix;
        }
    }
}