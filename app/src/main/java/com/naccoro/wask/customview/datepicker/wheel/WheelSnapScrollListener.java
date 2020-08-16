package com.naccoro.wask.customview.datepicker.wheel;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

/**
 * 사용자가 스크롤 할 시 item의 위치가 중앙에서 벗어나면 중앙으로 당겨오는 리스너
 * <p>
 * copy by link : https://medium.com/over-engineering/detecting-snap-changes-with-androids-recyclerview-snaphelper-9e9f5e95c424
 *
 * @author jaeryo
 * @since 2020-08-06
 */
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

    /**
     * Snap Value의 값이 변경될 때 Notify를 언제 해줄지 결정하는 enum
     * <p>
     * NOTIFY_ON_SCROLL : 스크롤 하는 도중 중앙 값에 Item이 오면 Notify
     * NOTIFY_ON_SCROLL_STATE_IDLE : 스크롤이 멈추고 가운데에 온 Item을 Notify
     */
    enum Behavior {
        NOTIFY_ON_SCROLL,
        NOTIFY_ON_SCROLL_STATE_IDLE
    }

    interface OnSnapPositionChangeListener {
        public void onSnapPositionChange(RecyclerView recyclerView, int position);
    }
}
