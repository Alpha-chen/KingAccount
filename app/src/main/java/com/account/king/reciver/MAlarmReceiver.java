package com.account.king.reciver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.account.king.util.AlarmUtil;
import com.account.king.util.LogUtil;


/**
 * @author king
 */
public class MAlarmReceiver extends BroadcastReceiver {


    //记账提醒
    public static final String REMIND_RECEIVER = "com.account.king.remind";

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        LogUtil.d("MAlarmReceiver action=" + action);
        if (REMIND_RECEIVER.equals(action)) {
            AlarmUtil.showRemind(context);
        }
    }
}
