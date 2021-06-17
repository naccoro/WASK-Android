package com.naccoro.wask.ui.calendar;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.naccoro.wask.WaskApplication;
import com.naccoro.wask.notification.ServiceUtil;
import com.naccoro.wask.preferences.SettingPreferenceManager;
import com.naccoro.wask.replacement.repository.ReplacementHistoryRepository;
import com.naccoro.wask.utils.AlarmUtil;
import com.naccoro.wask.utils.DateUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import static com.naccoro.wask.R.*;

public class DayAdapter extends RecyclerView.Adapter<DayAdapter.DayViewHolder> {

    private static final String TAG = "CalendarAdapter";
    private Context context;
    private ArrayList<DayItem> dayItems;
    private int height;
    private boolean isModifyMode;

    private ReplacementHistoryRepository replacementHistoryRepository;

    /**
     * @param context
     * @param dayItems 달력에 표시할 일자들을 담은 list
     * @param replacementHistoryRepository 교체 기록
     * @param selectDate 달력에서 선택된 날짜
     * @param height 각 아이템이(칸)의 높이.
     */
    public DayAdapter(Context context, ArrayList<DayItem> dayItems,
                      ReplacementHistoryRepository replacementHistoryRepository, Date selectDate, int height) {
        this.context = context;
        this.dayItems = dayItems;
        this.height = height;
        this.replacementHistoryRepository = replacementHistoryRepository;
    }

    public void setModifyMode(boolean isModifyMode) {
        this.isModifyMode = isModifyMode;
        notifyDataSetChanged();
    }

    /**
     * 아이템 뷰를 넣기 위한 view holder 생성
     *
     * @param parent
     * @param viewType
     * @return
     */
    @NonNull
    @Override
    public DayViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(layout.date_item, parent, false);
        view.getLayoutParams().height = this.height;

