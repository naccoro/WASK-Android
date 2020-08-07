package com.naccoro.wask.customview.datepicker.wheel;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.naccoro.wask.R;

import java.util.Calendar;
import java.util.GregorianCalendar;


/**
 * Modified by: WheelView - wangjie
 * link : https://github.com/wangjiegulu/WheelView
 * <p>
 *
 * @author jaeryo / Email: jaeryo2357@naver.com
 * @since 2020-08-06.
 * <p>
 * WheelDatePicker는 3개의 Vertical RecyclerView를 가진 Custom View입니다.
 * 각 RecyclerView는 year, month, day를 나타냅니다.
 * 한 레이아웃에서 3개의 RecyclerView 스크롤을 인식하기 위해 NestedScrollView를 사용합니다.
 */
public class WheelDatePicker extends NestedScrollView implements WheelSnapScrollListener.OnSnapPositionChangeListener {

    //picker에 표시할 년도의 범위를 2000년도~2030년도로 설정 (변경가능)
    private final int START_YEAR_VALUE = 2000;
    private final int END_YEAR_VALUE = 2030;

    //picker에 표시되는 월의 범위를 1(고정)~12월로 설정
    private final int END_MONTH_VALUE = 12;

    //picker에 표시되는 일의 범위를 1(고정)~31일로 설정 (이후 로직에서 변경)
    private final int END_DAY_OF_MONTH_VALUE = 31;


    boolean isDateInitDone = true;

    private WheelRecyclerView yearRecyclerView;
    private WheelRecyclerView monthRecyclerView;
    private WheelRecyclerView dayRecyclerView;

    //selected Area Line 을 그리기한 paint
    Paint linePaint;
    //recyclerview 모두 5개의 크기가 다른 item을 보여주기 위한 높이를 저장하는 변수
    float recyclerHeight = 0f;

    public WheelDatePicker(Context context) {
        super(context);
        init(context);
    }

