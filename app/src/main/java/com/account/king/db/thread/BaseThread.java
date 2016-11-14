package com.account.king.db.thread;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import com.account.king.callback.DaoRequesrCallBack;

import java.lang.ref.WeakReference;

/**
 * Created by xupangen
 * on 2016/10/15.
 */
public class BaseThread extends Thread {
    protected WeakReference<SQLiteDatabase> dbRef;
    protected String table ;
    protected ContentValues contentValues;
    protected DaoRequesrCallBack callBack;

    protected boolean flag = false;
    protected long id = 0;
    protected  SQLiteDatabase db;
    public BaseThread(SQLiteDatabase db,String table,ContentValues values,DaoRequesrCallBack callBack){
        this.dbRef = new WeakReference<SQLiteDatabase>(db);
        this.table = table;
        this.contentValues = values;
        this.callBack = callBack;
    }

    @Override
    public void run() {
        super.run();
    }
}
