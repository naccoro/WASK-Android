package com.naccoro.wask.setting;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import com.naccoro.wask.R;

public class SettingActivity extends AppCompatActivity {

    //마스크 교체 주기
    TextView replacementCycleAlertLabel;
    //나중에 교체하기
    TextView replaceLaterLabel;
    //푸시 알람
    TextView pushAlertLabel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(R.string.setting_title);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        
        replacementCycleAlertLabel = findViewById(R.id.mask_replacement_cycle_alert);
        replaceLaterLabel = findViewById(R.id.mask_replace_later);
        pushAlertLabel = findViewById(R.id.mask_push_alert);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}