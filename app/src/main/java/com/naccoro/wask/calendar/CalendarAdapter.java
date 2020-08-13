package com.naccoro.wask.calendar;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import static com.naccoro.wask.R.*;

public class CalendarAdapter extends RecyclerView.Adapter<CalendarAdapter.CalendarViewHolder> {

    private ArrayList<CalendarItem> calendarList;

    public CalendarAdapter(ArrayList<CalendarItem> calendarList) {
        this.calendarList = calendarList;
    }

    public void setCalendarList(ArrayList<CalendarItem> calendarList) {
        this.calendarList = calendarList;
        notifyDataSetChanged();
    }

    /**
     * 아이템 뷰를 넣기 위한 view holder 생성
     *
     * @param parent
     * @param viewType
     * @return
     */
    @NonNull
    @Override
    public CalendarAdapter.CalendarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext() ;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) ;

        View view = inflater.inflate(layout.calendar_item, parent, false) ;
        CalendarAdapter.CalendarViewHolder viewHolder = new CalendarAdapter.CalendarViewHolder(view) ;

        return viewHolder ;
    }

    /**
     * position에 해당하는 data를 view holder에 넣기
     *
     * @param calendarViewHolder
     * @param position
     */
    @Override
    public void onBindViewHolder(@NonNull CalendarViewHolder calendarViewHolder, int position) {

        View itemView = calendarViewHolder.getItemView();

        CalendarItem item = calendarList.get(position);
        calendarViewHolder.dateTextView.setText(item.getDate().get(Calendar.DAY_OF_MONTH) + "");

        // 일요일은 빨간날
        if (position%7==0) {
            calendarViewHolder.dateTextView.setTextColor(itemView.getResources().getColor(color.waskRed));
        }
        // 오늘이면 동그라미 표시
        if (item.isToday()) {
            calendarViewHolder.dateTextView.setTextColor(itemView.getResources().getColor(color.white));
            calendarViewHolder.dateBackgroundImageView.setVisibility(itemView.getVisibility());
        }

        //지난 달과 다음 달의 날짜는 흐리게
        if (!item.isCurrentMonth()) {
            calendarViewHolder.dateTextView.setAlpha(0.4f);
        } else {
            calendarViewHolder.dateTextView.setAlpha(1.0f);
        }
    }

    /**
     * item 몇 개 생성되는지
     * 제대로 생성되지 않으면 오류가 난다고 합니다.
     *
     * @return item 개수 반환
     */
    @Override
    public int getItemCount() {
        if (calendarList != null) {
            return calendarList.size();
        }
        return 0;
    }

    /**
     * calendar view holder
     */
    public class CalendarViewHolder extends RecyclerView.ViewHolder {
        private TextView dateTextView;
        private ImageView dateBackgroundImageView;
        private ImageView changeImageView;
        private View itemView;

        public CalendarViewHolder(@NonNull View itemView) {
            super(itemView);
            this.itemView = itemView;

            initView(itemView);
        }

        public void initView(View v) {
            dateTextView = v.findViewById(id.textView_date);
            dateBackgroundImageView = v.findViewById(id.imageView_date_background);
            changeImageView = v.findViewById(id.imageView_change);
        }

        public View getItemView() {
            return itemView;
        }
    }
}
