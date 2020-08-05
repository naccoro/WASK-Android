package com.naccoro.wask.datepicker;

import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.naccoro.wask.R;
import com.naccoro.wask.datepicker.wheel.WheelView;

import java.util.Arrays;

/**
 * <p>A fragment that shows a list of items as a modal bottom sheet.</p>
 * <p>You can show this modal bottom sheet from your activity like this:</p>
 * <pre>
 *     DatePickerDialogFragment.newInstance().show(getSupportFragmentManager(), "dialog");
 * </pre>
 */
public class DatePickerDialogFragment extends BottomSheetDialogFragment {

    // TODO: Customize parameter argument names
    private static final String[] PLANETS = new String[]{"Mercury", "Venus", "Earth", "Mars", "Jupiter", "Uranus", "Neptune", "Pluto"};
    // TODO: Customize parameters
    public static DatePickerDialogFragment newInstance() {
        return new DatePickerDialogFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_date_picker_dialog_list_dialog, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        WheelView wva = (WheelView) view.findViewById(R.id.wheel_view);
        wva.setOffset(1);
        wva.setItems(Arrays.asList(PLANETS));
        wva.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
            @Override
            public void onSelected(int selectedIndex, String item) {
                Log.d("datePicker", "selectedIndex: " + selectedIndex + ", item: " + item);
            }
        });
    }
}