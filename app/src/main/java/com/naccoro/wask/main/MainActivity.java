package com.naccoro.wask.main;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.naccoro.wask.R;
import com.naccoro.wask.calendar.CalendarActivity;
import com.naccoro.wask.customview.waskdialog.WaskDialogBuilder;
import com.naccoro.wask.replacement.model.Injection;
import com.naccoro.wask.setting.SettingActivity;

public class MainActivity extends AppCompatActivity
        implements View.OnClickListener, MainContract.View {
    MainPresenter presenter;

    ImageView settingButton;
    ImageView calendarButton;
    ImageView emotionImageView;
    TextView cardMessageTextView;
    TextView usePeriodTextView;
    TextView usePeriodMessageTextView;
    TextView changeButton;

    @Override
    protected void onStart() {
        super.onStart();
        presenter.start();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        presenter = new MainPresenter(this, Injection.replacementHistoryRepository(getApplicationContext()));
        initView();
    }

    private void initView() {
        settingButton = findViewById(R.id.imageview_setting);
        calendarButton = findViewById(R.id.imageview_calendar);
        emotionImageView = findViewById(R.id.imageview_emotion);
        cardMessageTextView = findViewById(R.id.textview_card_message);
        usePeriodTextView = findViewById(R.id.textview_use_period);
        usePeriodMessageTextView = findViewById(R.id.textview_use_period_message);
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
                presenter.clickCalendarButton();
                break;
            case R.id.button_change:

                //교체하기 로직
                presenter.changeMask(this);
                break;
        }
    }

    @Override
    public void showReplaceToast() {
        Toast.makeText(this.getApplicationContext(), "교체되었습니다.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void enableReplaceButton() {
        changeButton.setText("교체하기");
        changeButton.setBackgroundTintList(null);
    }

    @Override
    public void disableReplaceButton() {
        changeButton.setText("교체 취소");
        changeButton.setBackgroundTintList(getResources().getColorStateList(R.color.dividerGray, null));
    }

    @Override
    public void showSettingView() {
        Intent intent = new Intent(MainActivity.this, SettingActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_activity_fadein, R.anim.slide_activity_fadeout);
    }

    @Override
    public void showCalendarView() {
        Intent intent = new Intent(MainActivity.this, CalendarActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_activity_fadein, R.anim.slide_activity_fadeout);
    }


    /**
     * 1일 1개 마스크를 사용하고 있다고 칭찬의 화면을 보여준다.
     */
    @Override
    public void showGoodMainView() {
        emotionImageView.setImageResource(R.drawable.ic_good);

        int textColor = getColor(R.color.waskBlue);
        String message = getString(R.string.main_card_good);
        cardMessageTextView.setTextColor(textColor);
        cardMessageTextView.setText(message);

        usePeriodTextView.setTextColor(textColor);
    }

    /**
     * 1일이 지나도록 마스크를 교체하지 않은 사용자에게 경고하는 화면을 보여준다.
     */
    @Override
    public void showBadMainView() {
        emotionImageView.setImageResource(R.drawable.ic_bad);

        int textColor = getColor(R.color.waskRed);
        String message = getString(R.string.main_card_bad);
        cardMessageTextView.setTextColor(textColor);
        cardMessageTextView.setText(message);

        usePeriodTextView.setTextColor(textColor);
    }

    @Override
    public void setPeriodTextValue(int period) {
        usePeriodTextView.setText(period + "");
    }

    @Override
    public void showCancelDialog() {
        new WaskDialogBuilder()
                .setMessage(getString(R.string.dialog_cancelreplacement))
                .addHorizontalButton("취소", (dialog, view) -> dialog.dismiss())
                .addHorizontalButton("확인", ((dialog, view) -> {
                    presenter.cancelChanging(MainActivity.this);
                    dialog.dismiss();
                }))
                .build()
                .show(getSupportFragmentManager(), "cancel");
    }

    @Override
    public void showNoReplaceData() {
        usePeriodTextView.setVisibility(View.GONE);
        usePeriodMessageTextView.setText(R.string.main_use_period_message_no_replacement);
    }

    @Override
    public void changeUsePeriodMessage() {
        usePeriodTextView.setVisibility(View.VISIBLE);
        usePeriodMessageTextView.setText(R.string.main_use_period_message);
    }
}