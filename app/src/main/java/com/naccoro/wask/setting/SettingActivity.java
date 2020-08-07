package com.naccoro.wask.setting;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.naccoro.wask.R;
import com.naccoro.wask.customview.waskdialog.WaskDialogBuilder;

public class SettingActivity extends AppCompatActivity
        implements SettingContract.View, View.OnClickListener {

    //마스크 교체 주기
    TextView replacementCycleAlertLabel;
    //나중에 교체하기
    TextView replaceLaterLabel;
    //푸시 알람
    TextView pushAlertLabel;

    SettingPresenter presenter;

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

        replacementCycleAlertLabel = findViewById(R.id.mask_replacement_cycle_alert);
        replaceLaterLabel = findViewById(R.id.mask_replace_later);
        pushAlertLabel = findViewById(R.id.mask_push_alert);

        findViewById(R.id.replacement_cycle_alert_layout).setOnClickListener(this);

        findViewById(R.id.replace_later_layout).setOnClickListener(this);

        findViewById(R.id.push_alert_layout).setOnClickListener(this);

        Switch alertVisibleSwitch = findViewById(R.id.mask_foreground_alert_visible_switch);

        alertVisibleSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean newValue) {
                presenter.changeAlertVisibleSwitch(newValue);
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
                .addHorizontalButton(getString(R.string.setting_dialog_ok), (dialog, view) -> {
                    //이후 wheelPicker value로 대체
                    int cycleValue = 0;
                    presenter.changeReplacementCycleValue(cycleValue);
                    dialog.dismiss();
                })
                .build()
                .show(getSupportFragmentManager(), "replacementCycle");
    }

    @Override
    public void showReplaceLaterDialog() {
        new WaskDialogBuilder()
                .setTitle(getString(R.string.setting_replace_later))
                .addHorizontalButton(getString(R.string.setting_dialog_ok), (dialog, view) -> {
                    //이후 wheelPicker value로 대체
                    int cycleValue = 0;
                    presenter.changeReplaceLaterValue(cycleValue);
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
                    presenter.changePushAlertValue(getString(R.string.setting_push_alert_sound));
                    dialog.dismiss();
                })
                .addVerticalButton(getString(R.string.setting_push_alert_vibration), (dialog, view) -> {
                    presenter.changePushAlertValue(getString(R.string.setting_push_alert_vibration));
                    dialog.dismiss();
                })
                .addVerticalButton(getString(R.string.setting_push_alert_all), (dialog, view) -> {
                    presenter.changePushAlertValue(getString(R.string.setting_push_alert_all));
                    dialog.dismiss();
                })
                .addVerticalButton(getString(R.string.setting_push_alert_none), (dialog, view) -> {
                    presenter.changePushAlertValue(getString(R.string.setting_push_alert_none));
                    dialog.dismiss();
                })
                .build()
                .show(getSupportFragmentManager(), "replaceLater");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.replacement_cycle_alert_layout:
                presenter.clickReplacementCycle();
                break;

            case R.id.replace_later_layout:
                presenter.clickReplaceLater();
                break;

            case R.id.push_alert_layout:
                presenter.clickPushAlert();
        }
    }
}