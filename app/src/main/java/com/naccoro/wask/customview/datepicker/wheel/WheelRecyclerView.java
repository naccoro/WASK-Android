package com.naccoro.wask.customview.datepicker.wheel;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
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
public class WheelRecyclerView extends RecyclerView implements WheelSnapScrollListener.OnSnapPositionChangeListener {

    private final int SECOND_LABEL_POSITION = 1;

    private final int SELECTED_LABEL_SIZE = 20;
    private final int SECOND_LABEL_SIZE = 18;
    private final int THIRD_LABEL_SIZE = 15;

    private final int SELECTED_LABEL_PADDING = 5;
    private final int NON_SELECTED_LABEL_PADDING = 2;

    //picker에 표시할 년도의 범위를 2000년도~2030년도로 설정 (변경가능)
    public static final int START_YEAR_VALUE = 2000;
    public static final int END_YEAR_VALUE = 2030;

    //picker에 표시되는 월의 범위를 1(고정)~12월로 설정
    public static final int END_MONTH_VALUE = 12;

    //picker에 표시되는 일의 범위를 1(고정)~31일로 설정 (이후 로직에서 변경)
    public static final int END_DAY_OF_MONTH_VALUE = 31;

    public static final int END_DEFAULT_VALUE = 10;

    private final WheelRecyclerViewType DEFAULT_TYPE = WheelRecyclerViewType.NONE;

    private int selectedLabelColor = 0;
    private int nonSelectedLabelColor = 0;

    private WheelSnapScrollListener wheelSnapScrollListener;
    private Context context;
    WheelRecyclerAdapter adapter;
    WheelRecyclerViewType recyclerViewType = DEFAULT_TYPE;

    //이 RecyclerView가 보여주는 범위를 저장한다.
    int startDateValue = 1;
    int endDateValue = END_DEFAULT_VALUE;


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

        adapter = new WheelRecyclerAdapter(recyclerViewType);
        this.setAdapter(adapter);

        selectedLabelColor = context.getColor(R.color.colorDatePickerSelectedLabel);
        nonSelectedLabelColor = context.getColor(R.color.colorDatePickerNoSelectedLabel);

        attachSnapHelperWithListener(new LinearSnapHelper(),
                WheelSnapScrollListener.Behavior.NOTIFY_ON_SCROLL, this);
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
        //이전에 Snap을 등록했다면 해제 시켜주어야 함
        if (wheelSnapScrollListener != null) {
            wheelSnapScrollListener.helper.attachToRecyclerView(null);
        }

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
        switch (type) {
            case YEAR:
                setRecyclerViewRange(START_YEAR_VALUE, END_YEAR_VALUE);
                break;
            case MONTH:
                setRecyclerViewRange(1, END_MONTH_VALUE);
                break;
            case DAY:
                setRecyclerViewRange(1, END_DAY_OF_MONTH_VALUE);
        }
        adapter.setRecyclerType(type);
    }

    public void setRecyclerViewRange(int startDateValue, int endDateValue) {
        this.startDateValue = startDateValue;
        this.endDateValue = endDateValue;

        adapter.setRange(startDateValue, endDateValue);
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

    /**
     * 교체 주기, 나중에 교체하기 주기 다이얼로그에서 일자를 선택할 때 3개의 item만 보여주기 위한 높이
     *
     * @return 3개의 item 높이
     */
    public float getNoneHeight() {
        float allItemHeight = 0;
        allItemHeight += getSelectedLabelHeight();

        allItemHeight += getSecondLabelHeight() * 2;

        return allItemHeight;
    }

    /**
     * 현재 사용자의 스크롤에 의해서 가운데에 보이는 값을 가져옴
     *
     * @return 가운데에 있는 값
     */
    public int getSnapValue() {
        return adapter.getDate();
    }

    public float getSelectedLabelHeight() {
        TextView text = createItemView(SELECTED_LABEL_SIZE, SELECTED_LABEL_PADDING);
        return getViewMeasuredHeight(text);
    }

    public float getSecondLabelHeight() {
        TextView text = createItemView(SECOND_LABEL_SIZE, NON_SELECTED_LABEL_PADDING);
        return getViewMeasuredHeight(text);
    }

    public float getThirdLabelHeight() {
        TextView text = createItemView(THIRD_LABEL_SIZE, NON_SELECTED_LABEL_PADDING);
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
     * 커스텀 뷰의 높이를 측정하는 오버라이드 함수
     * setMeasuredDimension 함수를 통해 매개변수의 넓이, 가로를 강제 설정
     *
     * @param widthSpec  : 넓이
     * @param heightSpec : 높이
     */
    @Override
    protected void onMeasure(int widthSpec, int heightSpec) {
        int height = (int) getMaxHeight();
        if (recyclerViewType == WheelRecyclerViewType.NONE) {
            height = (int) getNoneHeight();
        }
        setMeasuredDimension(widthSpec, height);
    }

    @Override
    public void onSnapPositionChange(RecyclerView recyclerView, int position) {
        this.adapter.setCenterPosition(position);
    }

    /**
     * WheelRecyclerView는 범위 이외에도 양 끝에 2개의 빈공간을 가지고 있습니다. (second + third)
     * 범위가 1 ~ 12라고 하고 5개의 Item을 보여준다고 했을 때 snapPosition으로 1이 가르키려고 하면 1위에 2개의 공간이 있어야 합니다.
     * 이를 위해 String이 비어있는 2개의 공간을 양 끝에 넣어줍니다.
     */
    class WheelRecyclerAdapter extends RecyclerView.Adapter<WheelRecyclerAdapter.WheelViewHolder> {
        //빈공간의 수를 상수로 지정
        private final int defaultPosition = 1;

        private int emptySpace = defaultPosition;

        private WheelRecyclerViewType type;

        int centerPosition = defaultPosition;
        int startDateValue = 1;
        int endDateValue = END_DEFAULT_VALUE;

        WheelRecyclerAdapter(WheelRecyclerViewType type) {
            this.type = type;
        }

        public int getEmptySpace() {
            return emptySpace;
        }

        /**
         *  type이 None이 아닐 경우 emptySpace를 2로 설정한다. 보여주어야 하는 Item이 5개 이기 때문.
         * @param type : year, month, day, none
         */
        public void setRecyclerType(WheelRecyclerViewType type) {
            this.type = type;
            if (type != WheelRecyclerViewType.NONE) {
                this.emptySpace = 2;
                this.centerPosition = 2;
            }
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
        }

        /**
         * centerPosition과 그 양 옆 2개의 Item만 변경해준다.
         *
         * @param centerPosition : Selected position
         */
        void setCenterPosition(int centerPosition) {
            this.centerPosition = centerPosition;
            this.notifyItemRangeChanged(centerPosition - emptySpace, centerPosition + emptySpace);
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
                textSize = SELECTED_LABEL_SIZE;
                padding = SELECTED_LABEL_PADDING;
            } else {
                if (Math.abs(centerPosition - position) == SECOND_LABEL_POSITION) { //가운데 포지션과 차이가 1이라면
                    textSize = SECOND_LABEL_SIZE;
                } else {
                    textSize = THIRD_LABEL_SIZE;
                }
                textColor = nonSelectedLabelColor;
                padding = NON_SELECTED_LABEL_PADDING;
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
            int dateValue = startDateValue + position;
            switch (type) {
                case YEAR:
                    return getYearString(dateValue);
                case MONTH:
                    return getMonthString(dateValue);
                case DAY:
                    return getDayString(dateValue);
                default:
                    return dateValue + "";
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
        YEAR, MONTH, DAY, NONE
    }
}
