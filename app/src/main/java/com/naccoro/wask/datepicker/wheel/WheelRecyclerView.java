package com.naccoro.wask.datepicker.wheel;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.text.Layout;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import com.naccoro.wask.R;

import java.util.List;

public class WheelRecyclerView extends RecyclerView implements WheelSnapScrollListener.OnSnapPositionChangeListener {

    Context context;
    WheelRecyclerAdapter adapter;

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
        LinearLayoutManager manager = new LinearLayoutManager(context);
        this.setLayoutManager(manager);

        adapter = new WheelRecyclerAdapter();
        this.setAdapter(adapter);

        //스크롤시 중앙에 아이탬을 고정하도록 도와주는 snapHelper 적용
        SnapHelper helper = new LinearSnapHelper();
        helper.attachToRecyclerView(this);

        WheelSnapScrollListener wheelSnapScrollListener = new WheelSnapScrollListener(
                helper, WheelSnapScrollListener.Behavior.NOTIFY_ON_SCROLL_STATE_IDLE, this);
        this.addOnScrollListener(wheelSnapScrollListener);
    }


    public void setItemList(List<String> itemList) {
        this.adapter.setItemList(itemList);
    }


    /**
     * Height 값이 다른 5개의 TextView 높이 값을 모두 더해서 총 RecyclerView 높이를 구함
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
     *  recyclerView의 높이를 고정시키기 위해 Height 크기가 다른 TextView 5개를 만들기 위한 함수
     * @param textSize : testSize
     * @param paddingSize : TextView 사이의 간격
     */
    private TextView createItemView(int textSize, int paddingSize) {
        TextView textView = (TextView)LayoutInflater.from(context).inflate(R.layout.date_picker_item, null);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
        int padding = dip2px(paddingSize);
        textView.setPadding(padding, padding, padding, padding);

        return textView;
    }

    /**
     * dp 값을 px로 변경
     * @param dpValue
     * @return px로 변경된 값
     */
    private int dip2px(float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * viwe의 높이를 측정하는 함수
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
     * @param position
     */
    @Override
    public void scrollToPosition(int position) {
        super.scrollToPosition(position);
        adapter.setCenterPosition(position - 2);
    }

    /**
     * snapPosition을 Adapter에 등록하여 Text 스타일 변경
     * @param position 변경된 snapPosition
     */
    @Override
    public void onSnapPositionChange(int position) {
        adapter.setCenterPosition(position);
    }

    class WheelRecyclerAdapter extends RecyclerView.Adapter<WheelRecyclerAdapter.WheelViewHolder> {

        List<String> itemList;
        int centerPosition = -1;

        @NonNull
        @Override
        public WheelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.date_picker_item, parent, false);
            return new WheelViewHolder(view);
        }

        public void setItemList(List<String> itemList) {
            this.itemList = itemList;
        }

        void setCenterPosition(int centerPosition) {
            this.centerPosition = centerPosition;
            this.notifyDataSetChanged();
        }

        @Override
        public void onBindViewHolder(@NonNull WheelViewHolder holder, int position) {
            String itemBody;
            int textColor;
            int textSize;
            int padding;

            if (position >= 2 && itemList.size() + 2 > position) {
                itemBody = itemList.get(position - 2);
            } else {
                itemBody = ""; // recyclerView 양 끝에 빈공간 2개를 생성
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
            holder.itemLabel.setTextSize(TypedValue.COMPLEX_UNIT_SP,  textSize);
            holder.itemLabel.setPadding(padding, padding, padding, padding);
            holder.itemLabel.setText(itemBody);
        }

        @Override
        public int getItemCount() {
            return itemList.size() + 4;
        }

        class WheelViewHolder extends RecyclerView.ViewHolder {
            TextView itemLabel;

          public WheelViewHolder(@NonNull View itemView) {
              super(itemView);
              itemLabel = (TextView)itemView;
          }
      }
    }
}
