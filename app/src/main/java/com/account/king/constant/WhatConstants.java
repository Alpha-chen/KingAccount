package com.account.king.constant;

public interface WhatConstants {

    /*Listener*/
    interface Refresh {
        int HOME_FRAGMENT_BOOK = 5001;
        int HOME_FRAGMENT_BUDGET = HOME_FRAGMENT_BOOK + 1;
        int PHOTO_DELETE = HOME_FRAGMENT_BUDGET + 1;
        int LOGO_FINISH = PHOTO_DELETE + 1;
        int QUERY_TYPE_NODES = LOGO_FINISH + 1;
        int QUERY_TYPE_TIMES = QUERY_TYPE_NODES + 1;
        int QUERY_TYPE_INIT = QUERY_TYPE_TIMES + 1;
        int QUERY_TYPE_IE = QUERY_TYPE_INIT + 1;
        int ACCOUNT_INPUT_NOTE = QUERY_TYPE_IE + 1;
    }

    //跳转
    interface What {
        int LOGO_TO_LOCK = 6001;
        int PRE_TO_LOCK = LOGO_TO_LOCK + 1;
    }

    // 跳转到 类别选择
    interface ACCOUNT_TYPE {
        int SELECT_TYPE = 7001;
    }
}

