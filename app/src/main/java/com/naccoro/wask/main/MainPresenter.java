package com.naccoro.wask.main;

import android.content.Context;

import com.naccoro.wask.preferences.NotificationPreferenceManager;
import com.naccoro.wask.preferences.SettingPreferenceManager;
import com.naccoro.wask.utils.AlarmUtil;
import com.naccoro.wask.utils.DateUtils;

import java.util.Calendar;

public class MainPresenter implements MainContract.Presenter {
    MainContract.View mainView;

    MainPresenter(MainContract.View mainView) {
        this.mainView = mainView;
    }

    /**
     * MainView에서 보여줘야 할 Data를 가져오는 함수
     */
    @Override
    public void start() {
        int period = getMaskPeriod();
        mainView.setPeriodTextValue(period);

        if (period > 1) {
            mainView.showBadMainView();
        } else {
            mainView.showGoodMainView();
        }
    }

    /**
     * WaskDatabase에서 현재 마스크 교체 상태를 가져오는 함수
     *
     * @return [오늘 날짜 - 마지막 교체 일자]
     */
    private int getMaskPeriod() {
        //TODO: WaskDatabase에서 교체일자를 비교하여 현재 상태를 가져온다.
        return 3;
    }

    @Override
    public void clickSettingButton() {
        mainView.showSettingView();
    }

    @Override
    public void clickCalendarButton() {
        mainView.showCalendarView();
    }

    /**
     * 사용자가 교체하기 버튼을 누를 경우 호출되는 함수
     */
    @Override
    public void changeMask(Context context) {
        //TODO: WaskDatabase에서 교체일자로 오늘 날짜를 넣는다.
        start();
        setMaskReplaceNotification(context);
    }

    /**
     * 사용자가 교체하기를 누르면 등록되어 있는 알람을 종료하고 새로운 알람을 등록한다.
     */
    private void setMaskReplaceNotification(Context context) {
        //남아있던 alarm 을 종료한다.
        AlarmUtil.cancelReplacementCycleAlarm(context);
        AlarmUtil.cancelReplaceLaterAlarm(context);

        //혹여나 핸드폰이 종료되어 BootReceiver 가 작동되어도 ReplaceLater Alarm 을 작동되지 않게 하기 위해 null 을 넣는다.
        NotificationPreferenceManager.setReplaceLaterDate(null);

        String todayDate = DateUtils.getStringOfCalendar(Calendar.getInstance());

        //교체하기 Date 를 등록한다. BootReceiver 가 작동되어도 등록한 날짜 기준으로 period 후에 alarm 이 동작하게 만든다.
        NotificationPreferenceManager.setReplacementCycleDate(todayDate);

        AlarmUtil.setReplacementCycleAlarm(context);
    }
}
