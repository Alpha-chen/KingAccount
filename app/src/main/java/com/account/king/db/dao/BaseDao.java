package com.account.king.db.dao;

import android.content.Context;

import com.account.king.callback.DaoRequesrCallBack;
import com.account.king.db.DBHelper;


/**
 * 所有的增删改查的 最底层的操作
 * Created by xupangen
 * on 2016/10/15.
 */
public abstract class BaseDao {

    public DBHelper dbHelper;

    public BaseDao(Context context){
        dbHelper= DBHelper.getInstances(context);
    }

    public void insert (Object object, DaoRequesrCallBack  callBack){
    }

    public void delete (Object object, DaoRequesrCallBack callBack){
    }

    public void update (Object object, DaoRequesrCallBack  callBack){
    }
    public Object  select (String object, DaoRequesrCallBack  callBack){
        return null;
    }



}
