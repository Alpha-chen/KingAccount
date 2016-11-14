package com.example.administrator.databasetest.db.thread;

import android.database.sqlite.SQLiteDatabase;

import com.example.administrator.databasetest.callback.DaoRequesrCallBack;

import java.lang.ref.WeakReference;

/**
 * Created by xupangen
 * on 2016/10/15.
 */
public class DeleteThread  extends Thread {
    protected WeakReference<SQLiteDatabase> dbRef;
    protected String table ;
    protected DaoRequesrCallBack callBack;
    private String whereClause ;
    private String[] whereArgs;

    public DeleteThread(SQLiteDatabase db, String table, String whereClause, String[] whereArgs, DaoRequesrCallBack callBack){
        this.dbRef = new WeakReference<SQLiteDatabase>(db);
        this.table = table;
        this.whereClause = whereClause;
        this.whereArgs =whereArgs;
        this.callBack = callBack;
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
        if (null !=callBack){
            if (flag){
                callBack.onSucess(id);
            }else {
                callBack.onFailure();
            }
        }
    }
}
