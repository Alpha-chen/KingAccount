package com.account.king.util;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.account.king.R;


/**
 * 状态栏通知等
 */
public class NotificationUtil {


    public static final int IS_FROM_ALARM = 1;//来自记账提醒

    public static void addNotification(Context context, Class clazz, String content, int type) {
        LogUtil.d("nnn", "addNotification=" + clazz.getSimpleName());
        Intent toIntent = new Intent(context, clazz);
        toIntent.putExtra(ActivityLib.INTENT_PARAM, type);
        toIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent intent = PendingIntent.getActivity(context, 0, toIntent, 0);
        Notification.Builder builder = new Notification.Builder(context)
                .setAutoCancel(true)
                .setContentTitle(context.getString(R.string.app_name))
                .setContentText(content)
                .setContentIntent(intent)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setWhen(System.currentTimeMillis())
                .setAutoCancel(true)
                .setOngoing(true);
        Notification notification = builder.getNotification();
        notification.defaults = Notification.DEFAULT_SOUND;
        NotificationManager manager = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(0, notification);
    }
}
