package com.naccoro.wask.ui.calendar;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.naccoro.wask.R;
import com.naccoro.wask.WaskApplication;
import com.naccoro.wask.replacement.model.Injection;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MonthFragment extends Fragment {
    CalendarModel calendarModel;

    RecyclerView recyclerView;
    GridLayoutManager gridLayoutManager;
    DayAdapter dayAdapter;

    Context context;

    Date selectDate;
    ArrayList<DayItem> dayItems = new ArrayList<>();

    /**
     * 안전하게 activity의 context 저장.
     * @param context
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        calendarModel = new CalendarModel(Injection.replacementHistoryRepository(context));
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_month, container, false);
        init(view);
        return view;
    }

    private void init(View view) {
        updateDayItems();
        recyclerView = view.findViewById(R.id.recyclerview_calender);
        dayAdapter = new DayAdapter(context, dayItems, Injection.replacementHistoryRepository(context), selectDate, getCalendarItemHeight());
        gridLayoutManager = new GridLayoutManager(context, 7);
        recyclerView.setAdapter(dayAdapter);
        recyclerView.setLayoutManager(gridLayoutManager);
    }

    /**
     * selectDate에 맞춰 dayItems 설정
     */
    private void updateDayItems() {
        Bundle args = getArguments();
        selectDate = (Date)args.getSerializable("date");
        int position = args.getInt("position");
        calendarModel.updateDateByPosition(selectDate, position, date -> selectDate = date);
        calendarModel.updateCalendarList(selectDate, dateList -> initCalendarList(dateList));
    }

    public void initCalendarList(ArrayList<DayItem> dayItems) { this.dayItems = dayItems; }

    /**
     * @return 적당한 캘린더의 item의 높이
     */
    private int getCalendarItemHeight() {
        // TODO: 6/8/21 네비게이션바를 제외한 레이아웃의 크기를 구하지 못해서 '0.76' 으로 하드코딩한 모습.. (원래의도 : 화면크기*0.8)
        int itemHeight = (int)((context.getResources().getDisplayMetrics().heightPixels - WaskApplication.toolbarHeight) * 0.72 / (dayItems.size()/7));
        return itemHeight;
    }
}
