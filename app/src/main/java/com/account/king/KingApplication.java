package com.account.king;

import android.annotation.TargetApi;
import android.app.Application;
import android.content.Context;
import android.os.Build;

import com.account.king.util.LogUtil;
import com.account.king.util.SPUtils;

/**
 * Created by King
 * on 2016/11/21.
 */
@TargetApi(Build.VERSION_CODES.GINGERBREAD)
public class KingApplication extends Application {
    public static KingApplication mApplication;
    public static Context appContext;
    public static String accountId;//账本id
    public static boolean isLock = false;

    @Override
    public void onCreate() {
        super.onCreate();
        mApplication = this;
        appContext = this;
        LogUtil.d("KingApplication", "appContext->=" + appContext);
        LogUtil.d("KingApplication", "mApplication->=" + mApplication);
        restoreData();
    }

    //切换登录  重置用户信息
    public static void restoreData() {
        restoreLock();
    }

    //获取密码锁信息
    public static void restoreLock() {
        isLock = false;
        isLock = SPUtils.getBoolean(appContext, SPUtils.LOCK_OPEN_);
    }
}
