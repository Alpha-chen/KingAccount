package com.account.king.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;



public class AppUtils {
    static String versionName = "";

    /**
     * 获取版本名称字符串
     *
     * @param context
     * @return
     */
    public static String getVersionName(Context context) {
        if (!TextUtils.isEmpty(versionName)) {
            return versionName;
        }
        try {
            versionName = context.getPackageManager().getPackageInfo(
                    context.getPackageName(), 0).versionName;
            versionName = versionName.replaceAll("[a-zA-Z]", "");
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionName.trim();
    }

    /**
     * 获取应用程序名称
     *
     * @param context
     * @return
     */
    public static String getAppName(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(
                    context.getPackageName(), 0);
            int labelRes = packageInfo.applicationInfo.labelRes;
            return context.getResources().getString(labelRes);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取版本号数字
     *
     * @param context
     * @return
     */
    public static int getVersionCode(Context context) {
        int intVerCode = 0;
        try {
            intVerCode = context.getPackageManager().getPackageInfo(
                    context.getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return intVerCode;
    }

}
