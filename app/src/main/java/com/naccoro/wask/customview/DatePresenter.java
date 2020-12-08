package com.naccoro.wask.customview;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.naccoro.wask.R;
import com.naccoro.wask.ui.calendar.Date;
import com.naccoro.wask.utils.MetricsUtil;

import java.text.DateFormatSymbols;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

/**
 * <YYYY년 MM월>, <MMMM YYYY> 형식으로 연도와 월을 보여주는 뷰
 * 설정된 언어에 따라 형식이 맞추어짐
 * @author seonggyu
 * @since 2020.12.05
 */
public class DatePresenter extends LinearLayout {
    private static final float TEXT_SIZE = 14f;
    private static final float PADDING_ARROW_IMAGE = 11f;

    private boolean isKoreanDataType;
    private TextView yearText;
    private TextView monthText;
    private ImageView arrowImage;
    private Date date;

    private DateFormatSymbols dateFormatSymbols;

    public DatePresenter(Context context) {
        super(context);
        init();
    }

    public DatePresenter(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DatePresenter(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public DatePresenter(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    //뷰 초기화
    private void init() {
        //부모 레이아웃의 속성 설정
        setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        setGravity(Gravity.CENTER);
        setOrientation(LinearLayout.HORIZONTAL);
        //부모 클릭시 애니메이션 효과 설정
        setClickEffect();

        //오늘 날짜로 초기화
        initDate();
        //자식 뷰 초기화
        initChildViews();
        //설정 언어에 맞게 어순과 형식 변경
        setLocaleDateType();
    }

    //설정 언어에 맞게 어순과 형식 변경
    private void setLocaleDateType() {
        //설정된 국가 가져오기
        Locale locale = getResources().getConfiguration().locale;
        //국가로부터 언어 가져오기
        String language = locale.getLanguage();
        if (language.equals("en")) { //영어로 설정된 경우
            setEnglishDateType();
        } else { //그 외
            setKoreanDateType();
        }
    }

    //오늘 날짜로 date 초기화
    private void initDate() {
        GregorianCalendar calendar = new GregorianCalendar();
        date = new Date(calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
    }

    //자식 뷰 생성 및 속성 초기화
    private void initChildViews() {
        yearText = new TextView(getContext());
        monthText = new TextView(getContext());
        arrowImage = new ImageView(getContext());
        setAttributesToTextView(yearText);
        setAttributesToTextView(monthText);
        setAttributesToImageView();
    }

    //이미지뷰 속성 초기화
    private void setAttributesToImageView() {
        int padding = (int) MetricsUtil.convertDpToPixel(PADDING_ARROW_IMAGE, getContext());
        arrowImage.setPadding(padding, padding, padding, padding);
        arrowImage.setImageDrawable(
                ContextCompat.getDrawable(getContext(), R.drawable.ic_calendar_changedate));
        arrowImage.setBackgroundColor(Color.TRANSPARENT);
    }

    //텍스트뷰 속성 초기화
    private void setAttributesToTextView(TextView textView) {
        textView.setTextSize(TEXT_SIZE);
        textView.setTextColor(ContextCompat.getColor(getContext(), R.color.waskGrayFont));
    }

    //클릭 애니메이션 효과 설정
    private void setClickEffect() {
        TypedValue outValue = new TypedValue();
        getContext().getTheme().resolveAttribute(android.R.attr.selectableItemBackground,
                outValue, true);
        setBackgroundResource(outValue.resourceId);
    }

    //영어 형식으로 어순 및 형식 변경
    private void setEnglishDateType() {
        removeAllViews();
        addView(monthText);
        addView(yearText);
        addView(arrowImage);
        setEnglishDate();
        isKoreanDataType = false;
    }

    //한국어 형식으로 어순 및 형식 변경
    private void setKoreanDateType() {
        removeAllViews();
        addView(yearText);
        addView(monthText);
        addView(arrowImage);
        setKoreanDate();
        isKoreanDataType = true;
    }

    //한국어 형식으로 날짜 적용
    private void setKoreanDate() {
        yearText.setText(date.getYear() + getContext().getString(R.string.calendar_year) + " ");
        monthText.setText(date.getMonth() + 1 + getContext().getString(R.string.calendar_month));
    }

    //영어 형식으로 날짜 적용
    private void setEnglishDate() {
        yearText.setText(String.valueOf(date.getYear()));
        monthText.setText(getMonthName(date.getMonth()) + " ");
    }

    //영어인 경우, 월의 이름(November, December 등) 가져오기
    private String getMonthName(int month) {
        if (dateFormatSymbols == null) {
            dateFormatSymbols = new DateFormatSymbols();
        }
        return dateFormatSymbols.getMonths()[month];
    }

    /**
     * 해당 뷰에서 보여주고 싶은 연도와 월을 설정
     * @param date 설정할 연도와 월을 포함한 Date 객체
     */
    public void setDate(Date date) {
        this.date = date;
        if (isKoreanDataType) {
            setKoreanDate();
        } else {
            setEnglishDate();
        }
    }
}
