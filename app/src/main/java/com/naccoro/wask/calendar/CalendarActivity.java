package com.naccoro.wask.calendar;

import android.icu.util.Calendar;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.naccoro.wask.R;

import java.util.ArrayList;
import java.util.GregorianCalendar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class CalendarActivity extends AppCompatActivity
        implements View.OnClickListener, CalendarContract.View {

    ImageView backButton;
    TextView calendarDateTextView;
    RecyclerView recyclerView;
    CalendarAdapter calendarAdapter;
    GridLayoutManager gridLayoutManager;

    GregorianCalendar selectDate; // DatePicker로 선택된 날짜

    ArrayList<CalendarItem> dateList = new ArrayList<CalendarItem>();

    private CalendarPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        presenter = new CalendarPresenter(this);

        initView();
    }

    private void initView() {
        backButton = findViewById(R.id.imageview_back);
        calendarDateTextView = findViewById(R.id.textView_calendar_date);
        recyclerView = findViewById(R.id.recyclerview_calender);

        backButton.setOnClickListener(this);
        calendarDateTextView.setOnClickListener(this);

        initSelectDate();
        presenter.changeCalendarDateTextView(selectDate);
        presenter.changeCalendarList(selectDate);

        // 리사이클러뷰 초기화
        calendarAdapter = new CalendarAdapter(dateList);
        gridLayoutManager = new GridLayoutManager(this, 7);
        recyclerView.setAdapter(calendarAdapter);
        recyclerView.setLayoutManager(gridLayoutManager);
    }

    /**
     * selectDate를 앱 실행 날짜로 초기화
     */
    private void initSelectDate() {
        GregorianCalendar cal = new GregorianCalendar();
        selectDate = new GregorianCalendar(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DATE), 0, 0, 0); // 선택날짜 (초기화:오늘)
    }

    @Override
    public void initCalendarList(ArrayList<CalendarItem> calendarItems) {
        dateList = calendarItems;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imageview_back:
                presenter.clickBackButton();
                break;
            case R.id.textView_calendar_date:
                presenter.clickChangeDateButton();
                calendarAdapter.setCalendarList(dateList); // 갱신
                break;
        }
    }

    /**
     * date picker에서 설정된 날짜(selectDate)기준으로 0000년 00월 표시 설정
     */
    @Override
    public void showCalendarDateTextView(int month) {
        calendarDateTextView.setText(selectDate.get(Calendar.YEAR) + "년 " + month + "월");
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
}
