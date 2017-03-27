package com.account.king.manager;

import android.content.Context;

import com.account.king.callback.AMapLocationCallBack;
import com.account.king.util.LogUtil;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;


/**
 * 对定位进行管理，
 * <p>
 * Created by king
 * on 2015/12/8.
 */
public class AMapLocationManager implements AMapLocationListener {
    private Context context;
    private AMapLocationClient aMapLocationClient;
    private AMapLocationClientOption aMapLocationClientOption;
    private AMapLocationCallBack callBack;

    // 防止用户在进行一次定位未完成时候频繁的点击
    private boolean isFastClick = true;

    private boolean isLocation = false;

    /**
     * @param context
     */
    public AMapLocationManager(Context context) {
        this.context = context;
    }

    public AMapLocationManager(Context context, AMapLocationClientOption option) {
        this.context = context;
        aMapLocationClientOption = option;
    }


    public void setAMapLocationCallBack(AMapLocationCallBack callBackListener) {
        this.callBack = callBackListener;

    }

    private AMapLocationClientOption getStandardOption() {
        aMapLocationClientOption = new AMapLocationClientOption();
        aMapLocationClientOption.setOnceLocation(true);
        aMapLocationClientOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        aMapLocationClientOption.setInterval(2000); // 网络请求间隔
        aMapLocationClientOption.setHttpTimeOut(2000 * 3); // 定位请求超时
        aMapLocationClientOption.setMockEnable(false);  // 不允许模拟地理位置
//        aMapLocationClientOption.setMockEnable(true);  // 允许模拟地理位置
        return aMapLocationClientOption;
    }

    /**
     * 创建AMapLocationClient 并初始化
     *
     * @param option 定位的一些选项
     * @return
     */
    private void getInstance(AMapLocationClientOption option) {
        if (null == aMapLocationClient) {
            aMapLocationClient = new AMapLocationClient(context);
        }
        if (null == option) {
            aMapLocationClientOption = getStandardOption();
        }
        aMapLocationClient.setLocationOption(aMapLocationClientOption);
        aMapLocationClient.setLocationListener(this);
    }

    /**
     * 定位开始
     */
    public void startOnceLocation() {
        if (null == callBack) {
            throw new NullPointerException("AMapLocationManager中AMapLocationCallBack为空");
        }
        if (isLocation) {
            return;
        }
        getInstance(aMapLocationClientOption);
        aMapLocationClient.startLocation();
        isFastClick = false;
        isLocation = true;
    }

    private void stopLocation() {
        aMapLocationClient.stopLocation();
        aMapLocationClient.unRegisterLocationListener(this);
        aMapLocationClient.onDestroy();
        callBack = null;
        isLocation = false;
    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {

        if (null != aMapLocation) {
            int tempCode = aMapLocation.getErrorCode();
            if (tempCode == 0) {
                LogUtil.d("AMapLocation=" + aMapLocation.getLocationDetail());
                callBack.onLocationSuccess(aMapLocation);
                stopLocation();
            } else {
                callBack.onLocationFailed(tempCode, aMapLocation.getErrorInfo());
                stopLocation();
            }
        } else {
            LogUtil.d("AMapLocationManager", "aMapLocation为空");
            stopLocation();
        }

    }
}
