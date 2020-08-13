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

        setCalendarList();
        calendarAdapter.notifyDataSetChanged();
    }

    private void initView() {
        backButton = findViewById(R.id.imageview_back);
        calendarDateTextView = findViewById(R.id.textView_calendar_date);

        backButton.setOnClickListener(this);
        calendarDateTextView.setOnClickListener(this);

        initSelectDate();
        updateCalendarDateTextView();

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
            case R.id.textView_calendar_date:
                // date picker 실행
                // selectDate 바꾸고
                setCalendarList();
                calendarAdapter.setCalendarList(dateList); // 갱신
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

    /**
     * selectDate를 앱 실행 날짜로 초기화
     */
    private void initSelectDate() {
        GregorianCalendar cal = new GregorianCalendar();
        selectDate = new GregorianCalendar(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DATE), 0, 0, 0); // 선택날짜 (초기화:오늘)
    }

    /**
     * 화면에 표시되는 달력 데이터 설정
     */
    private void setCalendarList() {
        dateList.clear();
        try {
            GregorianCalendar calendar = new GregorianCalendar(selectDate.get(Calendar.YEAR), selectDate.get(Calendar.MONTH), 1, 0, 0, 0);

            int startDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1; // 해당 월에 시작하는 요일
            int lasyDayOfMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH); // 이번 달의 말일

            // 지난달 마지막 날짜
            calendar.add(Calendar.MONTH, - 1);
            int lastDayOfPreMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

            // 지난 달 저장
            for (int j = startDayOfWeek - 1; j >= 0; j--) {
                GregorianCalendar item = new GregorianCalendar(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), lastDayOfPreMonth - j);
                dateList.add(new CalendarItem(false, false, item));
            }

            // 이번 달 저장
            calendar.add(Calendar.MONTH, + 1); // 이번달로 돌아옴
            for (int j = 1; j <= lasyDayOfMonth; j++) {
                GregorianCalendar item = new GregorianCalendar(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), j);
                if (selectDate.get(Calendar.DATE) == j) {
                    // today
                    dateList.add(new CalendarItem(true, true, item));
                } else {
                    dateList.add(new CalendarItem(false, true, item));
                }
            }

            // 다음 달 저장
            calendar.add(Calendar.MONTH, + 1); // 다음달
            int nextMonthDay = 42 - startDayOfWeek - lasyDayOfMonth;
            for (int j = 1; j <= nextMonthDay; j++) {
                GregorianCalendar item = new GregorianCalendar(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, j);
                dateList.add(new CalendarItem(false, false, item));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * date picker에서 설정된 날짜(selectDate)기준으로 0000년 00월 표시 설정
     */
    private void updateCalendarDateTextView() {
        int month = selectDate.get(Calendar.MONTH) + 1;
        calendarDateTextView.setText(selectDate.get(Calendar.YEAR) + "년 " + month + "월");
    }

}
