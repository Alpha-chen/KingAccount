package com.account.king.db;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.account.king.node.AccountNode;


/**
 * Created by King
 * on 2016/10/15.
 */
public class DBHelper extends SQLiteOpenHelper {

    private static final String TABLE_ACCOUNT="king_account";
    private static DBHelper dbHelper;
    private static String DB_NAME= "account_1.0.db";
    private static int DB_VERSION =1;

    /**
     * 单例获取数据库对象
     * @param context
     * @return
     */
    public static synchronized  DBHelper getInstances(Context context){
        context= context.getApplicationContext();
        if (null == dbHelper){
            synchronized (DBHelper.class){
                if (null == dbHelper){
                    dbHelper = new DBHelper(context.getApplicationContext(),DB_NAME,null,DB_VERSION);
                }
            }
        }
        return dbHelper;
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
                "CREATE TABLE IF NOT EXISTS").append(TABLE_ACCOUNT)
                .append(" ( ")
                .append(AccountNode.ID)
                .append("  integer PRIMARY KEY autoincrement, ")
                .append(AccountNode.PRICE).append(", ")
                .append(AccountNode.COUNT).append(", ")
                .append(AccountNode.ACCOUNT_TYPE).append(", ")
                .append(AccountNode.TYPE).append(", ")
                .append(AccountNode.DATE_YMD).append(", ")
                .append(AccountNode.TIME_HMS).append(", ")
                .append(AccountNode.ATTACHMENT).append(" )");

        if (null!=db){
            db.execSQL(CREATE_FEED_TABLE.toString());
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //更新数据库的时候就是 根据不同的数据库版本，执行不同的数据库更新操作
    }
}
