package com.example.administrator.databasetest.db.thread;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import com.example.administrator.databasetest.callback.DaoRequesrCallBack;

/**
 * Created by xupangen
 * on 2016/10/15.
 */
public class InsertThread extends  BaseThread {
    public InsertThread(SQLiteDatabase db,String table,ContentValues values,DaoRequesrCallBack callBack){
      super(db,table,values,callBack);
    }

    @Override
    public void run() {
        super.run();
        db = dbRef.get();
        try {
            db.beginTransaction();
            id =db.insert(table,null,contentValues);
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
