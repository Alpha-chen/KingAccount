package com.example.administrator.databasetest.db.thread;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import com.example.administrator.databasetest.callback.DaoRequesrCallBack;

import java.lang.ref.WeakReference;

/**
 * Created by xupangen
 * on 2016/10/17.
 */
public class UpdateThread  extends Thread{
    WeakReference<SQLiteDatabase> dbRef;
    String table;
    String whereClause;
    String[] whereArgs;
    ContentValues values;
    private DaoRequesrCallBack callback;

    public UpdateThread(SQLiteDatabase db, String table, ContentValues values, DaoRequesrCallBack callBack) {
        this.dbRef = new WeakReference(db);
        this.table = table;
        this.values = values;
        this.whereClause = whereClause;
        this.whereArgs = whereArgs;
        this.callback = callback;
    }


    @Override
    public void run() {
        super.run();
        boolean flag = false;
        long id = 0;
        SQLiteDatabase db = dbRef.get();
        if (null == db || TextUtils.isEmpty(table)) {
            return;
        }
        try {
            db.beginTransaction();
            id = db.update(table, values, whereClause, whereArgs);
            db.setTransactionSuccessful();
            flag = true;
        } catch (Exception e) {
            e.printStackTrace();
            flag = false;
        } finally {
            if (db.inTransaction()) {
                db.endTransaction();
            }
        }
        if (null != callback) {
            if (flag) {
                callback.onSucess(id);
            } else {
                callback.onFailure();
            }
        }
        //closeDatabase(db);
    }
}
