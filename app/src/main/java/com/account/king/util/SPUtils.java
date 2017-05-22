package com.account.king.util;

import android.content.Context;
import android.content.SharedPreferences;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

public class SPUtils {
    /**
     * 保存在手机里面的文件名 默认
     */
    static final String FILE_NAME = "share_data";

    /*
    token
    * */
    public static final String USERINFO_ISLOGIN = "isLogin";
    public static final String USERINFO = "user_info";
    public static final String USER_ACCOUNT = "user_account";
    public static final String BUDGET_DAY = "budget_day";
    public static final String REMIND_START = "remind_start";
    public static final String REMIND_HOUR = "remind_hour";
    public static final String REMIND_MINUTE = "remind_minute";
    public static final String REPEAT_MODEL = "repeat_model";
    public static final String REMIND_ID = "remind_id";
    public static final String RECORD_UPDATE_TIME = "record_update_time_";
    public static final String BUDGET_UPDATE_TIME = "budget_update_time_";
    public static final String TYPE_UPDATE_TIME = "type_update_time_";
    public static final String BOOK_UPDATE_TIME = "book_update_time_";
    public static final String BUDGET_OPEN = "budget_open_";
    public static final String ACCOUNT_BOOK_ = "account_book_";//选中的账本
    public static final String LOCK_ = "lock_";//密码锁 value
    public static final String LOCK_OPEN_ = "lock_open_";//密码锁 是否开启
    public static final String LOCK_FORGET_LOGIN = "lock_forget_login_";//密码锁 忘记后 重新登录
    public static final String LOCK_BACK_APP = "lock_back_app";//密码锁home键退出
    public static final String LAUNCH = "launch";//

    public static final String REMIND_LOCK_PASSWORD = "REMIND_LOCK_PASSWORD";

    public static final String MONEY_TYPE_OUT = "money_type_out";
    public static final String MONEY_TYPE_IN = "money_type_in";

    /**
     * 保存数据的方法，我们需要拿到保存数据的具体类型，然后根据类型调用不同的保存方法
     *
     * @param context
     * @param key
     * @param object
     */
    public static void put(Context context, String key, Object object) {
        put(context, FILE_NAME, key, object);
    }

    public static void put(Context context, String storeFile, String key, Object object) {
        if (null == object) {
            return;
        }
        SharedPreferences sp = context.getSharedPreferences(storeFile,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();

        if (object instanceof String) {
            editor.putString(key, (String) object);
        } else if (object instanceof Integer) {
            editor.putInt(key, (Integer) object);
        } else if (object instanceof Boolean) {
            editor.putBoolean(key, (Boolean) object);
        } else if (object instanceof Float) {
            editor.putFloat(key, (Float) object);
        } else if (object instanceof Long) {
            editor.putLong(key, (Long) object);
        } else {
            editor.putString(key, object.toString());
        }
        SharedPreferencesCompat.apply(editor);
    }

    /**
     * 得到保存数据的方法
     */
    public static int getInt(Context context, String key) {
        return getInt(context, FILE_NAME, key);
    }

    public static int getInt(Context context, String storeFile, String key) {
        SharedPreferences sp = context.getSharedPreferences(storeFile,
                Context.MODE_PRIVATE);
        return sp.getInt(key, 0);
    }

    public static String getString(Context context, String key) {
        return getString(context, FILE_NAME, key, "");
    }

    public static String getString(Context context, String key, String value) {
        return getString(context, FILE_NAME, key, value);
    }

    public static String getString(String storeFile, String key, Context context) {
        return getString(context, storeFile, key, "");
    }

    public static String getString(Context context, String storeFile, String key, String value) {
        SharedPreferences sp = context.getSharedPreferences(storeFile,
                Context.MODE_PRIVATE);
        return sp.getString(key, value);
    }

    public static Boolean getBoolean(Context context, String key) {
        return getBoolean(context, FILE_NAME, key);
    }

    public static Boolean getBoolean(Context context, String key, boolean value) {
        return getBoolean(context, FILE_NAME, key, value);
    }

    public static Boolean getBoolean(Context context, String storeFile, String key) {
        return getBoolean(context, storeFile, key, false);
    }

    public static Boolean getBoolean(Context context, String storeFile, String key, boolean value) {
        SharedPreferences sp = context.getSharedPreferences(storeFile,
                Context.MODE_PRIVATE);
        return sp.getBoolean(key, value);
    }

    public static Float getFloat(Context context, String key) {
        return getFloat(context, FILE_NAME, key);
    }

    public static Float getFloat(Context context, String storeFile, String key) {
        SharedPreferences sp = context.getSharedPreferences(storeFile,
                Context.MODE_PRIVATE);
        return sp.getFloat(key, 0);
    }

    public static Long getLong(Context context, String key) {
        return getLong(context, FILE_NAME, key);
    }

    public static Long getLong(Context context, String storeFile, String key) {
        SharedPreferences sp = context.getSharedPreferences(storeFile,
                Context.MODE_PRIVATE);
        return sp.getLong(key, 0);
    }

    /**
     * 移除某个key值已经对应的值
     *
     * @param context
     * @param key
     */
    public static void remove(Context context, String key) {
        remove(context, FILE_NAME, key);
    }

    public static void remove(Context context, String storeFile, String key) {
        SharedPreferences sp = context.getSharedPreferences(storeFile,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.remove(key);
        SharedPreferencesCompat.apply(editor);
    }

    /**
     * 清除所有数据
     *
     * @param context
     */
    public static void clear(Context context) {
        clear(context, FILE_NAME);
    }

    public static void clear(Context context, String storeFile) {
        SharedPreferences sp = context.getSharedPreferences(storeFile,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.clear();
        SharedPreferencesCompat.apply(editor);
    }

    /**
     * 查询某个key是否已经存在
     *
     * @param context
     * @param key
     * @return
     */
    public static boolean contains(Context context, String key) {
        return contains(context, FILE_NAME, key);
    }

    public static boolean contains(Context context, String storeFile, String key) {
        SharedPreferences sp = context.getSharedPreferences(storeFile,
                Context.MODE_PRIVATE);
        return sp.contains(key);
    }

    /**
     * 返回所有的键值对
     *
     * @param context
     * @return
     */
    public static Map<String, ?> getAll(Context context) {
        return getAll(context, FILE_NAME);
    }

    public static Map<String, ?> getAll(Context context, String storeFile) {
        SharedPreferences sp = context.getSharedPreferences(storeFile,
                Context.MODE_PRIVATE);
        return sp.getAll();
    }

    /**
     * 创建一个解决SharedPreferencesCompat.apply方法的一个兼容类
     *
     * @author zhy
     */
    private static class SharedPreferencesCompat {
        private static final Method sApplyMethod = findApplyMethod();

        /**
         * 反射查找apply的方法
         *
         * @return
         */
        @SuppressWarnings({"unchecked", "rawtypes"})
        private static Method findApplyMethod() {
            try {
                Class clz = SharedPreferences.Editor.class;
                return clz.getMethod("apply");
            } catch (NoSuchMethodException e) {
            }

            return null;
        }

        /**
         * 如果找到则使用apply执行，否则使用commit
         *
         * @param editor
         */
        public static void apply(SharedPreferences.Editor editor) {
            try {
                if (sApplyMethod != null) {
                    sApplyMethod.invoke(editor);
                    return;
                }
            } catch (IllegalArgumentException e) {
            } catch (IllegalAccessException e) {
            } catch (InvocationTargetException e) {
            }
            editor.commit();
        }
    }

}