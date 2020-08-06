package com.naccoro.wask.customview.datepicker.wheel;

import android.content.Context;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import com.naccoro.wask.R;
import com.naccoro.wask.utils.MetricsUtil;

/**
 * centerPosition의 item의 크기를 가장 크게
 * 가운데에서 벗어날 수록 item의 크기가 작아지는 RecyclerView이다.
 *
 * @author jaeryo
 * @since 2020-08-06
 */
public class WheelRecyclerView extends RecyclerView {

    Context context;
    WheelRecyclerAdapter adapter;
    private final WheelRecyclerViewType defaultType = WheelRecyclerViewType.YEAR;
    WheelRecyclerViewType recyclerViewType = defaultType;
    WheelSnapScrollListener wheelSnapScrollListener;

    //이 RecyclerView가 보여주는 범위를 저장한다.
    int startDateValue = 1;
    int endDateValue = 1;

    private final int SECOND_LABEL_POSITION = 1;

    private int selectedLabelColor = 0;
    private int nonSelectedLabelColor = 0;

    private final int selectedLabelSize = 20;
    private final int secondLabelSize = 18;
    private final int thirdLabelSize = 15;

    private final int selectedLabelPadding = 5;
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

        selectedLabelColor = context.getColor(R.color.colorDatePickerSelectedLabel);
        nonSelectedLabelColor = context.getColor(R.color.colorDatePickerNoSelectedLabel);
    }

    /**
     * //스크롤시 중앙에 아이탬을 고정하도록 도와주는 snapHelper 적용
     *
     * @param helper
     * @param behavior : listener가 호출 되는 시점 Idle or Scroll
     * @param listener : snap position 변경 시 호출
     */
    public void attachSnapHelperWithListener(SnapHelper helper,
                                             WheelSnapScrollListener.Behavior behavior,
                                             WheelSnapScrollListener.OnSnapPositionChangeListener listener) {
        helper.attachToRecyclerView(this);

        wheelSnapScrollListener = new WheelSnapScrollListener(
                helper, behavior, listener);
        this.addOnScrollListener(wheelSnapScrollListener);
    }

    public void setSnapBehaviorType(WheelSnapScrollListener.Behavior behaviorType) {
        if (wheelSnapScrollListener != null) {
            wheelSnapScrollListener.behavior = behaviorType;
        }
    }

    public void setRecyclerViewType(WheelRecyclerViewType type) {
        recyclerViewType = type;
        adapter.setRecyclerType(type);
    }

    public void setRecyclerViewRange(int startDateValue, int endDateValue, boolean change) {
        this.startDateValue = startDateValue;
        this.endDateValue = endDateValue;

        adapter.setRange(startDateValue, endDateValue, change);
    }

    /**
     * WheelRecyclerView는 5개의 Item을 보여준다.
     * ThirdLabel
     * SecondLabel
     * SelectedLabel : snapHelper로 구한 화면에서 centerPosition
     * SecondLabel
     * ThirdLabel
     * <p>
     * Height 값이 다른 5개의 TextView 높이 값을 모두 더해서 총 RecyclerView 높이를 구함
     *
     * @return recyclerView 높이
     */
    public float getMaxHeight() {
        float allItemHeight = 0;
        allItemHeight += getSelectedLabelHeight();

        allItemHeight += getSecondLabelHeight() * 2;

        allItemHeight += getThirdLabelHeight() * 2;


        return allItemHeight;
    }

    public float getSelectedLabelHeight() {
        TextView text = createItemView(selectedLabelSize, selectedLabelPadding);
        return getViewMeasuredHeight(text);
    }

    public float getSecondLabelHeight() {
        TextView text = createItemView(secondLabelSize, nonSelectedLabelPadding);
        return getViewMeasuredHeight(text);
    }

    public float getThirdLabelHeight() {
        TextView text = createItemView(thirdLabelSize, nonSelectedLabelPadding);
        return getViewMeasuredHeight(text);
    }

    /**
     * 각 위치별로 Item의 실제 높이를 구하기 위한 View를 생성
     *
     * @param textSize    : testSize
     * @param paddingSize : TextView 사이의 간격
     */
    private TextView createItemView(int textSize, int paddingSize) {
        TextView textView = (TextView) LayoutInflater.from(context).inflate(R.layout.date_picker_item, null);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
        int padding = (int) MetricsUtil.convertDpToPixel(paddingSize, getContext());
        textView.setPadding(padding, padding, padding, padding);

        return textView;
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
     * WheelRecyclerView는 범위 이외에도 양 끝에 2개의 빈공간을 가지고 있습니다. (second + third)
     * 범위가 1 ~ 12라고 하고 5개의 Item을 보여준다고 했을 때 snapPosition으로 1이 가르키려고 하면 1위에 2개의 공간이 있어야 합니다.
     * 이를 위해 String이 비어있는 2개의 공간을 양 끝에 넣어줍니다.
     */
    class WheelRecyclerAdapter extends RecyclerView.Adapter<WheelRecyclerAdapter.WheelViewHolder> {
        //빈공간의 수를 상수로 지정
        private final int emptySpace = 2;
        //빈공간을 계산하여 기본 centerPosition을 설정
        private final int defaultPosition = emptySpace;

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
         * @param change     : 범위를 설정한 뒤 바로 UI를 변경할 지
         */
        public void setRange(int startValue, int endValue, boolean change) {
            this.startDateValue = startValue;
            this.endDateValue = endValue;
            this.centerPosition = defaultPosition;
            if (change) {
                this.notifyDataSetChanged();
            }
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

            if (position >= emptySpace && (endDateValue - startDateValue) + emptySpace >= position) {
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

            int paddingSize = (int) MetricsUtil.convertDpToPixel(padding, getContext());
            holder.itemLabel.setTextColor(textColor);
            holder.itemLabel.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
            holder.itemLabel.setPadding(paddingSize, paddingSize, paddingSize, paddingSize);
            holder.itemLabel.setText(itemBody);
        }

        /**
         * @return 범위 + 양끝의 빈공간의 수
         */
        @Override
        public int getItemCount() {
            return (endDateValue - startDateValue + 1) + emptySpace * 2;
        }

        /**
         * 범위로 centerPosition 그 자체가 날짜 데이터가 될 수 있다.
         *
         * @return 빈공간을 계산하여 구한 날짜 값
         */
        public int getDate() {
            return startDateValue + (centerPosition - emptySpace);
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
