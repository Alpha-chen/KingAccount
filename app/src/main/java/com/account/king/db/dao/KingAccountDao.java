package com.account.king.db.dao;

import android.content.Context;

import com.account.king.db.DBHelper;
import com.account.king.node.KingAccountNode;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.ArrayList;

/**
 * 账单操作的类
 * Created by King
 * on 2016/11/21.
 */

public class KingAccountDao {

    private Dao<KingAccountNode, Integer> clazzDao;
    private DBHelper helper;

    public KingAccountDao(Context context) throws SQLException {
        helper = DBHelper.getInstances(context);
        clazzDao = helper.getDao(KingAccountNode.class);
    }

    public DBHelper getHelper() {
        return helper;
    }

    /**
     * 插入一条数据  返回id
     *
     * @param data
     * @return
     * @throws SQLException
     */
    public int create(Object data) {
        KingAccountNode node = (KingAccountNode) data;
        try {
            node = clazzDao.createIfNotExists(node);
            return node.getId();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * 删除成功返回1
     *
     * @param data
     * @return
     * @throws SQLException
     */
    public int delete(Object data) {
        KingAccountNode node = (KingAccountNode) data;
        try {
            return clazzDao.delete(node);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * 更新成功返回1
     *
     * @param data
     * @return
     * @throws SQLException
     */
    public int update(Object data) {
        KingAccountNode node = (KingAccountNode) data;
        try {
            return clazzDao.update(node);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * 根据主表second_id 查找
     *
     * @param id
     * @return
     */
    public KingAccountNode queryForId(int id) {
        try {
            KingAccountNode bookNode = clazzDao.queryForId(id);
            if (bookNode == null) {
                return null;
            }
            return bookNode;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    public ArrayList<KingAccountNode>  queryForAll(){
        try {
            ArrayList<KingAccountNode> bookNode = (ArrayList<KingAccountNode>) clazzDao.queryForAll();
            if (bookNode == null) {
                return null;
            }
            return bookNode;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
