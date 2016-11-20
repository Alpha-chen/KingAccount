package com.account.king.db.thread;

import android.database.sqlite.SQLiteDatabase;

import java.lang.ref.WeakReference;

/**
 * Created by xupangen
 * on 2016/10/15.
 */
public class DeleteThread  extends Thread {
    protected WeakReference<SQLiteDatabase> dbRef;
    protected String table ;
    private String whereClause ;
    private String[] whereArgs;

    public DeleteThread(SQLiteDatabase db, String table, String whereClause, String[] whereArgs){
        this.dbRef = new WeakReference<SQLiteDatabase>(db);
        this.table = table;
        this.whereClause = whereClause;
        this.whereArgs =whereArgs;
    }

    @Override
    public void run() {
        super.run();
        boolean flag = false;
        long id = 0;
        SQLiteDatabase db;
        db = dbRef.get();
        try {
            db.beginTransaction();
            id =db.delete(table,whereClause,whereArgs);
            db.setTransactionSuccessful();
            if ( id > 0 ){
                flag = true;
            }
        }catch (Exception e){
            e.printStackTrace();
            flag = false;
        }finally {
            if (db.inTransaction()){
                db.endTransaction();
            }
        }
    }
}
