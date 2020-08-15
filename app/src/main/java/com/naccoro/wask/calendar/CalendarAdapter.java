package com.naccoro.wask.calendar;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.naccoro.wask.WaskApplication;
import com.naccoro.wask.preferences.SettingPreferenceManager;
import com.naccoro.wask.replacement.repository.ReplacementHistoryRepository;
import com.naccoro.wask.utils.AlarmUtil;
import com.naccoro.wask.utils.DateUtils;
import com.naccoro.wask.calendar.CalendarActivity.Date;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import static com.naccoro.wask.R.*;

public class CalendarAdapter extends RecyclerView.Adapter<CalendarAdapter.CalendarViewHolder> {

    private static final String TAG = "CalendarAdapter";
    private Context context;
    private ArrayList<CalendarItem> calendarList;
    private boolean isModifyMode;
    private int selectPosition;

    private static Date today;
    private ReplacementHistoryRepository replacementHistoryRepository;

    public CalendarAdapter(Context context, ArrayList<CalendarItem> calendarList,
                           ReplacementHistoryRepository replacementHistoryRepository, Date selectDate) {
        this.context = context;
        this.calendarList = calendarList;
        this.replacementHistoryRepository = replacementHistoryRepository;
        this.setToday(selectDate);
    }

    public void setToday(Date date) {
        today = new Date(date.getYear(), date.getMonth(), date.getDay());
    }

    public void setCalendarList(ArrayList<CalendarItem> calendarList) {
        this.calendarList = calendarList;
        notifyDataSetChanged();
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
    public CalendarAdapter.CalendarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(layout.calendar_item, parent, false);
        CalendarAdapter.CalendarViewHolder viewHolder = new CalendarAdapter.CalendarViewHolder(view);

        return viewHolder;
    }

    /**
     * position에 해당하는 data를 view holder에 넣기
     *
     * @param calendarViewHolder
     * @param position
     */
    @Override
    public void onBindViewHolder(@NonNull CalendarViewHolder calendarViewHolder, int position) {

        CalendarItem item = calendarList.get(position);
        calendarViewHolder.dateTextView.setText(item.getDate().get(Calendar.DAY_OF_MONTH) + "");

        // 수정모드 설정
        if (isModifyMode) {
            calendarViewHolder.itemView.setOnClickListener(view -> onDayClick(calendarViewHolder, item, position));
        }

        decorateItem(calendarViewHolder, item, position);
    }

    /**
     * 아이템(각 날짜) 꾸미기
     *
     * @param calendarViewHolder
     * @param item
     * @param position
     */
    private void decorateItem(CalendarViewHolder calendarViewHolder, CalendarItem item, int position) {

        View itemView = calendarViewHolder.getItemView();

        // 일요일은 빨간날
        if (position % 7 == 0) {
            calendarViewHolder.dateTextView.setTextColor(itemView.getContext().getColor(color.waskRed));
        } else {
            calendarViewHolder.dateTextView.setTextColor(itemView.getContext().getColor(color.black));
        }

        // 오늘이면 동그라미 표시
        if (item.isSelect()) {
            selectPosition = position; // 어댑터속에 저장! (나중에 지우기 위해)
            calendarViewHolder.dateTextView.setTextColor(itemView.getContext().getColor(color.white));
            calendarViewHolder.dateBackgroundImageView.setVisibility(View.VISIBLE);
        } else {
            calendarViewHolder.dateTextView.setTextColor(itemView.getContext().getColor(color.black));
            calendarViewHolder.dateBackgroundImageView.setVisibility(View.GONE);
        }

        //지난 달과 다음 달의 날짜는 흐리게
        if (!item.isCurrentMonth()) {
            calendarViewHolder.dateTextView.setAlpha(0.4f);
        } else {
            calendarViewHolder.dateTextView.setAlpha(1.0f);
        }

        // 마스크 교체한 날 표시
        if (item.isChangeMask()) {
            calendarViewHolder.changeImageView.setVisibility(View.VISIBLE);
        } else {
            calendarViewHolder.changeImageView.setVisibility(View.INVISIBLE);
        }
    }

    /**
     * 날짜를 클릭했을 때 마스크교체여부가 바뀐다. (DB에도 바로 반영)
     *
     * @param calendarViewHolder
     * @param item
     * @param position           select 로 저장하기 위함
     */
    private void onDayClick(CalendarViewHolder calendarViewHolder, CalendarItem item, int position) {

        GregorianCalendar clickItem = item.getDate();

        calendarList.get(selectPosition).setSelect(false); // 이전 선택 해제
        notifyItemChanged(selectPosition);


        item.setSelect(true);
        selectPosition = position; // select로 지정

        // 미래는 마스크 교체여부 변경 제한
        if (clickItem.get(Calendar.YEAR) > today.getYear()) {
            return;
        } else if (clickItem.get(Calendar.MONTH) > today.getMonth()) {
            return;
        } else if ((clickItem.get(Calendar.MONTH) == today.getMonth()) && (clickItem.get(Calendar.DATE) > today.getDay())) {
            return;
        }

        // 마스크 교체여부 변경
        if (item.isChangeMask()) {
            item.setChangeMask(false);
            replacementHistoryRepository.delete(DateUtils.getDateFromGregorianCalendar(item.getDate()));
            calendarViewHolder.changeImageView.setVisibility(View.GONE);
            updateMaskAlarm(context);
        } else {
            item.setChangeMask(true);
            replacementHistoryRepository.insert(DateUtils.getDateFromGregorianCalendar(item.getDate()), new ReplacementHistoryRepository.InsertHistoryCallback() {
                @Override
                public void onSuccess() {
                    calendarViewHolder.changeImageView.setVisibility(View.VISIBLE);
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
                AlarmUtil.showForegroundService(context, period);

                AlarmUtil.setForegroundAlarm(context);
            } else {
                AlarmUtil.dismissForegroundService(context);

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
        if (calendarList != null) {
            return calendarList.size();
        }
        return 0;
    }

    /**
     * calendar view holder
     */
    public class CalendarViewHolder extends RecyclerView.ViewHolder {
        private TextView dateTextView;
        private ImageView dateBackgroundImageView;
        private ImageView changeImageView;
        private View itemView;

        public CalendarViewHolder(@NonNull View itemView) {
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
