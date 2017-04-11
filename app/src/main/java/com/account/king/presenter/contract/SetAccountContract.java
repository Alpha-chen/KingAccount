package com.account.king.presenter.contract;

import android.app.Activity;
import android.content.Context;

import com.account.king.node.AlarmNode;

/**
 * 账单设置
 *
 * @author King
 */
public class SetAccountContract {


    public interface ISetAccountView {

        public void startRemind();

        public void stopRemind();

        public void setDayPicker(int position, String day);

        public void setTimePicker(String hour, String minute);

        public void setRepeatPicker(int position, String day);

        public void closeLockEdit();

        public void openLockEdit();
    }

    public interface ISetAccountPresenter {

        public void switchRemind(boolean isChecked);

        public void showDayPicker(Activity context, int index);

        public void showTimePicker(Activity activity, int hour, int minute);

        public void showWeekPicker(Activity activity, int index);

        public void openAlarm(Context context, AlarmNode alarmNode);

        public void closeAlarm(Context context);

        public void switchLock(Activity context, boolean isChecked);
    }
}
