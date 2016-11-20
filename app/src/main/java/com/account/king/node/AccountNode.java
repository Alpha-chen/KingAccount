package com.account.king.node;

import java.io.Serializable;

/**
 * Created by king
 * on 2016/11/20.
 */

public class AccountNode implements Serializable{
    /**
     * id : 0
     * price : 1.8
     * count : 1.5
     * account_type : 0
     * type : 1
     * attachment : {"content":"今天发工资哎","attachment_path":"https://ss0.bdstatic.com/5aV1bjqh_Q23odCf/static/superman/img/logo/logo_white_fe6da1ec.png"}
     * date_ymd : 20161120
     * time_hms : 20:10:45
     */

    private int id;
    public static String ID="id";
    private double price;
    public static String PRICE ="price";
    private double count;
    public static String COUNT ="count";
    /**
     * 收入/支出
     */
    private int account_type;
    public static String ACCOUNT_TYPE ="account_type";
    public static int INCOME =0; // 收入
    public static int OUTCOME =1; // 支出
    /**
     * 类型 薪水/交通
     */
    private int type;
    public static String TYPE ="type";
    /**
     * 附件信息
     */
    private Attachment attachment;
    public static String ATTACHMENT ="attachment";
    /**
     * 记录时间
     */
    private int date_ymd;
    public static String DATE_YMD ="date_ymd";
    /**
     * 记录的详细时间
     */
    private String time_hms;
    public static String TIME_HMS ="time_hms";

    public AccountNode() {
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
        return attachment;
    }

    public void setAttachment(Attachment attachment) {
        this.attachment = attachment;
    }

    public int getDate_ymd() {
        return date_ymd;
    }

    public void setDate_ymd(int date_ymd) {
        this.date_ymd = date_ymd;
    }

    public String getTime_hms() {
        return time_hms;
    }

    public void setTime_hms(String time_hms) {
        this.time_hms = time_hms;
    }
}
