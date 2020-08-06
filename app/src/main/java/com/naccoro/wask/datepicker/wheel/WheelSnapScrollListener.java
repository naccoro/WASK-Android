package com.naccoro.wask.datepicker.wheel;

import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import com.google.android.material.appbar.AppBarLayout;

public class WheelSnapScrollListener extends RecyclerView.OnScrollListener {

    SnapHelper helper;
    Behavior behavior;
    OnSnapPositionChangeListener snapPositionChangeListener;

    private int snapPosition = RecyclerView.NO_POSITION;

    public WheelSnapScrollListener(SnapHelper helper) {
        this(helper, Behavior.NOTIFY_ON_SCROLL, null);
    }

    public WheelSnapScrollListener(SnapHelper helper, Behavior behavior) {
        this(helper, behavior, null);
    }

    public WheelSnapScrollListener(SnapHelper helper, Behavior behavior, OnSnapPositionChangeListener listener) {
        this.helper = helper;
        this.behavior = behavior;
        this.snapPositionChangeListener = listener;
    }

    @Override
    public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
        super.onScrollStateChanged(recyclerView, newState);

        if (behavior == Behavior.NOTIFY_ON_SCROLL_STATE_IDLE
                && newState == RecyclerView.SCROLL_STATE_IDLE) {
            maybeNotifySnapPositionChange(recyclerView);
        }
    }

    @Override
    public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
        if (behavior == Behavior.NOTIFY_ON_SCROLL) {
            maybeNotifySnapPositionChange(recyclerView);
        }
    }

    public int getSnapPosition(RecyclerView recyclerView) {
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if (layoutManager == null) return RecyclerView.NO_POSITION;

        View snapView = helper.findSnapView(layoutManager);
        if (snapView == null) return RecyclerView.NO_POSITION;

        return layoutManager.getPosition(snapView);
    }

    /**
     * 스크롤로 변경된 RecyclerView의 Snap Position을 구한 뒤 변경되면 Notify
     *
     * @param recyclerView : 스크롤된 RecyclerView
     */
    private void maybeNotifySnapPositionChange(RecyclerView recyclerView) {

        int snapPosition = getSnapPosition(recyclerView);
        boolean isSnapPositionChanged = this.snapPosition != snapPosition;
        if (isSnapPositionChanged && snapPositionChangeListener != null) {
            snapPositionChangeListener.onSnapPositionChange(recyclerView, snapPosition);
            this.snapPosition = snapPosition;
        }
    }


    enum Behavior {
        NOTIFY_ON_SCROLL,
        NOTIFY_ON_SCROLL_STATE_IDLE
    }

    interface OnSnapPositionChangeListener {
        public void onSnapPositionChange(RecyclerView recyclerView, int position);
    }
}
