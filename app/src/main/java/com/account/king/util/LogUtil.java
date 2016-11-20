package com.account.king.util;

import android.util.Log;

import com.account.king.BuildConfig;
import com.account.king.constant.Constant;


/**
 *
 */
public class LogUtil {

    public static final boolean doLog = BuildConfig.DEBUG;
    public static void d(String TAG, String log) {
        if (!doLog) {
            return;
        }
        Log.d(Constant.KingAccount, "[" + TAG + "]:" + log);
    }
    public static void d( String log) {
        if (!doLog) {
            return;
        }
        Log.d(Constant.KingAccount, "#######################################=" + log);
    }
    public static void d( int log) {
        if (!doLog) {
            return;
        }
        Log.d(Constant.KingAccount, "#######################################=" + log);
    }
}
