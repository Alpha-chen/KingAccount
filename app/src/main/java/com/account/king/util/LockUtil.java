package com.account.king.util;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.account.king.InputLockActivity;
import com.account.king.KingApplication;
import com.account.king.R;
import com.account.king.SetLockActivity;
import com.account.king.view.dialog.ToastDialog;


/**
 * @author king
 */
public class LockUtil {

    // 如果用户有设置密码锁，则在退出程序时将标识置为false
    //输入正确的密码锁之后 标识为true
    public static boolean pwdlocker_open = false;

    public static boolean isBackApp(Context context) {
        return SPUtils.getBoolean(context, SPUtils.LOCK_BACK_APP);
    }

    public static void toBackAPP(Context context) {
        //用户未设置密码锁  不设置
        if (!KingApplication.isLock) {
            return;
        }
        SPUtils.put(context, SPUtils.LOCK_BACK_APP, true);
    }

    public static void isNeedLock(Context context) {
        boolean isBackApp = SPUtils.getBoolean(context, SPUtils.LOCK_BACK_APP);
        SPUtils.remove(context, SPUtils.LOCK_BACK_APP);
        if (!isBackApp) {
            return;
        }
        if (!KingApplication.isLock) {
            return;
        }

        Intent intent = new Intent();
        intent.setClass(context, InputLockActivity.class);
        context.startActivity(intent);
    }

    //重置密码锁 1=忘记密码  2=5次输入错误
    public static void resetLock(Context context, int index) {
        SPUtils.remove(context, SPUtils.LOCK_OPEN_);
        SPUtils.remove(context, SPUtils.LOCK_);
    }

    //密码锁界面 输入5次密码或忘记密码时 再次登录会弹出去设置密码锁dialog
    public static void openRestLockHint(final Context context) {
        int lockIndex = SPUtils.getInt(context, SPUtils.LOCK_FORGET_LOGIN);
        if (lockIndex != 0) {
            ToastDialog dialog = new ToastDialog(context);
            if (lockIndex == 1) {
                dialog.setHintText(R.string.lock_reset);
            } else {
                dialog.setHintText(R.string.lock_reset2);
            }
            dialog.setType(1);
            dialog.setOnclick(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    context.startActivity(new Intent(context, SetLockActivity.class));
                }
            });
            PermissionUtil.getAlertPermission(context, dialog);
            SPUtils.remove(context, SPUtils.LOCK_FORGET_LOGIN);
        }
    }
}
