package com.naccoro.wask.calendar;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.naccoro.wask.R;

import java.util.ArrayList;
import java.util.Calendar;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

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

        View view = inflater.inflate(R.layout.calendar_item, parent, false) ;
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
        CalendarItem item = calendarList.get(position);
        calendarViewHolder.dateTextView.setText(item.getDate().get(Calendar.DAY_OF_MONTH) + "");
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

        public CalendarViewHolder(@NonNull View itemView) {
            super(itemView);

            initView(itemView);
        }

        public void initView(View v) {
            dateTextView = v.findViewById(R.id.textView_date);
            dateBackgroundImageView = v.findViewById(R.id.imageView_date_background);
            changeImageView = v.findViewById(R.id.imageView_change);
        }

    }
}
