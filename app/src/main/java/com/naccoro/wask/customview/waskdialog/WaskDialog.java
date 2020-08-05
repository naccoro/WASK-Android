package com.naccoro.wask.customview.waskdialog;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.naccoro.wask.R;
import com.naccoro.wask.utils.MetricsUtil;

import java.util.List;

public class WaskDialog extends DialogFragment {

    private static final String TAG = "WaskDialog";

    private String title;
    private String message;
    private int contentRes = -1;
    private List<WaskDialogButton> verticalButtons;
    private List<WaskDialogButton> horizontalButtons;

    private TextView textViewDialogTitle;
    private TextView textViewDialogMessage;
    private LinearLayout linearLayoutDialogContent;
    private LinearLayout linearLayoutDialogButtons;

    private View contentView;

    public WaskDialog(String title, String message, @LayoutRes int contentRes, List<WaskDialogButton> verticalButtons,
                      List<WaskDialogButton> horizontalButtons) {
        super();
        this.title = title;
        this.message = message;
        this.contentRes = contentRes;
        this.verticalButtons = verticalButtons;
        this.horizontalButtons = horizontalButtons;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_wask, container);

        contentCheck();

        initView(view, inflater, container);

        if (getDialog() != null) {
            Window window = getDialog().getWindow();
            if (window != null) {
                window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                window.requestFeature(Window.FEATURE_NO_TITLE);
            } else {
                Log.e(TAG, "Window is null.");
            }
        } else {
            Log.e(TAG, "Dialog is null.");
        }
        setCancelable(false);

        return view;
    }

    private void initView(View view, LayoutInflater inflater, ViewGroup container) {
        textViewDialogTitle = view.findViewById(R.id.textview_dialog_title);
        textViewDialogMessage = view.findViewById(R.id.textview_dialog_message);
        linearLayoutDialogContent = view.findViewById(R.id.linearlayout_dialog_content);
        linearLayoutDialogButtons = view.findViewById(R.id.linearlayout_dialog_buttons);

        initTextView(textViewDialogTitle, title);
        initTextView(textViewDialogMessage, message);

        if (contentRes != -1) {
            contentView = inflater.inflate(contentRes, container);
            linearLayoutDialogContent.addView(contentView);
        } else {
            linearLayoutDialogContent.setPadding(0, (int) MetricsUtil.convertDpToPixel(19f, getContext()), 0, 0);
        }

        if (verticalButtons != null) {
            initButtons(verticalButtons, inflater, linearLayoutDialogButtons, LinearLayout.VERTICAL);
        }

        if (horizontalButtons != null) {
            LinearLayout verticalButtonGroup = new LinearLayout(getContext());
            verticalButtonGroup.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

            initButtons(horizontalButtons, inflater, verticalButtonGroup, LinearLayout.HORIZONTAL);
            linearLayoutDialogButtons.addView(verticalButtonGroup);
        }
    }

    private void initButtons(List<WaskDialogButton> buttons, LayoutInflater inflater, ViewGroup container, int orientation) {
        for (WaskDialogButton waskDialogButton : buttons) {

            View button = inflater.inflate(R.layout.item_dialog_button, container, false);

            if (orientation == LinearLayout.HORIZONTAL) {
                button.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1f));
                if (waskDialogButton != buttons.get(0)) {
                    setButtonDivider(container);
                }
            }

            ((TextView) button.findViewById(R.id.textview_dialogbuttonitem)).setText(waskDialogButton.getText());

            button.setOnClickListener((View v) -> waskDialogButton.getListener().onClick(this, contentView));

            container.addView(button);
        }
    }

    private void setButtonDivider(ViewGroup container) {
        View divider = new View(getContext());

        divider.setLayoutParams(new LinearLayout.LayoutParams((int) MetricsUtil.convertDpToPixel(1f, getContext()), LinearLayout.LayoutParams.MATCH_PARENT));

        divider.setBackgroundResource(R.color.dividerGray);

        container.addView(divider);
    }

    private void initTextView(TextView textView, String text) {
        if (text != null) {
            textView.setText(text);
        } else {
            textView.setVisibility(View.GONE);
        }
    }

    private void contentCheck() {
        if (message != null && contentRes != -1) {
            throw new IllegalArgumentException("Message cannot be used with ContentRes. Please choose one of the two.");
        }
    }
}
