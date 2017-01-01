package com.account.king.util;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.DisplayMetrics;
import android.webkit.MimeTypeMap;

import java.io.File;
import java.util.List;
import java.util.regex.Pattern;

/**
 * 窗口调用相关
 */
public class ActivityLib {
    /**
     * 界面间传送对象key
     */
    public static final String INTENT_PARAM = "object";
    /**
     * 界面间传送对象key
     */
    public static final String INTENT_PARAM2 = "object2";

    /**
     * 界面间传送对象key
     */
    public static final String INTENT_PARAM3 = "object3";

    /**
     * 界面间传送对象key
     */
    public static final String INTENT_PARAM4 = "object4";

    /**
     * 界面间传送对象key
     */
    public static final String INTENT_PARAM5 = "object5";

    /**
     * 是否编辑
     */
    public static final String START_TYPE = "start_type";
    /**
     * 新建
     */
    public static final int START_TYPE_NEW = 0;
    /**
     * 查看
     */
    public static final int START_TYPE_VIEW = 1;
    /**
     * 编辑
     */
    public static final int START_TYPE_EDIT = 2;

    /**
     * 从第几张图片开始浏览
     */
    public static final String SRART_IMG = "start_img";


    /**
     * 浏览图片时传递的key
     */
    public static final String ATTACHMENTS_LIST = "attachments_list";

    public static final String TAG = "ActivityLib";


    /**
     * 调用系统的看图程序，直接打开图片，无关联程序列表对话框确认
     */
    public static void OpenImage(Context packageContext, String imageFileName) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        MimeTypeMap myMime = MimeTypeMap.getSingleton();
        String mimeType = myMime.getMimeTypeFromExtension(imageFileName
                .substring(imageFileName.lastIndexOf(".") + 1));
        intent.setDataAndType(Uri.fromFile(new File(imageFileName)), mimeType);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        packageContext.startActivity(intent);
    }

    public static String getFloatIntegerString(float parm) {
        String result = parm + "";
        String mowei = result.substring(result.indexOf(".") + 1);
        if (mowei.equals("0") || mowei.equals("00")) {
            result = result.substring(0, result.indexOf("."));
        }
        return result;
    }

    /**
     * 得到屏幕的宽度和高度，0-宽度，1-高度
     */
    public static int[] getScreenSize(Context context) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        int[] size = new int[2];
        size[0] = dm.widthPixels;
        size[1] = dm.heightPixels;
        return size;
    }

    /**
     * 检查是否为有效的邮件格式
     */
    public static boolean CheckEmail(String strEmail) {
        Pattern p = Pattern
                .compile("^([a-zA-Z0-9_\\.-])+@(([a-zA-Z0-9-])+\\.)+([a-zA-Z0-9]{2,4})+$");
        return p.matcher(strEmail).matches();
    }

    /**
     * 判断字符串是否为空
     */
    public static boolean isEmpty(String string) {
        return null == string || "null".equals(string)
                || string.trim().length() < 1;
    }
    public static boolean isEmpty(List string) {
        return null == string || string.size() == 0;
    }

}