        DayViewHolder viewHolder = new DayViewHolder(view);
        return viewHolder;
    }

    /**
     * position에 해당하는 data를 view holder에 넣기
     *
     * @param dayViewHolder
     * @param position
     */
    @Override
    public void onBindViewHolder(@NonNull DayViewHolder dayViewHolder, int position) {

        DayItem item = dayItems.get(position);
        dayViewHolder.dateTextView.setText(item.getDate().get(Calendar.DAY_OF_MONTH) + "");

        // 수정모드 설정
        if (isModifyMode) {
            dayViewHolder.itemView.setOnClickListener(view -> onDayClick(dayViewHolder, item, position));
        }

        decorateItem(dayViewHolder, item, position);
    }

    /**
     * 아이템(각 날짜) 꾸미기
     *
     * @param dayViewHolder
     * @param item
     * @param position
     */
    private void decorateItem(DayViewHolder dayViewHolder, DayItem item, int position) {

        View itemView = dayViewHolder.getItemView();

        // 날짜(Day) 부분
        if (WaskApplication.today == DateUtils.getDateFromGregorianCalendar(item.getDate())) {
            // 오늘이면 동그라미 표시
            dayViewHolder.dateTextView.setTextColor(itemView.getContext().getColor(color.white));
            dayViewHolder.dateBackgroundImageView.setVisibility(View.VISIBLE);
        } else if (position % 7 == 0) {
            // 일요일은 빨간날
            dayViewHolder.dateTextView.setTextColor(itemView.getContext().getColor(color.waskRed));
            dayViewHolder.dateBackgroundImageView.setVisibility(View.GONE);
        } else {
            dayViewHolder.dateTextView.setTextColor(itemView.getContext().getColor(color.black));
            dayViewHolder.dateBackgroundImageView.setVisibility(View.GONE);
        }

        //지난 달과 다음 달의 날짜는 흐리게
        if (!item.isCurrentMonth()) {
            dayViewHolder.dateTextView.setAlpha(0.4f);
        } else {
            dayViewHolder.dateTextView.setAlpha(1.0f);
        }

        // 마스크 교체한 날 표시
        if (item.isChangeMask()) {
            dayViewHolder.changeImageView.setVisibility(View.VISIBLE);
        } else {
            dayViewHolder.changeImageView.setVisibility(View.INVISIBLE);
        }
    }

    /**
     * 날짜를 클릭했을 때 마스크교체여부가 바뀐다. (DB에도 바로 반영)
     *
     * @param dayViewHolder
     * @param item
     * @param position           select 로 저장하기 위함
     */
    private void onDayClick(DayViewHolder dayViewHolder, DayItem item, int position) {

        GregorianCalendar clickItem = item.getDate();

        // 미래는 마스크 교체여부 변경 제한
        if (clickItem.get(Calendar.YEAR) > DateUtils.getYear(WaskApplication.today)) {
            return;
        } else if (clickItem.get(Calendar.MONTH) + 1 > DateUtils.getMonth(WaskApplication.today)) {
            return;
        } else if ((clickItem.get(Calendar.MONTH) + 1 == DateUtils.getMonth(WaskApplication.today)) && (clickItem.get(Calendar.DATE) > DateUtils.getDay(WaskApplication.today))) {
            return;
        }

        // 마스크 교체여부 변경
        if (item.isChangeMask()) {
            item.setChangeMask(false);
            replacementHistoryRepository.delete(DateUtils.getDateFromGregorianCalendar(item.getDate()));
            dayViewHolder.changeImageView.setVisibility(View.GONE);
            updateMaskAlarm(context);
        } else {
            item.setChangeMask(true);
            replacementHistoryRepository.insert(DateUtils.getDateFromGregorianCalendar(item.getDate()), new ReplacementHistoryRepository.InsertHistoryCallback() {
                @Override
                public void onSuccess() {
                    dayViewHolder.changeImageView.setVisibility(View.VISIBLE);
                    updateMaskAlarm(context);
                }

                @Override
                public void onDuplicated() {
                    Log.d(TAG, "onDuplicated: true");
                }
            });
        }

        //변경된 날짜만 변경하면 더 효율적
        notifyItemChanged(position);
    }

    /**
     * 사용자가 수정모드로 변경할 때마다 알람도 변경해줍니다.
     */
    private void updateMaskAlarm(Context context) {
        AlarmUtil.cancelReplaceLaterAlarm(context);
        AlarmUtil.setReplacementCycleAlarm(context);

        if (SettingPreferenceManager.getIsShowNotificationBar()) {
            int period = getMaskPeriod();
            Log.d("MaksPeriod", period + "");

            WaskApplication.isChanged = period == 1;

            if (period > 0) {
                ServiceUtil.showForegroundService(context, period);

                AlarmUtil.setForegroundAlarm(context);
            } else {
                ServiceUtil.dismissForegroundService(context);

                AlarmUtil.cancelForegroundAlarm(context);
            }
        }
    }

    /**
     * WaskDatabase에서 현재 마스크 교체 상태를 가져오는 함수
     *
     * @return [오늘 날짜 - 마지막 교체 일자 + 1]
     */
    private int getMaskPeriod() {
        int lastReplacement = replacementHistoryRepository.getLastReplacement();

        if (lastReplacement == -1) {
            //교체 기록이 없을 경우
            return 0;
        }

        return DateUtils.calculateDateGapWithToday(lastReplacement) + 1;
    }

    /**
     * item 몇 개 생성되는지
     * 제대로 생성되지 않으면 오류가 난다고 합니다.
     *
     * @return item 개수 반환
     */
    @Override
    public int getItemCount() {
        if (dayItems != null) {
            return dayItems.size();
        }
        return 0;
    }

    /**
     * calendar view holder
     */
    public class DayViewHolder extends RecyclerView.ViewHolder {
        private TextView dateTextView;
        private ImageView dateBackgroundImageView;
        private ImageView changeImageView;
        private View itemView;

        public DayViewHolder(@NonNull View itemView) {
            super(itemView);
            this.itemView = itemView;

            initView(itemView);
        }

        public void initView(View v) {
            dateTextView = v.findViewById(id.textView_date);
            dateBackgroundImageView = v.findViewById(id.imageView_date_background);
            changeImageView = v.findViewById(id.imageView_change);
        }

        public View getItemView() {
            return itemView;
        }
    }
}
