package com.naccoro.wask.customview.waskdialog;

public class WaskDialogButton {
    private String text;
    private WaskDialog.OnClickListener listener;

    public WaskDialogButton(String text, WaskDialog.OnClickListener listener) {
        this.text = text;
        this.listener = listener;
    }

    public String getText() {
        return text;
    }

    public WaskDialog.OnClickListener getListener() {
        return listener;
    }
}
