package com.account.king.presenter.contract.presenter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.account.king.KingApplication;
import com.account.king.R;
import com.account.king.SetLockActivity;
import com.account.king.node.AlarmNode;
import com.account.king.presenter.contract.SetAccountContract;
import com.account.king.reciver.MAlarmReceiver;
import com.account.king.util.AlarmUtil;
import com.account.king.util.SPUtils;

import cn.qqtheme.framework.picker.OptionPicker;
import cn.qqtheme.framework.picker.TimePicker;
import cn.qqtheme.framework.picker.WheelPicker;

/**
 * @author king
 */
public class SetAccountPresenter implements SetAccountContract.ISetAccountPresenter {


    private SetAccountContract.ISetAccountView setAccountView;

    public SetAccountPresenter(SetAccountContract.ISetAccountView setAccountView) {
        this.setAccountView = setAccountView;
    }

    @Override
    public void switchRemind(boolean isChecked) {
        if (isChecked) {
            setAccountView.startRemind();
        } else {
            setAccountView.stopRemind();
        }
    }

    @Override
    public void showDayPicker(Activity context, int index) {
        String[] days = new String[31];
        for (int i = 0; i <= 30; i++) {
            days[i] = (i + 1) < 10 ? "0" + (i + 1) + "日" : (i + 1) + "日";
        }
        OptionPicker picker = new OptionPicker(context, days);
        //picker.setAnimationStyle();
        setDefaultPicker(context, picker);
        picker.setSelectedIndex(index);
        picker.setOnOptionPickListener(new OptionPicker.OnOptionPickListener() {
            @Override
            public void onOptionPicked(String option) {
                int day = Integer.parseInt(option.substring(0, 2));
                setAccountView.setDayPicker(day, option);
            }
        });
        picker.show();
    }

    @Override
    public void showTimePicker(Activity activity, int hour, int minute) {
        //默认选中当前时间
        TimePicker picker = new TimePicker(activity, TimePicker.HOUR_OF_DAY);
        setDefaultPicker(activity, picker);
        picker.setSelectedItem(hour, minute);
        picker.setOnTimePickListener(new TimePicker.OnTimePickListener() {
            @Override
            public void onTimePicked(String hour, String minute) {
                if ("0".equals(hour)) {
                    hour = "00";
                }
                if ("0".equals(minute)) {
                    minute = "00";
                }
                setAccountView.setTimePicker(hour, minute);
            }
        });
        picker.show();
    }

    @Override
    public void showWeekPicker(Activity activity, int index) {
        final String[] weeks = activity.getResources().getStringArray(R.array.repeat_time);
        OptionPicker picker = new OptionPicker(activity, weeks);
        setDefaultPicker(activity, picker);
        picker.setSelectedIndex(index);
        picker.setOnOptionPickListener(new OptionPicker.OnOptionPickListener() {
            @Override
            public void onOptionPicked(String option) {
                int day = 0;
                for (int i = 0; i < weeks.length; i++) {
                    if (weeks[i].equals(option)) {
                        day = i;
                        break;
                    }
                }
                setAccountView.setRepeatPicker(day, option);
            }
        });
        picker.show();
    }

    public void setDefaultPicker(Activity context, WheelPicker picker) {
        picker.setLineColor(context.getResources().getColor(R.color.picker_line));
        picker.setOffset(2);
        picker.setTextSize(20);
        picker.setCancelVisible(false);
        picker.setTopLineVisible(false);
        picker.setSubmitTextColor(context.getResources().getColor(R.color.color5));
    }

    @Override
    public void openAlarm(Context context, AlarmNode alarmNode) {
        AlarmUtil.registAlarm(context, MAlarmReceiver.REMIND_RECEIVER, alarmNode);
    }

    @Override
    public void closeAlarm(Context context) {
        AlarmUtil.closeAlarm(context, MAlarmReceiver.REMIND_RECEIVER);
    }

    @Override
    public void switchLock(Activity context, boolean isChecked) {
        //关闭new图标
        SPUtils.put(context, "lock_new", true);
        if (isChecked) {
            String lock = SPUtils.getString(context, SPUtils.LOCK_);
            if (TextUtils.isEmpty(lock)) {
                context.startActivity(new Intent(context, SetLockActivity.class));
            } else {
                SPUtils.put(context, SPUtils.LOCK_OPEN_, true);
                setAccountView.openLockEdit();
            }
        } else {
            SPUtils.remove(context, SPUtils.LOCK_OPEN_);
            setAccountView.closeLockEdit();
        }
        KingApplication.restoreLock();
    }
}
