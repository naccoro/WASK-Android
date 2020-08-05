package com.naccoro.wask.customview.waskdialog;

import androidx.annotation.LayoutRes;

import java.util.ArrayList;
import java.util.List;

public class WaskDialogBuilder {

    private String title;
    private String message;
    private int contentRes = -1;
    private List<WaskDialogButton> verticalButtons;
    private List<WaskDialogButton> horizontalButtons;

    public WaskDialog build() {
        return new WaskDialog(title, message, contentRes, verticalButtons, horizontalButtons);
    }

    public WaskDialogBuilder setTitle(String title) {
        this.title = title;
        return this;
    }

    public WaskDialogBuilder setMessage(String message) {
        this.message = message;
        return this;
    }

    public WaskDialogBuilder setContent(@LayoutRes int contentRes) {
        this.contentRes = contentRes;
        return this;
    }

    public WaskDialogBuilder addVerticalButton(String buttonText, WaskDialog.OnClickListener listener) {
        if (verticalButtons == null) {
            verticalButtons = new ArrayList<>();
        }
        verticalButtons.add(new WaskDialogButton(buttonText, listener));
        return this;
    }

    public WaskDialogBuilder addVerticalButton(WaskDialogButton button) {
        verticalButtons.add(button);
        return this;
    }

    public WaskDialogBuilder addHorizontalButton(String buttonText, WaskDialog.OnClickListener listener) {
        if (horizontalButtons == null) {
            horizontalButtons = new ArrayList<>();
        }
        horizontalButtons.add(new WaskDialogButton(buttonText, listener));
        return this;
    }

    public WaskDialogBuilder addHorizontalButton(WaskDialogButton button) {
        horizontalButtons.add(button);
        return this;
    }
}
