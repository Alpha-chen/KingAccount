package com.account.king.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.account.king.node.KingAccountNode;
import com.account.king.util.LogUtil;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import static android.content.ContentValues.TAG;


/**
 * Created by King
 * on 2016/10/15.
 */
public class DBHelper extends OrmLiteSqliteOpenHelper {

    private static final String TABLE_ACCOUNT="king_account";
    private static DBHelper dbHelper;
    private static String DB_NAME= "account_1.0.db";
    private static int DB_VERSION =1;

    private Map<String, Dao> daos = new HashMap<String, Dao>();

    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        LogUtil.d(TAG, "DBOpenHelper");
    }

    /**
     * 单例获取数据库对象
     * @param context
     * @return
     */
    public static synchronized  DBHelper getInstances(Context context){
        if (null == dbHelper){
            synchronized (DBHelper.class){
                if (null == dbHelper){
                    dbHelper = new DBHelper(context.getApplicationContext());
                }
            }
        }
        return dbHelper;
    }


    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        LogUtil.d(TAG, "onCreate");
        try {
            TableUtils.createTableIfNotExists(connectionSource, KingAccountNode.class);
          /*  StringBuilder CREATE_FEED_TABLE = new StringBuilder(
                    " CREATE TABLE IF NOT EXISTS ").append(TABLE_ACCOUNT)
                    .append(" ( ")
                    .append(KingAccountNode.ID)
                    .append("  integer PRIMARY KEY autoincrement, ")
                    .append(KingAccountNode.PRICE).append(", ")
                    .append(KingAccountNode.COUNT).append(", ")
                    .append(KingAccountNode.ACCOUNT_TYPE).append(", ")
                    .append(KingAccountNode.TYPE).append(", ")
                    .append(KingAccountNode.YMD_HMS).append(", ")
                    .append(KingAccountNode.ATTACHMENT).append(" )");

            if (null!=database){
                database.execSQL(CREATE_FEED_TABLE.toString());
            }*/

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        //更新数据库的时候就是 根据不同的数据库版本，执行不同的数据库更新操作

    }

    /****
     * 根据传入dao的类名找到相应的dao
     * @param clazz
     * @return
     * @throws SQLException
     */
    public synchronized Dao getDao(Class clazz) throws SQLException {
        Dao dao = null;
        String className = clazz.getSimpleName();

        if (daos.containsKey(className)) {
            dao = daos.get(className);
        }
        if (dao == null) {
            dao = super.getDao(clazz);
            daos.put(className, dao);
        }
        return dao;
    }
    /**
     * 释放资源
     */
    @Override
    public void close() {
        super.close();
        for (String key : daos.keySet()) {
            Dao dao = daos.get(key);
            dao = null;
        }
    }
}
