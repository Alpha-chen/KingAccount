package com.account.king.util;

import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.view.WindowManager;

import com.account.king.R;


/**
 */
public class PermissionUtil {

    //6.0  permission denied for this window type  弹框权限
    //大多数手机暂时不需要获取这个权限也能弹出弹框，只有少数手机无权限会弹不出甚至闪退，所以只能申请权限
    public static void getAlertPermission(Context context, Dialog dialog) {
        //无弹框权限（小米4青春版）
        //android.view.WindowManager$BadTokenException:
        //Unable to add window android.view.ViewRootImpl$W@8348a4e -- permission denied for this window type
        //获取权限页面找不到  定制坑爹（联想手机）
        //android.content.ActivityNotFoundException:
        // No Activity found to handle Intent
        // { act=android.settings.action.MANAGE_OVERLAY_PERMISSION dat=package:net.ffrj.pinkwallet }
        //做法  先直接弹出弹框，若报错，则获取权限
        if (Build.VERSION.SDK_INT >= 23) {
            try {
                dialog.show();
            } catch (WindowManager.BadTokenException e) {
                e.printStackTrace();
                LogUtil.d("nnn","e="+e.getMessage());
                if (!Settings.canDrawOverlays(context)) {
                    try {
                        ToastUtil.makeToast(context, R.string.permission_alert);
                        Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                                Uri.parse("package:" + context.getPackageName()));
                        context.startActivity(intent);
                    } catch (ActivityNotFoundException e1) {
                        e1.printStackTrace();
                        try {
                            dialog.show();
                        }catch (Exception e2){
                            e2.printStackTrace();
                        }
                    }
                } else {
                    //绘ui代码, 这里说明6.0系统已经有权限了
                    dialog.show();
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        } else {
            //绘ui代码,这里android6.0以下的系统直接绘出即可
            dialog.show();
        }

    }

}
