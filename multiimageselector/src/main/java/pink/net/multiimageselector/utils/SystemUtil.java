package pink.net.multiimageselector.utils;

import android.os.Environment;

import java.io.File;

/**
 * 得到手机的一些状态和信息
 *
 * @author Administrator
 */
public class SystemUtil {


    /**
     * 应用跟路径分用户。
     */
    public static final String APPROOT = "fenfenWallet/";


    /**
     * 得到手机sd卡的目录。
     *
     * @return
     */
    public static final String TAG = "SystemUtil";

    public static String getSDCardRoot() {
        String state = Environment.getExternalStorageState();
        // 判断SdCard是否存在并且是可用的
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            if (Environment.getExternalStorageDirectory().canWrite()) {
                return Environment.getExternalStorageDirectory()
                        .getPath() + "/";
            }
        }
        return null;
    }

    /**
     * 判断sd卡是不是可用。
     *
     * @return
     */
    public static boolean sdcardUsable() {
        String state = Environment.getExternalStorageState();
        // 判断SdCard是否存在并且是可用的
        if (Environment.MEDIA_MOUNTED.equals(state)) {   // 是否插着sd卡
            File sdFile = Environment.getExternalStorageDirectory();
            return sdFile.exists() && sdFile.canRead() && sdFile.canWrite();
        } else {
            return false;
        }
    }

    public static String getImageFolder() {
        StringBuilder sb = new StringBuilder();
        sb.append(getAppRoot()).append("image/");
        String path = sb.toString();
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
        return path;
    }

    public static String getCacheFolder() {
        StringBuilder sb = new StringBuilder();
        sb.append(getAppRoot()).append("cache/");
        String path = sb.toString();
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
        return path;
    }

    /**
     * @return
     */
    public static String getAppRoot() {
        String sdCardRoot = getSDCardRoot();
        if (sdCardRoot == null) {
            return null;
        }
        String path = getSDCardRoot() + APPROOT;
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
        // 隐藏xiaoxiaotu文件夹下的图片
        try {
            new File(path, ".nomedia").createNewFile();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return path;
    }

    /**
     * 存储照片的文件。
     *
     * @return
     */
    public static String getPhotoFolder() {
        StringBuilder sb = new StringBuilder();
        sb.append(getAppRoot()).append("pinkimg/");
        String path = sb.toString();
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
        return path;
    }

}
