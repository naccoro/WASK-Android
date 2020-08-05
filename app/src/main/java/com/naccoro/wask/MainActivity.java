package com.naccoro.wask;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    ImageView settingBtn;
    ImageView emotionIv;
    TextView cardMessageTv;
    TextView usePeriodTv;
    Button changeBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
    }

    private void initView() {
        settingBtn = findViewById(R.id.btn_setting);
        emotionIv = findViewById(R.id.iv_emotion);
        cardMessageTv = findViewById(R.id.tv_card_message);
        usePeriodTv = findViewById(R.id.tv_use_period);
        changeBtn = findViewById(R.id.btn_change);

        settingBtn.setOnClickListener(this);
        changeBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.btn_setting:
                // 셋팅 액티비티로 이동
                break;
            case R.id.btn_change:
                // '교체하기' 버튼을 눌렀을 때
                break;
        }
    }
}