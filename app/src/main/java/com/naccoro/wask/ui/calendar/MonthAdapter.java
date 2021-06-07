package com.naccoro.wask.ui.calendar;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class MonthAdapter extends FragmentStateAdapter {
    Date selectDate;

    public MonthAdapter(@NonNull FragmentActivity fragmentActivity, Date date) {
        super(fragmentActivity);
        setDate(date);
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
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getItemCount() {
        return 50;
    }
}
