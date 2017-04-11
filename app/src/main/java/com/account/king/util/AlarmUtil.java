package com.account.king.util;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.account.king.PreNotificationActivity;
import com.account.king.R;
import com.account.king.node.AlarmNode;
import com.account.king.reciver.MAlarmReceiver;

import java.util.Calendar;
import java.util.TimeZone;

/**
 * @author king
 */
public class AlarmUtil {


    /**
     * 注册记账闹钟
     *
     * @param context
     * @param action
     * @param alarmNode
     */
    public static void registAlarm(Context context, String action, AlarmNode alarmNode) {
        //获取第一次提醒时间
        Calendar calendar = getCalendar(alarmNode);
        //闹钟id
        int remindId = getRemindId(context);
        //闹钟重复时间间隔  day
        int interval = 0;
        if (alarmNode.getRepeat_mode() == 0) {
            interval = 1;
        } else {
            interval = 7;
        }
        openAlarm(context, action, calendar, remindId, interval);
    }


    /**
     * 关闭闹钟
     *
     * @param context
     * @param action
     */
    public static void closeAlarm(Context context, String action) {
        //闹钟id
        int remindId = getRemindId(context);
        Intent intent = new Intent(context, MAlarmReceiver.class);
        intent.setAction(action);
        PendingIntent sender = PendingIntent.getBroadcast(context, remindId, intent, 0);
        AlarmManager alarm = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarm.cancel(sender);
    }

    /**
     * 得到记账提醒的calendar
     *
     * @param alarmNode
     * @return
     */
    public static Calendar getCalendar(AlarmNode alarmNode) {
        Calendar calendar = Calendar.getInstance();
        //设置时区
        calendar.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        calendar.set(Calendar.HOUR_OF_DAY, alarmNode.getHour());
        calendar.set(Calendar.MINUTE, alarmNode.getMinute());
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        //如果用户设置的最近一次提醒时间小于当前的时间，则从下一次的时间开始提醒用户
        Calendar currCalendar = Calendar.getInstance();
        if (alarmNode.getRepeat_mode() == 0) {    //每天提醒
            //当天提醒时间过了 到下一天
            if (calendar.getTimeInMillis() < currCalendar.getTimeInMillis()) {
                calendar.add(Calendar.DAY_OF_MONTH, 1);
            }
        } else {
            if (alarmNode.getRepeat_mode() == 7) {//周日
                calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
            } else {
                calendar.set(Calendar.DAY_OF_WEEK, alarmNode.getRepeat_mode() + 1);
            }
            //当周提醒时间过了 到下一周
            if (calendar.getTimeInMillis() < currCalendar.getTimeInMillis()) {
                calendar.add(Calendar.WEEK_OF_MONTH, 1);
            }
        }
        LogUtil.d("nnn", "记账提醒时间=" + CalendarUtil.getDate(calendar));
        return calendar;
    }

    /**
     * @param context
     * @param action
     * @param calendar
     * @param alarmId  闹钟的ID
     * @param interval 重复闹钟的时间间隔
     */
    public static void openAlarm(Context context, String action, Calendar calendar,
                                 int alarmId, int interval) {
        Intent intent = new Intent(context, MAlarmReceiver.class);
        intent.setAction(action);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context.getApplicationContext(), alarmId, intent, 0);
        AlarmManager alarmManager = (AlarmManager) context
                .getSystemService(Activity.ALARM_SERVICE);
        long timeInMillis = calendar.getTimeInMillis();
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, timeInMillis,
                interval * 24 * 60 * 60 * 1000, pendingIntent);
    }

    /**
     * 得到记账闹钟id
     *
     * @param context
     * @return
     */
    public static int getRemindId(Context context) {
        int remindId = SPUtils.getInt(context, SPUtils.REMIND_ID);
        if (0 == remindId) {
            remindId = (int) (System.currentTimeMillis() / 1000000);
            SPUtils.put(context, SPUtils.REMIND_ID, remindId);
        }
        return remindId;
    }


    /**
     * 显示记账提醒
     *
     * @param context
     */
    public static void showRemind(Context context) {
        String notifyContent = context.getResources().getString(R.string.remind_account);
        NotificationUtil.addNotification(context, PreNotificationActivity.class, notifyContent, NotificationUtil.IS_FROM_ALARM);
    }
}
