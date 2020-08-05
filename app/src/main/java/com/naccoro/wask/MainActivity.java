package com.naccoro.wask;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.naccoro.wask.datepicker.DatePickerDialogFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DatePickerDialogFragment.newInstance().show(getSupportFragmentManager(), "dialog");
    }
}