    public WheelDatePicker(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public WheelDatePicker(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {

        //사용자가 Nested ScrollView의 뷰 끝에서 스크롤을 시도할 시 번지는 효과 제거
        this.setOverScrollMode(OVER_SCROLL_NEVER);

        //RecyclerView 3개를 넣기위한 LinearLayout을 생성, weight로 동일한 비율의 뷰를 정렬하기에 편안
        LinearLayout parent = new LinearLayout(context);
        parent.setOrientation(LinearLayout.HORIZONTAL);
        parent.setGravity(Gravity.CENTER);
        parent.setWeightSum(3);
        NestedScrollView.LayoutParams rootParams = (NestedScrollView.LayoutParams) getLayoutParams();
        if (rootParams == null) {
            rootParams = new NestedScrollView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            rootParams.leftMargin = (int) getResources().getDimension(R.dimen.datePicker_margin);
            rootParams.rightMargin = (int) getResources().getDimension(R.dimen.datePicker_margin);
        }
        this.addView(parent, rootParams);

        yearRecyclerView = new WheelRecyclerView(context);
        //사용자가 RecyclerView의 뷰 끝에서 스크롤을 시도할 시 번지는 효과 제거
        yearRecyclerView.setOverScrollMode(OVER_SCROLL_NEVER);
        monthRecyclerView = new WheelRecyclerView(context);
        monthRecyclerView.setOverScrollMode(OVER_SCROLL_NEVER);
        dayRecyclerView = new WheelRecyclerView(context);
        dayRecyclerView.setOverScrollMode(OVER_SCROLL_NEVER);

        recyclerHeight = yearRecyclerView.getMaxHeight();

        //3개의 recyclerView를 동일한 크기로 정렬하기 위해 weight 값을 1로 설정
        LinearLayout.LayoutParams parentParams = new LinearLayout.LayoutParams(0, (int) recyclerHeight, 1);

        parent.addView(yearRecyclerView, parentParams);
        parent.addView(monthRecyclerView, parentParams);
        parent.addView(dayRecyclerView, parentParams);

        //각각 RecyclerView의 Date Type 지정
        yearRecyclerView.setRecyclerViewType(WheelRecyclerView.WheelRecyclerViewType.YEAR);
        monthRecyclerView.setRecyclerViewType(WheelRecyclerView.WheelRecyclerViewType.MONTH);
        dayRecyclerView.setRecyclerViewType(WheelRecyclerView.WheelRecyclerViewType.DAY);

        yearRecyclerView.setRecyclerViewRange(START_YEAR_VALUE, END_YEAR_VALUE);
        monthRecyclerView.setRecyclerViewRange(1, END_MONTH_VALUE);
        dayRecyclerView.setRecyclerViewRange(1, END_DAY_OF_MONTH_VALUE);

        //year, month, day 모두 snap Listener 적용
        yearRecyclerView.attachSnapHelperWithListener(new LinearSnapHelper(),
                WheelSnapScrollListener.Behavior.NOTIFY_ON_SCROLL, this);
        monthRecyclerView.attachSnapHelperWithListener(new LinearSnapHelper(),
                WheelSnapScrollListener.Behavior.NOTIFY_ON_SCROLL, this);
        dayRecyclerView.attachSnapHelperWithListener(new LinearSnapHelper(),
                WheelSnapScrollListener.Behavior.NOTIFY_ON_SCROLL, this);
    }


    /**
     * 화면에 UI를 그리는 함수 onDraw()
     * super 키워드로 상위 클래스의 onDraw()를 호출하여 NestedScrollView가 그려짐과 동시에
     * selected Area 영역에 라인을 그린다.
     *
     * @param canvas
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (linePaint == null) {
            linePaint = new Paint();
        }
        linePaint.setColor(getContext().getColor(R.color.colorDatePickerLine));
        linePaint.setStrokeWidth(1.5f);


        float firstLineHeight = yearRecyclerView.getSecondLabelHeight() + yearRecyclerView.getThirdLabelHeight();
        canvas.drawLine(0, firstLineHeight, this.getMeasuredWidth(), firstLineHeight, linePaint);
        float secondLineHeight = firstLineHeight + yearRecyclerView.getSelectedLabelHeight();
        canvas.drawLine(0, secondLineHeight, this.getMeasuredWidth(), secondLineHeight, linePaint);
    }

    /**
     * snapPosition은 사용자가 스크롤하여 설정한 Date의 값(위치)을 의미한다.
     * WheelRecyclerView는 snapPosition에 따라 item의 크기, 색이 달라지므로 이를 아래 함수에서 변경시켜주어야 한다.
     *
     * @param position 변경된 snapPosition
     */
    @Override
    public void onSnapPositionChange(RecyclerView recyclerView, final int position) {
        if (!(recyclerView instanceof WheelRecyclerView)) {
            return;
        }

        final WheelRecyclerView wheelRecyclerView = (WheelRecyclerView) recyclerView;
        wheelRecyclerView.post(new Runnable() {
            @Override
            public void run() {
                wheelRecyclerView.adapter.setCenterPosition(position);
            }
        });

        switch (wheelRecyclerView.recyclerViewType) {
            case YEAR:
            case MONTH:
                if (isDateInitDone) {
                    setRangeDay(true);
                }
                break;
            case DAY:
                if (!isDateInitDone) {
                    setAllSnapListenerScroll();
                    isDateInitDone = true;
                }
        }
    }

    /**
     * dayRecyclerView의 범위를 설정한다. (1일 ~ 31일, 1일 ~ 29일)
     *
     * @param scrollTo : 범위를 설정하고 스크롤 할지를 묻는 변수
     */
    private void setRangeDay(final boolean scrollTo) {
        int year = getYear();
        int month = getMonth();

        final Calendar calendar = new GregorianCalendar(year, month, 1);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //해당일자 년 달력의 마지막 요일을 계산
                int endValue = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
                //마지막 일이 변경될 경우 RecyclerView 범위 초기화
                if (dayRecyclerView.endDateValue != endValue) {
                    dayRecyclerView.setRecyclerViewRange(1, endValue);

                    int centerPosition = dayRecyclerView.adapter.centerPosition;
                    if (scrollTo && centerPosition > endValue) {

                        int dayInitPosition = 0;

                        LinearLayoutManager manager = (LinearLayoutManager) dayRecyclerView.getLayoutManager();
                        if (manager != null) {
                            dayRecyclerView.adapter.setCenterPosition(dayRecyclerView.adapter.getEmptySpace());
                            manager.scrollToPositionWithOffset(dayInitPosition, 0);
                        }
                    }
                }
            }
        }, 200);
    }

    public int getYear() {
        int year = yearRecyclerView.adapter.getDate();
        return year;
    }

    public int getMonth() {
        int month = monthRecyclerView.adapter.getDate() - 1;
        return month;
    }

    public int getDay() {
        int day = dayRecyclerView.adapter.getDate();
        return day;
    }

    /**
     * DatePicker의 달력 초기 값을 설정하는 함수
     *
     * @param year
     * @param month
     * @param day
     */
    public void setDate(int year, int month, int day) {

        setAllSnapListenerScrollIdle();
        isDateInitDone = false;

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                final int yearPosition = (year - START_YEAR_VALUE);
                LinearLayoutManager manager = (LinearLayoutManager) yearRecyclerView.getLayoutManager();
                if (manager != null) {
                    //기존 RecyclerView.smoothScrollToPosition or scrollToPosition은 잘못된 스크롤 동작 방식이 여러 보고 되었다.
                    //정확한 위치로 이동하기 위해서는 LayoutManager를 사용해야한다.
                    yearRecyclerView.adapter.setCenterPosition(yearPosition + yearRecyclerView.adapter.getEmptySpace());
                    manager.scrollToPositionWithOffset(yearPosition, 0);
                }
            }
        }, 200);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                final int monthPosition = month;
                LinearLayoutManager manager = (LinearLayoutManager) monthRecyclerView.getLayoutManager();
                if (manager != null) {
                    monthRecyclerView.adapter.setCenterPosition(monthPosition + monthRecyclerView.adapter.getEmptySpace());
                    manager.scrollToPositionWithOffset(monthPosition, 0);
                }
            }
        }, 400);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //변경된 년도 달력에 따라 마지막 요일 적용
                setRangeDay(false);
                final int dayPosition = day - 1;
                LinearLayoutManager manager = (LinearLayoutManager) dayRecyclerView.getLayoutManager();
                if (manager != null) {
                    dayRecyclerView.adapter.setCenterPosition(dayPosition + dayRecyclerView.adapter.getEmptySpace());
                    manager.scrollToPositionWithOffset(dayPosition, 0);
                }
            }
        }, 600);
    }

    private void setAllSnapListenerScrollIdle() {
        //year, month, day 모두 snap Listener 적용
        yearRecyclerView.setSnapBehaviorType(
                WheelSnapScrollListener.Behavior.NOTIFY_ON_SCROLL_STATE_IDLE);

        monthRecyclerView.setSnapBehaviorType(
                WheelSnapScrollListener.Behavior.NOTIFY_ON_SCROLL_STATE_IDLE);

        dayRecyclerView.setSnapBehaviorType(
                WheelSnapScrollListener.Behavior.NOTIFY_ON_SCROLL_STATE_IDLE);
    }

    private void setAllSnapListenerScroll() {
        //year, month, day 모두 snap Listener 적용
        yearRecyclerView.setSnapBehaviorType(
                WheelSnapScrollListener.Behavior.NOTIFY_ON_SCROLL);

        monthRecyclerView.setSnapBehaviorType(
                WheelSnapScrollListener.Behavior.NOTIFY_ON_SCROLL);

        dayRecyclerView.setSnapBehaviorType(
                WheelSnapScrollListener.Behavior.NOTIFY_ON_SCROLL);
    }


}
