package com.naccoro.wask.setting;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.naccoro.wask.R;
import com.naccoro.wask.customview.PeriodPresenter;
import com.naccoro.wask.customview.WaskToolbar;
import com.naccoro.wask.customview.datepicker.wheel.WheelRecyclerView;
import com.naccoro.wask.customview.waskdialog.WaskDialog;
import com.naccoro.wask.customview.waskdialog.WaskDialogBuilder;
import com.naccoro.wask.notification.ServiceUtil;
import com.naccoro.wask.preferences.SettingPreferenceManager;
import com.naccoro.wask.preferences.SharedPreferenceManager;
import com.naccoro.wask.utils.AlarmUtil;

public class SettingActivity extends AppCompatActivity
        implements SettingContract.View, View.OnClickListener {

    //마스크 교체 주기
    private PeriodPresenter replacementCycleAlertLabel;
    //나중에 교체하기
    private PeriodPresenter replaceLaterLabel;
    //푸시 알람
    private TextView pushAlertLabel;
    //포그라운드 서비스 알람
    private SwitchCompat alertVisibleSwitch;

    private WaskToolbar toolbar;

    private SettingPresenter presenter;

    private int periodReplacementCycle;

    private int periodReplaceLater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        presenter = new SettingPresenter(this);

        init();

        //start()함수를 호출하여 초기 설정값을 불러옴
        presenter.start();

        //영구 알림 스위치 리스너 초기화
        initSwitchListener();
    }

    private void init() {
        replacementCycleAlertLabel = findViewById(R.id.periodpresenter_replacementcyclealert_body);
        replaceLaterLabel = findViewById(R.id.periodpresenter_replacelater_body);
        pushAlertLabel = findViewById(R.id.textview_pushalert_body);
        toolbar = findViewById(R.id.wasktoolbar_setting);

        findViewById(R.id.constraintlayout_replacementcyclealert).setOnClickListener(this);

        findViewById(R.id.constraintlayout_replacelater).setOnClickListener(this);

        findViewById(R.id.constraintlayout_pushalert).setOnClickListener(this);

        findViewById(R.id.imagebutton_replacelater_info).setOnClickListener(this);

        alertVisibleSwitch = findViewById(R.id.switch_foregroundalert);

        toolbar.setBackButton(() -> presenter.clickHomeButton());
        toolbar.setLeftSideTitle(getString(R.string.setting_title));
    }

    private void initSwitchListener() {
        alertVisibleSwitch.setOnCheckedChangeListener((compoundButton, isChecked) ->
                presenter.changeAlertVisibleSwitch(isChecked));
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
                    presenter.changeReplacementCycleValue(wheelRecyclerView.getWheelValue());
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
                    presenter.changeReplaceLaterValue(wheelRecyclerView.getWheelValue());
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
                    presenter.changePushAlertValue(SettingPreferenceManager.SettingPushAlertType.SOUND);
                    dialog.dismiss();
                })
                .addVerticalButton(getString(R.string.setting_push_alert_vibration), (dialog, view) -> {
                    presenter.changePushAlertValue(SettingPreferenceManager.SettingPushAlertType.VIBRATION);
                    dialog.dismiss();
                })
                .addVerticalButton(getString(R.string.setting_push_alert_all), (dialog, view) -> {
                    presenter.changePushAlertValue(SettingPreferenceManager.SettingPushAlertType.ALL);
                    dialog.dismiss();
                })
                .addVerticalButton(getString(R.string.setting_push_alert_none), (dialog, view) -> {
                    presenter.changePushAlertValue(SettingPreferenceManager.SettingPushAlertType.NONE);
                    dialog.dismiss();
                })
                .build()
                .show(getSupportFragmentManager(), "replaceLater");
    }

    @Override
    public void showReplacementCycleValue(int cycleValue) {
        periodReplacementCycle = cycleValue;
        replacementCycleAlertLabel.setPeriod(cycleValue);
    }

    @Override
    public void showReplaceLaterValue(int laterValue) {
        periodReplaceLater = laterValue;
        replaceLaterLabel.setPeriod(laterValue);
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

        ServiceUtil.showForegroundService(this, maskPeriod);

        AlarmUtil.setForegroundAlarm(this);
    }

    /**
     * 사용자가 마스크 사용 일자 알림바 ( foreground ) 스위치를 Off 했을 때
     */
    @Override
    public void dismissForegroundAlert() {
        ServiceUtil.dismissForegroundService(this);
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
    public void showSnoozeInfoDialog() {
        new WaskDialogBuilder()
                .setTitle("나중에 교체하기", true)
                .setContent(R.layout.layout_snooze_info)
                .addVerticalButton("확인", (dialog, view) -> dialog.dismiss())
                .build()
                .show(getSupportFragmentManager(), "snooze_info");
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
                break;

            case R.id.imagebutton_replacelater_info:
                showSnoozeInfoDialog();
                break;
        }
    }
}
