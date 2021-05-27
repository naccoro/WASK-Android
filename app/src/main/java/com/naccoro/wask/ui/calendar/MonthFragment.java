package com.naccoro.wask.ui.calendar;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.naccoro.wask.R;
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
        Bundle args = getArguments();
        Date selectDate = (Date)args.getSerializable("date");
        calendarModel.updateCalendarList(selectDate, dateList -> initCalendarList(dateList));
        recyclerView = view.findViewById(R.id.recyclerview_calender);
        dayAdapter = new DayAdapter(context, dayItems, Injection.replacementHistoryRepository(context), selectDate);
        gridLayoutManager = new GridLayoutManager(context, 7);
        // TODO: 5/18/21 아이템 개수에 맞춰 높이 설정하기
        recyclerView.setAdapter(dayAdapter);
        recyclerView.setLayoutManager(gridLayoutManager);
    }

    public void initCalendarList(ArrayList<DayItem> dayItems) { this.dayItems = dayItems; }
}
