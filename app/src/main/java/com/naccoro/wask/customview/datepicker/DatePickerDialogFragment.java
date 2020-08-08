package com.naccoro.wask.customview.datepicker;

import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.naccoro.wask.R;
import com.naccoro.wask.customview.datepicker.wheel.WheelDatePicker;

/**
 * @author jaeryo
 * @since 2020.08.06
 * <p>
 * DatePicker 기능을 하는 Modal BottomSheet Dialog 입니다.
 * 사용하기 위해서는 아래와 같이 호출해주면 됩니다.
 * <pre>
 *     DatePickerDialogFragment.newInstance().show(getSupportFragmentManager(), "dialog");
 *
 *
 *     DatePickerDialogFragment.newInstance().setOnDateChangedListener(new DatePickerDialogFragment.OnDateChangedListener() {
 *             @Override
 *             public void onDateChange(int year, int month, int day) {
 *                 Toast.makeText(MainActivity.this, year + "," + month + ", " + day, Toast.LENGTH_LONG).show();
 *             }
 *         }).setDate(2022,5,12).show(getSupportFragmentManager(), "dialog");
 * </pre>
 */
public class DatePickerDialogFragment extends BottomSheetDialogFragment implements View.OnClickListener {

    private OnDateChangedListener dateChangedListener;
    WheelDatePicker datePicker;
    int year = -1;
    int month = -1;
    int day = -1;

    // TODO: Customize parameters
    public static DatePickerDialogFragment newInstance() {
        return new DatePickerDialogFragment();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        if (!(dialog instanceof BottomSheetDialog)) return dialog;

        BottomSheetDialog sheetDialog = (BottomSheetDialog) dialog;

        //BottomSheet 상태관리하는 객체인 BottomSheetBehavior
        //아래로 스크롤 시 화면 아래로 숨겨지지 않게 설정
        BottomSheetBehavior behavior = sheetDialog.getBehavior();
        behavior.setHideable(false);
        return sheetDialog;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_date_picker_dialog_list_dialog, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        //corner style 적용하기
        setStyle(DialogFragment.STYLE_NORMAL, R.style.ThemeOverlay_DatePicker_BottomSheetDialog);

        datePicker = view.findViewById(R.id.wheeldatepicker_datepicker);
        if (year != -1 && month != -1 && day != -1) {
            datePicker.setDate(year, month, day);
        }

        view.findViewById(R.id.imagebutton_datepicker_ok).setOnClickListener(this);

        view.findViewById(R.id.imagebutton_datepicker_x).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imagebutton_datepicker_ok:
                clickDatePickerOkButton();
                break;

            case R.id.imagebutton_datepicker_x:
                clickDatePickerXButton();
        }
    }


    public DatePickerDialogFragment setOnDateChangedListener(OnDateChangedListener dateChangedListener) {
        this.dateChangedListener = dateChangedListener;
        return this;
    }

    //사용자가 메인에서 보고있는 달력을 기준
    public DatePickerDialogFragment setDate(int year, int month, int day) {
        this.year = year;
        this.month = month;
        this.day = day;
        return this;
    }

    private void clickDatePickerXButton(){
        datePickerDismiss();
    }

    private void clickDatePickerOkButton() {
        if (dateChangedListener != null) {
            int year = datePicker.getYear();
            int month = datePicker.getMonth();
            int day = datePicker.getDay();
            dateChangedListener.onDateChange(year, month, day);
        }
        datePickerDismiss();
    }

    /**
     * Dialog 종료
     */
    private void datePickerDismiss() {
        this.dismiss();
    }

    public interface OnDateChangedListener {
        public void onDateChange(int year, int month, int day);
    }
}