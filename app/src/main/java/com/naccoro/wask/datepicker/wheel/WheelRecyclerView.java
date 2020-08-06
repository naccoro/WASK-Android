package com.naccoro.wask.datepicker.wheel;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import com.naccoro.wask.R;

public class WheelRecyclerView extends RecyclerView {

    Context context;
    WheelRecyclerAdapter adapter;
    private final WheelRecyclerViewType defaultType = WheelRecyclerViewType.YEAR;
    WheelRecyclerViewType recyclerViewType = defaultType;

    int startDateValue = 1;
    int endDateValue = 1;

    private final int SECOND_LABEL_POSITION = 1;

    private final int selectedLabelColor = Color.parseColor("#707070");
    private final int nonSelectedLabelColor = Color.parseColor("#a0a7ad");
    private final int lineColor = Color.parseColor("#707070");

    private final int selectedLabelSize = 20;
    private final int secondLabelSize = 18;
    private final int thirdLabelSize = 15;

    private final int selectedLabelPadding = 15;
    private final int nonSelectedLabelPadding = 2;

    public WheelRecyclerView(@NonNull Context context) {
        super(context);
        init(context);
    }

    public WheelRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public WheelRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        this.context = context;
        this.setItemAnimator(null);
        LinearLayoutManager manager = new LinearLayoutManager(context);
        this.setLayoutManager(manager);

