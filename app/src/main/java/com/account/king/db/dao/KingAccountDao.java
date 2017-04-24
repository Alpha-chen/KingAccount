package com.account.king.db.dao;

import android.content.Context;
import android.text.TextUtils;

import com.account.king.db.DBHelper;
import com.account.king.node.Attachment;
import com.account.king.node.KingAccountNode;
import com.account.king.util.KingJson;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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


    public ArrayList<KingAccountNode> queryForAll() {
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


    //降序
    private QueryBuilder getOrderByFalse() {
        return clazzDao.queryBuilder().orderBy(KingAccountNode.YMD_HMS, false);
    }

    //顺序
    private QueryBuilder getOrderByTrue() {
        return clazzDao.queryBuilder().orderBy(KingAccountNode.YMD_HMS, true);
    }

    private Map<String, Object> getTableBookType() {
        Map<String, Object> map = new HashMap<>();
        map.put(KingAccountNode.TYPE, DBHelper.TABLE_ACCOUNT);
        return map;
    }

    public List<KingAccountNode> queryForIdAndNoteType(int accountType, long start, long end, int type
            , String note) {
        try {
            Where<KingAccountNode, Integer> where = getOrderByFalse().where();
            where.and().eq(KingAccountNode.ACCOUNT_TYPE, accountType);
            where.and().between(KingAccountNode.YMD_HMS, start, end);
            if (!TextUtils.isEmpty(note)) {
                where.and().like(KingAccountNode.ATTACHMENT, "%" + note + "%");
            }
            where.and().eq(KingAccountNode.TYPE, type);
            return queryWhere(where, getTableBookType());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * where 条件拼接  equal
     *
     * @param where
     * @param map
     * @return
     */
    private List<KingAccountNode> queryWhere(Where<KingAccountNode, Integer> where, Map<String, Object> map) {
        try {
            Iterator iterator = map.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry entry = (Map.Entry) iterator.next();
                String key = (String) entry.getKey();
                Object val = entry.getValue();
                where.and().eq(key, val);
            }
            return where.query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void setPhotoPath(KingAccountNode bookNode) {
        //json转对象
        bookNode.setAttachment(KingJson.parseObject(bookNode.getAttachmentStr(), Attachment.class));
    }
}
