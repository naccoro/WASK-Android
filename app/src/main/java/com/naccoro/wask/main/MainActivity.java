package com.naccoro.wask.main;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.naccoro.wask.R;
import com.naccoro.wask.setting.SettingActivity;

public class MainActivity extends AppCompatActivity
        implements View.OnClickListener, MainContract.View {
    MainPresenter presenter;

    ImageView settingButton;
    ImageView calendarButton;
    ImageView emotionImageView;
    TextView cardMessageTextView;
    TextView usePeriodTextView;
    TextView changeButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        presenter = new MainPresenter(this);
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
        switch (view.getId()) {
            case R.id.imageview_setting:
                // 셋팅 액티비티로 이동
                presenter.clickSettingButton();
                break;
            case R.id.imageview_calendar:
                // 캘린더 액티비티로 이동
                break;
            case R.id.button_change:
                // '교체하기' 버튼을 눌렀을 때 (버튼 동작을 확인하기위해 toast메시지를 띄워놓았습니다.)
                Toast.makeText(this.getApplicationContext(), "교체되었습니다.", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @Override
    public void showSettingView() {
        Intent intent = new Intent(MainActivity.this, SettingActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_activity_fadein, R.anim.slide_activity_fadeout);
    }
}