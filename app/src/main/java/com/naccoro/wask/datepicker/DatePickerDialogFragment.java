package com.naccoro.wask.datepicker;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
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

import java.lang.reflect.Field;
import java.util.Arrays;

/**
 * <p>A fragment that shows a list of items as a modal bottom sheet.</p>
 * <p>You can show this modal bottom sheet from your activity like this:</p>
 * <pre>
 *     DatePickerDialogFragment.newInstance().show(getSupportFragmentManager(), "dialog");
 * </pre>
 */
public class DatePickerDialogFragment extends BottomSheetDialogFragment {

    BottomSheetBehavior behavior;

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
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        if (!(dialog instanceof BottomSheetDialog)) return dialog;

        BottomSheetDialog sheetDialog = (BottomSheetDialog)dialog;

        behavior = sheetDialog.getBehavior();
        behavior.setHideable(false);
        return sheetDialog;
    }

    public void dismiss() {
        this.dismiss();
    }
}