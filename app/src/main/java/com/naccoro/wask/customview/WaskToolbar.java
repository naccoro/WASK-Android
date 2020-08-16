package com.naccoro.wask.customview;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.naccoro.wask.R;

public class WaskToolbar extends ConstraintLayout {

    private static final int layoutRes = R.layout.layout_wask_toolbar;

    private Context context;

    private ImageButton leftButton;

    private ImageButton rightButton;

    private ImageView logo;

    private TextView leftSideTitle;

    public WaskToolbar(Context context) {
        super(context);
        this.context = context;
        init();
    }

    public WaskToolbar(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    public WaskToolbar(Context context, AttributeSet attr, int defStyleAttr) {
        super(context, attr, defStyleAttr);
        this.context = context;
        init();
    }

    private void init() {
        View view = LayoutInflater.from(context).inflate(layoutRes, this, true);

        leftButton = view.findViewById(R.id.imagebutton_wasktoolbar_left);
        rightButton = view.findViewById(R.id.imagebutton_wasktoolbar_right);
        leftSideTitle = view.findViewById(R.id.textview_wasktoolbar_title);
        logo = view.findViewById(R.id.imageview_wasktoolbar_logo);
        leftButton.setVisibility(View.INVISIBLE);
        rightButton.setVisibility(View.INVISIBLE);
    }

    public void setLeftButton(int src, OnClickListener listener) {
        leftButton.setVisibility(View.VISIBLE);
        leftButton.setImageResource(src);
        leftButton.setOnClickListener(view -> listener.onClick());
    }

    public void setRightButton(int src, OnClickListener listener) {
        rightButton.setVisibility(View.VISIBLE);
        rightButton.setImageResource(src);
        rightButton.setOnClickListener(view -> listener.onClick());
    }

    public void setBackButton() {
        setLeftButton(R.drawable.ic_appbarback, () -> ((Activity) context).finish());
    }

    public void setBackButton(OnClickListener listener) {
        setLeftButton(R.drawable.ic_appbarback, listener);
    }

    public void setLeftSideTitle(String title) {
        leftSideTitle.setVisibility(View.VISIBLE);
        leftSideTitle.setText(title);
        logo.setVisibility(View.INVISIBLE);
    }

    public interface OnClickListener {
        void onClick();
    }
}