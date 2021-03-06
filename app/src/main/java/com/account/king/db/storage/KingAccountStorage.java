package com.account.king.db.storage;

import android.content.Context;

import com.account.king.db.dao.KingAccountDao;
import com.account.king.node.KingAccountNode;
import com.account.king.util.LogUtil;
import com.j256.ormlite.misc.TransactionManager;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * Created by King
 * on 2016/11/21.
 */

public class KingAccountStorage {

    private KingAccountDao bookDao;
    private Context mContext;

    public KingAccountStorage(Context context) {
        try {
            this.mContext = context;
            bookDao = new KingAccountDao(mContext);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 创建流水账
     *
     * @param bookNode
     * @return
     */
    public boolean create(KingAccountNode bookNode) {
        int second_id = bookDao.create(bookNode);
        if (second_id == -1) {
            return false;
        }

        return true;
    }


    public void deleteList(final List<KingAccountNode> bookNodes) {
        if (bookNodes == null) {
            return;
        }
        try {
            TransactionManager.callInTransaction(bookDao.getHelper().getConnectionSource(), new Callable<Void>() {
                @Override
                public Void call() throws Exception {
                    for (KingAccountNode bookNode : bookNodes) {
                        LogUtil.d("nnn", "bookNode=" + bookNode.toString());
                        delete(bookNode);
                    }
                    return null;
                }
            });
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 删除流水账
     *
     * @param bookNode
     * @return
     */
    public boolean delete(KingAccountNode bookNode) {
        int result = bookDao.delete(bookNode);
        if (result == 1) {
            return true;
        }
        return false;
    }

    /**
     * 更新流水账
     *
     * @param bookNode
     * @return
     */
    public boolean update(KingAccountNode bookNode) {
        int result = bookDao.update(bookNode);
        if (result == 1) {
            return true;
        }
        return false;
    }


    /**
     * 按照时间降序查找全部  指定账本
     *
     * @param
     */
    public KingAccountNode query(String accountId) {
        return bookDao.queryForId(Integer.valueOf(accountId));
    }

    public ArrayList<KingAccountNode> queryAll() {
        return bookDao.queryForAll();
    }

    /**
     * 查询时间内账单数据
     *
     * @param start
     * @param end
     * @param note  备注
     * @return
     */
    public List<KingAccountNode> queryForTimeAndNoteType(int accountType, long start, long end, int type, String note) {
        return bookDao.queryForIdAndNoteType(accountType, start, end, type, note);
    }

    public List<KingAccountNode> queryForTime(int accountType, long start, long end) {
        return bookDao.queryForIdAndTime(accountType, start, end);
    }
    public List<KingAccountNode> queryForType(int accountType, long start, long end,int type ) {
        return bookDao.queryForType( accountType,start, end,type);
    }

}
