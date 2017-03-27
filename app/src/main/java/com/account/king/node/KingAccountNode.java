package com.account.king.node;

import android.text.TextUtils;

import com.account.king.util.KingJson;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

/**
 * Created by king
 * on 2016/11/20.
 */

@DatabaseTable(tableName = "king_account")
public class KingAccountNode implements Serializable {
    /**
     * id : 0
     * price : 1.8
     * count : 1.5
     * account_type : 0
     * type : 1
     * attachment : {"content":"今天发工资哎","attachment_path":"https://ss0.bdstatic.com/5aV1bjqh_Q23odCf/static/superman/img/logo/logo_white_fe6da1ec.png"}
     * ymd_hms : 20161120
     * time_hms : 20:10:45
     */
    @DatabaseField(generatedId = true)
    private int id;
    public static String ID = "id";
    @DatabaseField(columnName = "price")
    private double price;
    public static String PRICE = "price";
    @DatabaseField(columnName = "count")
    private double count;
    public static String COUNT = "count";
    /**
     * 收入/支出
     */
    @DatabaseField(columnName = "account_type")
    private int account_type = 0;
    public static String ACCOUNT_TYPE = "account_type";
    public static int MONEY_IN = 0; // 收入
    public static int MONEY_OUT = 1; // 支出
    /**
     * 类型 薪水/交通
     */
    @DatabaseField(columnName = "type")
    private int type;
    public static String TYPE = "type";
    /**
     * 附件信息
     */
    @DatabaseField(columnName = "attachment")
    private String attachment;
    public static String ATTACHMENT = "attachment";
    private Attachment mAttachment;
    @DatabaseField(columnName = "extend")
    private String extend;
    public static String EXTEND = "extend";

    /**
     * 记录时间
     */
    @DatabaseField(columnName = "ymd_hms")
    private long ymd_hms;
    public static String YMD_HMS = "ymd_hms";

    public KingAccountNode() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getPrice() {
        return price;
    }

    public String getExtend() {
        return extend;
    }

    public void setExtend(String extend) {
        this.extend = extend;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getCount() {
        return count;
    }

    public void setCount(double count) {
        this.count = count;
    }

    public int getAccount_type() {
        return account_type;
    }

    public void setAccount_type(int account_type) {
        this.account_type = account_type;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Attachment getAttachment() {
        if (!TextUtils.isEmpty(attachment)) {
            mAttachment = KingJson.parseObject(attachment, Attachment.class);
            return mAttachment;
        } else {
            return null;
        }
    }

    public void setAttachment(Attachment attachment) {
        if (null != attachment) {
            this.mAttachment = attachment;
            setAttachment(KingJson.toJSON(attachment).toString());
        }
    }

    public void setAttachment(String attachment) {
        this.attachment = attachment;
    }

    public long getYmd_hms() {
        return ymd_hms;
    }

    public void setYmd_hms(long ymd_hms) {
        this.ymd_hms = ymd_hms;
    }

    public Object copy() {
        KingAccountNode accountBookNode = new KingAccountNode();
        accountBookNode.setId(this.id);
        accountBookNode.setAttachment(KingJson.parseObject(this.attachment, Attachment.class));
        accountBookNode.setAccount_type(this.account_type);
        accountBookNode.setPrice(this.price);
        accountBookNode.setCount(this.count);
        accountBookNode.setYmd_hms(this.ymd_hms);
        accountBookNode.setType(this.type);
        return accountBookNode;
    }

    @Override
    public String toString() {
        return "KingAccountNode{" +
                "id=" + id +
                ", price=" + price +
                ", count=" + count +
                ", account_type=" + account_type +
                ", type=" + type +
                ", attachment='" + attachment + '\'' +
                ", mAttachment=" + mAttachment +
                ", ymd_hms=" + ymd_hms +
                '}';
    }
}
