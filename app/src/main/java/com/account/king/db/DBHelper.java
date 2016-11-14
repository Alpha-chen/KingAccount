package com.account.king.db;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.account.king.FeedNode;


/**
 * Created by xupangen
 * on 2016/10/15.
 */
public class DBHelper extends SQLiteOpenHelper {

    private static final String TABLE_FEED="table_feed";
    private static DBHelper dBhelper;
    private static String DB_NAME= "feedtest_1.0.db";
    private static int DB_VERSION =1;

    /**
     * 单例获取数据库对象
     * @param context
     * @return
     */
    public static synchronized  DBHelper getInstances(Context context){
        context= context.getApplicationContext();
        if (null == dBhelper){
            synchronized (DBHelper.class){
                if (null == dBhelper){
                    dBhelper = new DBHelper(context.getApplicationContext(),DB_NAME,null,DB_VERSION);
                }
            }
        }
        return dBhelper;
    }

    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        StringBuilder CREATE_FEED_TABLE = new StringBuilder(
                "CREATE TABLE IF NOT EXISTS").append(TABLE_FEED)
                .append(" ( ")
                .append(FeedNode.ID)
                .append("  integer PRIMARY KEY autoincrement,")
                .append(FeedNode.MOOD)
                .append(FeedNode.COMMENT)
                .append(FeedNode.FEEDSTATUS).append(" integer,")
                .append(FeedNode.LINK);

        if (null!=db){
            db.execSQL(CREATE_FEED_TABLE.toString());
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //更新数据库的时候就是 根据不同的数据库版本，执行不同的数据库更新操作

    }
}
