package com.naccoro.wask.calendar;

import android.icu.util.Calendar;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.naccoro.wask.R;
import com.naccoro.wask.customview.datepicker.DatePickerDialogFragment;
import com.naccoro.wask.replacement.model.Injection;

import java.util.ArrayList;
import java.util.GregorianCalendar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class CalendarActivity extends AppCompatActivity
        implements View.OnClickListener, CalendarContract.View {

    ImageView backButton;
    TextView calendarDateTextView;
    ConstraintLayout changeDateConstraintLayout;
    Switch modifyModeSwitch;
    TextView modifyModeTextView;

    RecyclerView recyclerView;
    CalendarAdapter calendarAdapter;
    GridLayoutManager gridLayoutManager;

    Date selectDate; // DatePicker로 선택된 날짜 <Calendar에서 핵심역할>

    // 화면에 표시되는 달력 데이터 저장 (저번달 짜투리 + 이번달(1~30) + 다음달 조금)
    ArrayList<CalendarItem> dateList = new ArrayList<CalendarItem>();

    private CalendarPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        presenter = new CalendarPresenter(this, Injection.replacementHistoryRepository(this));

        initView();
    }

    private void initView() {
        backButton = findViewById(R.id.imageview_back);

        calendarDateTextView = findViewById(R.id.textview_calendar_date);
        changeDateConstraintLayout = findViewById(R.id.constraintlayout_changedate);
        modifyModeSwitch = findViewById(R.id.switch_calendar_modify);
        modifyModeTextView = findViewById(R.id.textview_calendar_modify);
        recyclerView = findViewById(R.id.recyclerview_calender);

        backButton.setOnClickListener(this);
        changeDateConstraintLayout.setOnClickListener(this);

        // 스위치 모드 변경
        modifyModeSwitch.setOnCheckedChangeListener((compoundButton, isChecked) -> presenter.changeModifyMode(isChecked, calendarAdapter));

        // calendar 관련 설정
        initSelectDate();
        presenter.changeCalendarDateTextView(selectDate);
        presenter.changeCalendarList(selectDate);

        // 리사이클러뷰 초기화
        calendarAdapter = new CalendarAdapter(dateList, Injection.replacementHistoryRepository(getApplicationContext()), selectDate);
        gridLayoutManager = new GridLayoutManager(this, 7);
        recyclerView.setAdapter(calendarAdapter);
        recyclerView.setLayoutManager(gridLayoutManager);
    }

    /**
     * selectDate를 앱 실행 날짜로 초기화
     */
    private void initSelectDate() {
        GregorianCalendar cal = new GregorianCalendar();
        selectDate = new Date(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DATE));
    }

    /**
     * 화면에 표시되는 달력 데이터 초기화
     *
     * @param calendarItems model에서 만들어진 달력 데이터
     */
    @Override
    public void initCalendarList(ArrayList<CalendarItem> calendarItems) {
        dateList = calendarItems;
    }

    /**
     * 수정모드에 따라 '수정모드' 색상 변경
     *
     * @param isChecked
     */
    @Override
    public void showModifyModeTextView(boolean isChecked) {
        if (isChecked) {
            modifyModeTextView.setTextColor(getColor(R.color.waskBlue));
        } else {
            modifyModeTextView.setTextColor(getColor(R.color.colorDatePickerNoSelectedLabel));
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imageview_back: // 뒤로가기버튼
                presenter.clickBackButton();
                break;
            case R.id.constraintlayout_changedate: // (0000년 00월) 버튼 클릭 시
                DatePickerDialogFragment.newInstance().
                        setOnDateChangedListener((year, month, day) -> {
                            selectDate.setDate(year, month, day);
                            presenter.clickChangeDateButton(selectDate);
                            calendarAdapter.setCalendarList(dateList);
                        })
                        .setDate(selectDate.getYear(), selectDate.getMonth(), selectDate.getDay())
                        .show(getSupportFragmentManager(), "dialog");
                break;
        }
    }

    /**
     * date picker에서 설정된 날짜(selectDate)기준으로 0000년 00월 표시 설정
     */
    @Override
    public void showCalendarDateTextView(int month) {
        calendarDateTextView.setText(selectDate.getYear() + "년 " + month + "월");
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
     * SelectDate를 저장하기 위한 클래스 (year, month, day 저장)
     */
    public static class Date {
        private int year;
        private int month;
        private int day;

        public Date(int year, int month, int day) {
            this.year = year;
            this.month = month;
            this.day = day;
        }

        public void setDate(int year, int month, int day) {
            this.year = year;
            this.month = month;
            this.day = day;
        }

        public int getYear() {
            return year;
        }

        public void setYear(int year) {
            this.year = year;
        }

        public int getMonth() {
            return month;
        }

        public void setMonth(int month) {
            this.month = month;
        }

        public int getDay() {
            return day;
        }

        public void setDay(int day) {
            this.day = day;
        }
    }
}
