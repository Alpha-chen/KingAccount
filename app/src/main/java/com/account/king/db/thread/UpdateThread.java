package com.account.king.db.thread;


import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

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

    public UpdateThread(SQLiteDatabase db, String table, ContentValues values) {
        this.dbRef = new WeakReference(db);
        this.table = table;
        this.values = values;
        this.whereClause = whereClause;
        this.whereArgs = whereArgs;
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

    }
}
