package com.naccoro.wask.setting;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.naccoro.wask.R;
import com.naccoro.wask.customview.datepicker.wheel.WheelRecyclerView;
import com.naccoro.wask.customview.waskdialog.WaskDialog;
import com.naccoro.wask.customview.waskdialog.WaskDialogBuilder;
import com.naccoro.wask.notification.WaskService;
import com.naccoro.wask.utils.AlarmUtil;

public class SettingActivity extends AppCompatActivity
        implements SettingContract.View, View.OnClickListener {

    //마스크 교체 주기
    private TextView replacementCycleAlertLabel;
    //나중에 교체하기
    private TextView replaceLaterLabel;
    //푸시 알람
    private TextView pushAlertLabel;
    //포그라운드 서비스 알람
    private Switch alertVisibleSwitch;

    private SettingPresenter presenter;

    private int periodReplacementCycle;

    private int periodReplaceLater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        presenter = new SettingPresenter(this);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(R.string.setting_title);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        init();

        //start()함수를 호출하여 초기 설정값을 불러옴
        presenter.start();
    }

    private void init() {
        replacementCycleAlertLabel = findViewById(R.id.textview_replacementcyclealert_body);
        replaceLaterLabel = findViewById(R.id.textview_replacelater_body);
        pushAlertLabel = findViewById(R.id.textview_pushalert_body);

        findViewById(R.id.constraintlayout_replacementcyclealert).setOnClickListener(this);

        findViewById(R.id.constraintlayout_replacelater).setOnClickListener(this);

        findViewById(R.id.constraintlayout_pushalert).setOnClickListener(this);

        alertVisibleSwitch = findViewById(R.id.switch_foregroundalert);

        alertVisibleSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                presenter.changeAlertVisibleSwitch(SettingActivity.this, isChecked);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                presenter.clickHomeButton();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void showReplacementCycleDialog() {
        new WaskDialogBuilder()
                .setTitle(getString(R.string.setting_replacement_cycle))
                .setContent(R.layout.dialog_replacementcycle)
                .setContentCallback(new WaskDialog.ContentViewCallback() {
                    @Override
                    public void onContentViewAttached(View view) {
                        WheelRecyclerView wheelRecyclerView = view.findViewById(R.id.wheelrecycler_replacementcycle);
                        wheelRecyclerView.setInitPosition(periodReplacementCycle);
                    }
                })
                .addHorizontalButton(getString(R.string.setting_dialog_ok), (dialog, view) -> {
                    //이후 wheelPicker value로 대체
                    WheelRecyclerView wheelRecyclerView = view.findViewById(R.id.wheelrecycler_replacementcycle);
                    presenter.changeReplacementCycleValue(SettingActivity.this, wheelRecyclerView.getWheelValue());
                    dialog.dismiss();
                })
                .build()
                .show(getSupportFragmentManager(), "replacementCycle");
    }

    @Override
    public void showReplaceLaterDialog() {

        new WaskDialogBuilder()
                .setTitle(getString(R.string.setting_replace_later))
                .setContent(R.layout.dialog_replacelater)
                .setContentCallback(new WaskDialog.ContentViewCallback() {
                    @Override
                    public void onContentViewAttached(View view) {
                        WheelRecyclerView wheelRecyclerView = view.findViewById(R.id.wheelrecycler_replacelater);
                        wheelRecyclerView.setInitPosition(periodReplaceLater);
                    }
                })
                .addHorizontalButton(getString(R.string.setting_dialog_ok), (dialog, view) -> {
                    //이후 wheelPicker value로 대체
                    WheelRecyclerView wheelRecyclerView = view.findViewById(R.id.wheelrecycler_replacelater);
                    presenter.changeReplaceLaterValue(SettingActivity.this, wheelRecyclerView.getWheelValue());
                    dialog.dismiss();
                })
                .build()
                .show(getSupportFragmentManager(), "replaceLater");
    }

    @Override
    public void showPushAlertDialog() {
        new WaskDialogBuilder()
                .setTitle(getString(R.string.setting_push_alert))
                .addVerticalButton(getString(R.string.setting_push_alert_sound), (dialog, view) -> {
                    presenter.changePushAlertValue(this, getString(R.string.setting_push_alert_sound));
                    dialog.dismiss();
                })
                .addVerticalButton(getString(R.string.setting_push_alert_vibration), (dialog, view) -> {
                    presenter.changePushAlertValue(this, getString(R.string.setting_push_alert_vibration));
                    dialog.dismiss();
                })
                .addVerticalButton(getString(R.string.setting_push_alert_all), (dialog, view) -> {
                    presenter.changePushAlertValue(this, getString(R.string.setting_push_alert_all));
                    dialog.dismiss();
                })
                .addVerticalButton(getString(R.string.setting_push_alert_none), (dialog, view) -> {
                    presenter.changePushAlertValue(this, getString(R.string.setting_push_alert_none));
                    dialog.dismiss();
                })
                .build()
                .show(getSupportFragmentManager(), "replaceLater");
    }

    @Override
    public void showReplacementCycleValue(int cycleValue) {
        periodReplacementCycle = cycleValue;
        replacementCycleAlertLabel.setText(cycleValue + "일");
    }

    @Override
    public void showReplaceLaterValue(int laterValue) {
        periodReplaceLater = laterValue;
        replaceLaterLabel.setText(laterValue + "일");
    }

    @Override
    public void showPushAlertValue(String pushAlertValue) {
        pushAlertLabel.setText(pushAlertValue);
    }

    /**
     * 사용자가 마스크 사용 일자 알림바 ( foreground ) 스위치를 On 했을 때
     *
     * @param maskPeriod : 마스크를 착용한 기간
     */
    @Override
    public void showForegroundAlert(int maskPeriod) {

        AlarmUtil.showForegroundService(this, maskPeriod);

        AlarmUtil.setForegroundAlarm(this);
    }

    /**
     * 사용자가 마스크 사용 일자 알림바 ( foreground ) 스위치를 Off 했을 때
     */
    @Override
    public void dismissForegroundAlert() {
        AlarmUtil.dismissForegroundService(this);
    }

    @Override
    public void setAlertVisibleSwitchValue(boolean isChecked) {
        alertVisibleSwitch.setChecked(isChecked);
    }

    @Override
    public void finishSettingView() {
        finish();
        overridePendingTransition(R.anim.slide_activity_fadein, R.anim.slide_activity_fadeout);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.constraintlayout_replacementcyclealert:
                presenter.clickReplacementCycle();
                break;

            case R.id.constraintlayout_replacelater:
                presenter.clickReplaceLater();
                break;

            case R.id.constraintlayout_pushalert:
                presenter.clickPushAlert();
        }
    }
}
