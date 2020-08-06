package com.naccoro.wask.datepicker.wheel;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import com.naccoro.wask.R;

import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;


/**
 * Modified by: WheelView - wangjie
 * link : https://github.com/wangjiegulu/WheelView
 * <p>
 * Email: jaeryo2357@naver.com
 * Date: 8/5/20.
 */
public class WheelDatePicker extends NestedScrollView implements WheelSnapScrollListener.OnSnapPositionChangeListener {

    private LinearLayout parent;
    WheelRecyclerView yearRecycler;
    WheelRecyclerView monthRecycler;
    WheelRecyclerView dayRecycler;

    Paint linePaint;
    float recyclerHeight = 0f;

    private final int lineColor = Color.parseColor("#a0a7ad");

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
        NestedScrollView.LayoutParams rootParams = (NestedScrollView.LayoutParams)getLayoutParams();
        if (rootParams == null) {
            rootParams = new NestedScrollView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            rootParams.leftMargin = (int)getResources().getDimension(R.dimen.datePicker_margin);
            rootParams.rightMargin = (int)getResources().getDimension(R.dimen.datePicker_margin);
        }
        this.addView(parent, rootParams);

        yearRecycler = new WheelRecyclerView(context);
        monthRecycler = new WheelRecyclerView(context);
        dayRecycler = new WheelRecyclerView(context);

        recyclerHeight = yearRecycler.getMaxHeight();

        //3개의 recyclerView를 동일한 크기로 정렬하기 위해 weight 값을 1로 설정
        LinearLayout.LayoutParams parentParams = new LinearLayout.LayoutParams(0, (int)recyclerHeight, 1);

        parent.addView(yearRecycler, parentParams);
        parent.addView(monthRecycler, parentParams);
        parent.addView(dayRecycler, parentParams);

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

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (linePaint == null) {
            linePaint = new Paint();
        }
        linePaint.setColor(lineColor);
        linePaint.setStrokeWidth(1.5f);

        canvas.drawLine(0, recyclerHeight / 3 + 10f, this.getMeasuredWidth(), recyclerHeight / 3 + 10f, linePaint);
        canvas.drawLine(0, (recyclerHeight / 3) * 2 - 10f, this.getMeasuredWidth(), (recyclerHeight / 3) * 2 - 10f, linePaint);
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
                int endValue = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
                if (dayRecycler.endDateValue != endValue) {
                    dayRecycler.setRecyclerViewRange(1, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
                    dayRecycler.scrollToPosition(dayRecycler.adapter.getEmptySpace());
                }
            }
        });
    }
}
