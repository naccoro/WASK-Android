package com.naccoro.wask.ui.calendar;

import android.os.Bundle;
import android.view.View;
import android.widget.Switch;
import android.widget.TextView;

import com.naccoro.wask.R;
import com.naccoro.wask.customview.DatePresenter;
import com.naccoro.wask.customview.WaskToolbar;
import com.naccoro.wask.customview.datepicker.DatePickerDialogFragment;
import com.naccoro.wask.replacement.model.Injection;

import java.util.Calendar;
import java.util.GregorianCalendar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

public class CalendarActivity extends AppCompatActivity
        implements View.OnClickListener, CalendarContract.View {

    DatePresenter changeDatePresenter;
    Switch modifyModeSwitch;
    TextView modifyModeTextView;

    DayAdapter dayAdapter;

    MonthAdapter monthAdapter;
    ViewPager2 viewPager;

    WaskToolbar toolbar;

    Date selectDate; // DatePicker로 선택된 날짜

    private CalendarPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        presenter = new CalendarPresenter(this, Injection.replacementHistoryRepository(this));

        initView();
    }

    private void initView() {
        changeDatePresenter = findViewById(R.id.datepresenter_changedate);
        modifyModeSwitch = findViewById(R.id.switch_calendar_modify);
        modifyModeTextView = findViewById(R.id.textview_calendar_modify);
        viewPager = findViewById(R.id.viewpager_calendar);
        toolbar = findViewById(R.id.wasktoolbar_calendar);

        changeDatePresenter.setOnClickListener(this);
        modifyModeSwitch.setOnCheckedChangeListener((compoundButton, isChecked) -> presenter.changeModifyMode(isChecked, dayAdapter));
        initCalendar();
        toolbar.setBackButton(() -> presenter.clickBackButton());
    }

    /**
     * 달력 설정
     */
    private void initCalendar() {
        initSelectDate();
        monthAdapter = new MonthAdapter(this);
        presenter.setCalendar(selectDate);
        viewPager.setCurrentItem(monthAdapter.START_POSITION, false);
    }

    /**
     * selectDate를 앱 실행 날짜로 초기화
     */
    private void initSelectDate() {
        GregorianCalendar cal = new GregorianCalendar();
        selectDate = new Date(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DATE));
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
            case R.id.datepresenter_changedate: // (0000년 00월) 버튼 클릭 시
                DatePickerDialogFragment.newInstance().
                        setOnDateChangedListener((year, month, day) -> {
                            selectDate.setDate(year, month, day);
                            presenter.setCalendar(selectDate);
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
    public void showCalendarDateTextView() {
        changeDatePresenter.setDate(selectDate);
    }

    @Override
    public void showCalendarViewPager(Date selectDate) {
        monthAdapter.setDate(selectDate);
        viewPager.setAdapter(monthAdapter);
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
