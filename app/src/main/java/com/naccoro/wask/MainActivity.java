package com.naccoro.wask;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    ImageView settingButton;
    ImageView calendarButton;
    ImageView emotionImageView;
    TextView cardMessageTextView;
    TextView usePeriodTextView;
    Button changeButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
    }

    private void initView() {
        settingButton = findViewById(R.id.imageview_setting);
        calendarButton = findViewById(R.id.imageview_calendar);
        emotionImageView = findViewById(R.id.imageview_emotion);
        cardMessageTextView = findViewById(R.id.textview_card_message);
        usePeriodTextView = findViewById(R.id.textview_use_period);
        changeButton = findViewById(R.id.button_change);

        settingButton.setOnClickListener(this);
        calendarButton.setOnClickListener(this);
        changeButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.imageview_setting:
                // 셋팅 액티비티로 이동
                break;
            case R.id.imageview_calendar:
                // 캘린더 액티비티로 이동
                break;
            case R.id.button_change:
                // '교체하기' 버튼을 눌렀을 때
                break;
        }
    }
}