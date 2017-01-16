package com.account.king.rxevent;

/**
 * Created by King
 * on 2016/11/21.
 */

public class RxBusEvent {
    public static final int SYNC = 1000;//sync service success
    public static final int SYNC_SUCCESS = SYNC + 1; //sync client success
    public static final int UPDATE_AVATAR = SYNC_SUCCESS + 1; //更新头像
    //public static final int HOME_UPDATE_BOOK = UPDATE_AVATAR + 1; //添加更新账单
    public static final int UPDATE_BUDGET = UPDATE_AVATAR + 1; //添加更新预算
    public static final int MINE_UPDATE_USERINFO = UPDATE_BUDGET + 1; //用户信息更新
    public static final int LOGIN_SUCCESS = MINE_UPDATE_USERINFO + 1; //登录成功
    public static final int DELETE_BOOK = LOGIN_SUCCESS + 1; //删除账单
    public static final int UPDATE_ACCOUNT = DELETE_BOOK + 1; //更新账单
    public static final int ADD_ACCOUNT = UPDATE_ACCOUNT + 1; //添加账单
    //public static final int PIE_UPDATE_PIENODE = ADD_ACCOUNT + 1; //图表页面编辑账单 非删除
    public static final int KEEP_ACCOUNT_SELECT_TYPE = ADD_ACCOUNT + 1;//记账界面选择类别
    public static final int KEEP_ACCOUNT_REFRESH_TYPE = KEEP_ACCOUNT_SELECT_TYPE + 1;//记账界面类别刷新

    //calendar picker
    public static final int PICKER_PAGE_CHANGE = KEEP_ACCOUNT_REFRESH_TYPE + 1;//日历滑动
    public static final int PICKER_DATE_CLICK = PICKER_PAGE_CHANGE + 1;//日历点击
    /* third login */
    public static final int THIRD_OAUTH_SUCCESS = PICKER_DATE_CLICK + 1;//qq wx sina oauth success
    public static final int BUDGET_DAY_UPDATE = THIRD_OAUTH_SUCCESS + 1;//预算结算日更新

    public static final int GET_USER_INFO = BUDGET_DAY_UPDATE + 1;//获取用户信息
    public static final int SWITCH_ACCOUNT_BOOK = GET_USER_INFO + 1;//切换账本
    public static final int UPDATE_ACCOUNT_BOOK = SWITCH_ACCOUNT_BOOK + 1;//添加或更新账本
    public static final int LOCK_FAIL = UPDATE_ACCOUNT_BOOK + 1;//设置密码锁失败
    public static final int LOCK_SUCCESS = LOCK_FAIL + 1;//设置密码锁成功
    public static final int NEW_IMG =LOCK_SUCCESS+1;//new图标

    public static final int MODIFY_PSW_SUCCESS = NEW_IMG + 1;//修改密码成功

    public static final int VERIFY_PSW_SUCCESS = MODIFY_PSW_SUCCESS + 1;//验证老密码成功


    private int id;
    private Object object;

    public RxBusEvent(int id, Object object) {
        this.id = id;
        this.object = object;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }
}
