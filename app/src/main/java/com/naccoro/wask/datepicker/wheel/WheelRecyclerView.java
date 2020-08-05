package com.naccoro.wask.datepicker.wheel;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.naccoro.wask.R;

import java.util.List;

public class WheelRecyclerView extends RecyclerView {

    Context context;
    WheelRecyclerAdapter adapter;

    private static int DEFAULT_LABEL_SIZE = 15;
    private static int DEFAULT_LABEL_PADDING = 15;

    private final int FIRST_LABEL_POSITION = 0;
    private final int SECOND_LABEL_POSITION = 1;
    private final int THIRD_LABEL_POSITION = 2;

    private final int selectedLabelColor = Color.parseColor("#707070");
    private final int nonSelectedLabelColor = Color.parseColor("#a0a7ad");
    private final int lineColor = Color.parseColor("#707070");

    private final int selectedLabelSize = 20;
    private final int secondLabelSize = 18;
    private final int thirdLabelSize = 15;

    private final int selectedLabelPadding = 15;
    private final int nonSelectedLabelPadding = 15;

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
    }

    public int getMaxHeight() {
        int allItemHeight = 0;
        TextView text = createItemView(selectedLabelSize, selectedLabelPadding);
        allItemHeight += getViewMeasuredHeight(text);
        text = createItemView(secondLabelSize, nonSelectedLabelPadding);
        allItemHeight += getViewMeasuredHeight(text) * 2;
        text = createItemView(thirdLabelSize, nonSelectedLabelPadding);
        allItemHeight += getViewMeasuredHeight(text) * 2;

        return allItemHeight;
    }

    public void setItemList(List<String> itemList) {
        this.adapter.setItemList(itemList);


        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams)this.getLayoutParams();
        this.setLayoutParams(new LinearLayout.LayoutParams(params.width, getMaxHeight()));
    }

    private TextView createItemView(int textSize, int paddingSize) {
        TextView textView = (TextView)LayoutInflater.from(context).inflate(R.layout.date_picker_item, null);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
        int padding = dip2px(paddingSize);
        textView.setPadding(padding, padding, padding, padding);

        return textView;
    }

    private int dip2px(float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    private int getViewMeasuredHeight(View view) {
        int width = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        int expandSpec = View.MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, View.MeasureSpec.AT_MOST);
        view.measure(width, expandSpec);
        return view.getMeasuredHeight();
    }

    class WheelRecyclerAdapter extends RecyclerView.Adapter<WheelRecyclerAdapter.WheelViewHolder> {

        List<String> itemList;

        @NonNull
        @Override
        public WheelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = createItemView(thirdLabelSize, nonSelectedLabelPadding);
            return new WheelViewHolder(view);
        }

        public void setItemList(List<String> itemList) {
            this.itemList = itemList;
        }

        @Override
        public void onBindViewHolder(@NonNull WheelViewHolder holder, int position) {
            holder.itemLabel.setText(itemList.get(position));
        }

        @Override
        public int getItemCount() {
            return itemList.size();
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
