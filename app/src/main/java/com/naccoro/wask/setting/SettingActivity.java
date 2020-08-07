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

public class SettingActivity extends AppCompatActivity implements SettingContract.View {

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

        findViewById(R.id.replacement_cycle_alert_layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.clickReplacementCycle();
            }
        });

        findViewById(R.id.replace_later_layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.clickReplaceLater();
            }
        });

        findViewById(R.id.push_alert_layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.clickPushAlert();
            }
        });

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
                    dialog.dismiss();
                })
                .addVerticalButton(getString(R.string.setting_push_alert_vibration), (dialog, view) -> {
                    dialog.dismiss();
                })
                .addVerticalButton(getString(R.string.setting_push_alert_all), (dialog, view) -> {
                    dialog.dismiss();
                })
                .addVerticalButton(getString(R.string.setting_push_alert_none), (dialog, view) -> {
                    dialog.dismiss();
                })
                .build()
                .show(getSupportFragmentManager(), "replaceLater");
    }
}