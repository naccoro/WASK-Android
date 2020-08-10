package com.naccoro.wask.calendar;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.naccoro.wask.R;

import androidx.appcompat.app.AppCompatActivity;

public class CalendarActivity extends AppCompatActivity
        implements View.OnClickListener, CalendarContract.View {

    ImageView backButton;

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

        backButton.setOnClickListener(this);
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
}
