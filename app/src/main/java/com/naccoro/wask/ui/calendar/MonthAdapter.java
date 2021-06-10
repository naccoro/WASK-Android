package com.naccoro.wask.ui.calendar;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class MonthAdapter extends FragmentStateAdapter {
    Date selectDate;

    public static final int START_POSITION = Integer.MAX_VALUE/2;

    public MonthAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    public void setDate(Date date) {
        this.selectDate = date;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        Fragment fragment = new MonthFragment();
        Bundle args = new Bundle();
        args.putSerializable("date", selectDate);
        args.putInt("position", position);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getItemCount() {
        return Integer.MAX_VALUE;
    }
}
