package com.naccoro.wask.datepicker.wheel;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;


/**
 * Modified by: WheelView - wangjie
 * link : https://github.com/wangjiegulu/WheelView
 *
 * Email: jaeryo2357@naver.com
 * Date: 8/5/20.
 */
public class WheelDatePicker extends NestedScrollView  implements WheelSnapScrollListener.OnSnapPositionChangeListener{

    private LinearLayout parent;
    WheelRecyclerView yearRecycler;
    WheelRecyclerView monthRecycler;
    WheelRecyclerView dayRecycler;

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
        parent = new LinearLayout(context);
        parent.setOrientation(LinearLayout.HORIZONTAL);
        parent.setGravity(Gravity.CENTER);
        parent.setWeightSum(3);

        this.addView(parent);


        yearRecycler = new WheelRecyclerView(context);
        monthRecycler = new WheelRecyclerView(context);
        dayRecycler = new WheelRecyclerView(context);

        //3개의 recyclerView를 동일한 크기로 정렬하기 위해 weight 값을 1로 설정
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, yearRecycler.getMaxHeight(), 1);

        parent.addView(yearRecycler, params);
        parent.addView(monthRecycler, params);
        parent.addView(dayRecycler, params);

        yearRecycler.setRecyclerViewType(WheelRecyclerView.WheelRecyclerViewType.YEAR);
        monthRecycler.setRecyclerViewType(WheelRecyclerView.WheelRecyclerViewType.MONTH);
        dayRecycler.setRecyclerViewType(WheelRecyclerView.WheelRecyclerViewType.DAY);


        //year, month, day 모두 snap Listener 적용
        yearRecycler.attachSnapHelperWithListener(new LinearSnapHelper(),
                WheelSnapScrollListener.Behavior.NOTIFY_ON_SCROLL,
                this);

        monthRecycler.attachSnapHelperWithListener(new LinearSnapHelper(),
                WheelSnapScrollListener.Behavior.NOTIFY_ON_SCROLL,
                this);

        dayRecycler.attachSnapHelperWithListener(new LinearSnapHelper(),
                WheelSnapScrollListener.Behavior.NOTIFY_ON_SCROLL,
                this);
        Calendar calendar = new GregorianCalendar();
        yearRecycler.setRecyclerViewRange(2000, 2030);
        monthRecycler.setRecyclerViewRange(1, 12);
        dayRecycler.setRecyclerViewRange(1, 31);
    }

    /**
     * snapPosition을 Adapter에 등록하여 Text 스타일 변경
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
                setRangeDay();
        }
    }

    private void setRangeDay() {
        int year = yearRecycler.startDateValue + (yearRecycler.adapter.centerPosition - yearRecycler.adapter.getEmptySpace());
        int month = monthRecycler.startDateValue + (monthRecycler.adapter.centerPosition - monthRecycler.adapter.getEmptySpace());

        final Calendar calendar = new GregorianCalendar(year, month - 1, 1);
        dayRecycler.post(new Runnable() {
            @Override
            public void run() {
                int endValue  = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
                if (dayRecycler.endDateValue != endValue) {
                    dayRecycler.setRecyclerViewRange(1, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
                    dayRecycler.scrollToPosition(dayRecycler.adapter.getEmptySpace());
                }
            }
        });
    }
}
