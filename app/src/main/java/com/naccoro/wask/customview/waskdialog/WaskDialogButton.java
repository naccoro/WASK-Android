package com.naccoro.wask.customview.waskdialog;

import android.view.View;

import com.naccoro.wask.customview.waskdialog.WaskDialog;

public class WaskDialogButton {
    private String text;
    private OnClickListener listener;

    public WaskDialogButton(String text, OnClickListener listener) {
        this.text = text;
        this.listener = listener;
    }

    public String getText() {
        return text;
    }

    public OnClickListener getListener() {
        return listener;
    }

    public interface OnClickListener {
        void onClick(WaskDialog dialog, View view);
    }
}
