package com.account.king.callback;

import com.amap.api.location.AMapLocation;

/**
 * 定位请求 回调函数
 * Created by king
 * on 2015/12/8.
 */
public interface AMapLocationCallBack {
    void onLocationSuccess(AMapLocation aMapLocation); // 定位成功
    void onLocationFailed(int errorCode, String errorMessage); // 定位失败
//    public void UnCaughtException(int myErrorCode); // "1"表示返回的AMapLocation为空

}