        adapter = new WheelRecyclerAdapter(defaultType);
        this.setAdapter(adapter);
    }

    /** //스크롤시 중앙에 아이탬을 고정하도록 도와주는 snapHelper 적용
     *
     * @param helper
     * @param behavior : listener가 호출 되는 시점 Idle or Scroll
     * @param listener : snap position 변경 시 호출
     */
    public void attachSnapHelperWithListener(SnapHelper helper,
                                             WheelSnapScrollListener.Behavior behavior,
                                             WheelSnapScrollListener.OnSnapPositionChangeListener listener) {
        helper.attachToRecyclerView(this);

        WheelSnapScrollListener wheelSnapScrollListener = new WheelSnapScrollListener(
                helper, behavior, listener);
        this.addOnScrollListener(wheelSnapScrollListener);
    }

    public void setRecyclerViewType(WheelRecyclerViewType type) {
        recyclerViewType = type;
        adapter.setRecyclerType(type);
    }

    public void setRecyclerViewRange(int startDateValue, int endDateValue) {
        this.startDateValue = startDateValue;
        this.endDateValue = endDateValue;

        adapter.setRange(startDateValue, endDateValue);
    }

    /**
     * Height 값이 다른 5개의 TextView 높이 값을 모두 더해서 총 RecyclerView 높이를 구함
     *
     * @return recyclerView 높이
     */
    public int getMaxHeight() {
        int allItemHeight = 0;
        TextView text = createItemView(selectedLabelSize, selectedLabelPadding);
        allItemHeight += getViewMeasuredHeight(text);
        text = createItemView(secondLabelSize, nonSelectedLabelPadding);
        allItemHeight += getViewMeasuredHeight(text);
        text = createItemView(thirdLabelSize, nonSelectedLabelPadding);
        allItemHeight += getViewMeasuredHeight(text) * 2;

        return allItemHeight;
    }

    /**
     * recyclerView의 높이를 고정시키기 위해 Height 크기가 다른 TextView 5개를 만들기 위한 함수
     *
     * @param textSize    : testSize
     * @param paddingSize : TextView 사이의 간격
     */
    private TextView createItemView(int textSize, int paddingSize) {
        TextView textView = (TextView) LayoutInflater.from(context).inflate(R.layout.date_picker_item, null);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
        int padding = dip2px(paddingSize);
        textView.setPadding(padding, padding, padding, padding);

        return textView;
    }

    /**
     * dp 값을 px로 변경
     *
     * @param dpValue
     * @return px로 변경된 값
     */
    private int dip2px(float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * viwe의 높이를 측정하는 함수
     *
     * @param view
     * @return 생성된 view의 높이
     */
    private int getViewMeasuredHeight(View view) {
        int wrapSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        view.measure(wrapSpec, wrapSpec);
        return view.getMeasuredHeight();
    }

    /**
     * RecyclerView의 posiiton으로 스크롤이 이동하는 동시에 snapPosition으로 지정
     *
     * @param position
     */
    @Override
    public void scrollToPosition(int position) {
        super.scrollToPosition(position);
        adapter.setCenterPosition(position - 2);
    }


    class WheelRecyclerAdapter extends RecyclerView.Adapter<WheelRecyclerAdapter.WheelViewHolder> {

        private final int emptySpace = 2;
        private final int defaultPosition = 2;

        private WheelRecyclerViewType type;

        int centerPosition = defaultPosition;
        int startDateValue = 1;
        int endDateValue = 1;

        WheelRecyclerAdapter(WheelRecyclerViewType type) {
            this.type = type;
        }

        public int getEmptySpace() {
            return emptySpace;
        }

        public void setRecyclerType(WheelRecyclerViewType type) {
            this.type = type;
            this.notifyDataSetChanged();
        }

        /**
         * WheelRecyclerView의 범위를 결정
         *
         * @param startValue : 1일
         * @param endValue   : 31일
         */
        public void setRange(int startValue, int endValue) {
            this.startDateValue = startValue;
            this.endDateValue = endValue;
            this.centerPosition = defaultPosition;
            this.notifyDataSetChanged();
        }

        void setCenterPosition(int centerPosition) {
            this.centerPosition = centerPosition;
            this.notifyItemRangeChanged(centerPosition - 2, centerPosition + 2);
        }

        @NonNull
        @Override
        public WheelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.date_picker_item, parent, false);
            return new WheelViewHolder(view);
        }


        @Override
        public void onBindViewHolder(@NonNull WheelViewHolder holder, int position) {
            String itemBody;
            int textColor;
            int textSize;
            int padding;

            if (position >= emptySpace && (endDateValue - startDateValue) + emptySpace > position) {
                itemBody = getDateString(position - emptySpace);
            } else {
                itemBody = ""; // recyclerView 양 끝에 빈공간을 생성
            }

            if (centerPosition == position) {
                textColor = selectedLabelColor;
                textSize = selectedLabelSize;
                padding = selectedLabelPadding;
            } else {
                if (Math.abs(centerPosition - position) == SECOND_LABEL_POSITION) { //가운데 포지션과 차이가 1이라면
                    textSize = secondLabelSize;
                } else {
                    textSize = thirdLabelSize;
                }
                textColor = nonSelectedLabelColor;
                padding = nonSelectedLabelPadding;
            }

            holder.itemLabel.setTextColor(textColor);
            holder.itemLabel.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
            holder.itemLabel.setPadding(padding, padding, padding, padding);
            holder.itemLabel.setText(itemBody);
        }

        @Override
        public int getItemCount() {
            return (endDateValue - startDateValue + 1) + emptySpace * 2;
        }

        private String getDateString(int position) {
            switch (type) {
                case YEAR:
                    return getYearString(startDateValue + position);
                case MONTH:
                    return getMonthString(startDateValue + position);
                default:
                    return getDayString(startDateValue + position);
            }
        }

        private String getYearString(int year) {
            return year + "년";
        }

        private String getMonthString(int month) {
            return month + "월";
        }

        private String getDayString(int day) {
            return day + "일";
        }

        class WheelViewHolder extends RecyclerView.ViewHolder {
            TextView itemLabel;

            public WheelViewHolder(@NonNull View itemView) {
                super(itemView);
                itemLabel = (TextView) itemView;
            }
        }
    }

    enum WheelRecyclerViewType {
        YEAR, MONTH, DAY
    }
}
