package com.naccoro.wask.calendar;

import android.icu.util.Calendar;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.naccoro.wask.R;

import java.util.ArrayList;
import java.util.GregorianCalendar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class CalendarActivity extends AppCompatActivity
        implements View.OnClickListener, CalendarContract.View {

    ImageView backButton;
    RecyclerView recyclerView;
    CalendarAdapter calendarAdapter;
    GridLayoutManager gridLayoutManager;

    ArrayList<CalendarItem> dateList = new ArrayList<CalendarItem>();

    private CalendarPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        presenter = new CalendarPresenter(this);

        initView();

        setCalendarList();
        calendarAdapter.notifyDataSetChanged();
    }

    private void initView() {
        backButton = findViewById(R.id.imageview_back);

        backButton.setOnClickListener(this);

        // 리사이클러뷰 초기화
        recyclerView = findViewById(R.id.recyclerview_calender);
        calendarAdapter = new CalendarAdapter(dateList);
        gridLayoutManager = new GridLayoutManager(this, 7);

        recyclerView.setAdapter(calendarAdapter);
        recyclerView.setLayoutManager(gridLayoutManager);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imageview_back:
                presenter.clickBackButton();
                break;
        }
    }

    @Override
    public void finishCalendarView() {
        finish();
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_activity_fadein, R.anim.slide_activity_fadeout);
    }

    public void setCalendarList() {
        dateList.clear();
        GregorianCalendar cal = new GregorianCalendar();
        for (int i = 0; i < 1; i++) {
            try {
                GregorianCalendar calendar = new GregorianCalendar(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + i, 1, 0, 0, 0);

                int startDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1; // 해당 월에 시작하는 요일
                int lasyDayOfMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH); // 해당 월에 마지막 요일

                // 지난 달 저장
//                for (int j = 0; j < startDayOfWeek; j++) {
//                    dateList.add(new CalendarItem(false, false, ))
//                }

                // 이번 달 저장
                for (int j = 1; j <= lasyDayOfMonth; j++) {
                    GregorianCalendar item = new GregorianCalendar(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), j);
                    dateList.add(new CalendarItem(false, false, item));
                }

                // 다음 달 저장
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
