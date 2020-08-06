package com.naccoro.wask;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

import com.naccoro.wask.customview.datepicker.DatePickerDialogFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DatePickerDialogFragment.newInstance().
                setOnDateChangedListener(new DatePickerDialogFragment.OnDateChangedListener() {
                    @Override
                    public void onDateChange(int year, int month, int day) {
                        Toast.makeText(MainActivity.this, year + "," + month + ", " + day, Toast.LENGTH_LONG).show();
                    }
                }).show(getSupportFragmentManager(), "dialog");
    }
}