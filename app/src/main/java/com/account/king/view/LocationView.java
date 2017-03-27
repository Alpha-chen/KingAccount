package com.account.king.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.account.king.R;
import com.account.king.callback.AMapLocationCallBack;
import com.account.king.constant.WhatConstants;
import com.account.king.manager.AMapLocationManager;
import com.account.king.node.GeoNode;
import com.account.king.util.ActivityLib;
import com.account.king.util.LogUtil;
import com.amap.api.location.AMapLocation;

/**
 * 定位控件
 * 默认是打开，
 * defaultOpen 不设置 默认为true
 */
public class LocationView extends LinearLayout implements OnClickListener, AMapLocationCallBack {
    private static String TAG = "LocationView";
    private Context context;
    private boolean isRequest = false; //
    private ImageView locationImg;
    private TextView locationInfo;// 定位;
    // 定位
    private AMapLocationManager aMapLocationManager;
    private ProgressBar progressBar;
    private boolean defaultOpen = true; // 默认打开定位
    private Handler handler;
    private WeatherCallBack callBack;
    private boolean isNew;
    private GeoNode geo;
    private int weather;
    private int type = 0;

    public LocationView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;

        initView(context, attrs);
    }

    private void initData() {
        if (!isNew) {
            if (null == geo || ActivityLib.isEmpty(geo.getProvince())) {
                progressBar.setVisibility(View.GONE);
                locationImg.setVisibility(View.VISIBLE);
                locationImg.setImageResource(R.mipmap.sns_location_before);
                locationInfo.setText(context.getResources().getString(R.string.sns_txt_location));
                locationInfo.setTextColor(getResources().getColor(R.color.transparent_black_20));
                isRequest = true;
                return;
            }
            String key = weather + "";
            String weather = "";
            if (type == 0) {
                weather = "";
            }
            if (geo.getProvince().equals(geo.getCity())) {
                locationInfo.setText(geo.getProvince() + " " + weather);
            } else {
                locationInfo.setText(geo.getProvince() + " " + geo.getCity() + " " + weather);
            }
            progressBar.setVisibility(View.GONE);
            locationImg.setVisibility(View.VISIBLE);
            locationImg.setImageResource(R.mipmap.sns_location_done);
            locationInfo.setTextColor(getResources().getColor(R.color.sns_location_display));
            isRequest = false;
            return;
        }
        if (defaultOpen) {
            aMapLocationManager = new AMapLocationManager(context);
            aMapLocationManager.setAMapLocationCallBack(this);
            aMapLocationManager.startOnceLocation();
            isRequest = true;
        }
    }

    public void setWeatherCallBack(WeatherCallBack callBack) {
        this.callBack = callBack;
    }

    /**
     * 设置是否是新写的日记，如果不是新的，则此控件不需要重新去请求
     */
    public void setIsNew(boolean isNew, GeoNode geo, int weather) {
        this.isNew = isNew;
        this.geo = geo;
        this.weather = weather;
    }

    public void setType(int type) {
        this.type = type;
    }

    /**
     * 传入调用view的class中的handler
     *
     * @param handler 发送handler
     */
    public void setHandler(Handler handler) {
        this.handler = handler;
        initData();
    }

    private void initView(Context context, AttributeSet attrs) {
        if (null != attrs) {
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.LocationView);
            defaultOpen = typedArray.getBoolean(R.styleable.LocationView_defaultOpen, true);
            typedArray.recycle();
        }
        LayoutInflater layoutInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View locationView = layoutInflater.inflate(R.layout.location_tag, this);
        RelativeLayout locationLay = (RelativeLayout) locationView.findViewById(R.id.location_layout);
        locationImg = (ImageView) locationView.findViewById(R.id.sns_location_tag_img);
        locationInfo = (TextView) locationView.findViewById(R.id.sns_location_tag_info);
        locationLay.setOnClickListener(this);
        locationImg.setOnClickListener(this);
        locationInfo.setOnClickListener(this);
        progressBar = (ProgressBar) locationView.findViewById(R.id.sns_location_progress);
        progressBar.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sns_location_tag_img:
            case R.id.sns_location_tag_info:
            case R.id.location_layout:
                if (!isRequest) {
                    locationNormal(); // 正常情况下，点击添加地理位置
                    isRequest = true;
                } else {
                    aMapLocationManager = new AMapLocationManager(context);
                    aMapLocationManager.setAMapLocationCallBack(this);
                    aMapLocationManager.startOnceLocation();
                    locationProgress();
                }
                break;
            default:
                break;
        }

    }

    /**
     * 显示点击添加地理位置
     */
    private void locationNormal() {
        progressBar.setVisibility(View.GONE);
        locationImg.setVisibility(View.VISIBLE);
        locationImg.setImageResource(R.mipmap.sns_location_before);
        locationInfo.setText(context.getResources().getString(R.string.sns_txt_location));
        locationInfo.setTextColor(getResources().getColor(R.color.transparent_black_20));
        Message message = handler.obtainMessage();
        handler.sendMessage(message);
    }

    /**
     * 定位中
     */
    private void locationProgress() {
        progressBar.setVisibility(View.VISIBLE);
        locationImg.setVisibility(View.GONE);
        locationInfo.setText(context.getResources().getString(R.string.sns_location_loading));
        locationInfo.setTextColor(getResources().getColor(R.color.transparent_black_20));
    }


    /**
     * 定位成功
     *
     * @param aMapLocation 收到的定位信息
     */
    private void locationSuccess(final AMapLocation aMapLocation) {

        progressBar.setVisibility(View.GONE);
        locationImg.setVisibility(View.VISIBLE);
        locationImg.setImageResource(R.mipmap.sns_location_error);
        locationInfo.setTextColor(getResources().getColor(R.color.sns_location_display));
    }

    /**
     * 定位失败
     */
    private void locationFailed() {
        progressBar.setVisibility(View.GONE);
        locationImg.setVisibility(View.VISIBLE);
        locationImg.setImageResource(R.mipmap.sns_location_error);
        locationInfo.setTextColor(getResources().getColor(R.color.transparent_black_20));
        locationInfo.setText(getResources().getString(R.string.sns_location_failed));

    }

    @Override
    public void onLocationSuccess(AMapLocation aMapLocation) {
        isRequest = false;
        locationSuccess(aMapLocation);
        Message message = handler.obtainMessage();
        message.obj = aMapLocation;
        handler.sendMessage(message);
    }

    @Override
    public void onLocationFailed(int errorCode, String errorMessage) {
        isRequest = false;
        locationFailed();
        Message message = handler.obtainMessage();
        Bundle bundle = new Bundle();
        bundle.putString("errorMessage", errorMessage);
        bundle.putInt("errorCode", errorCode);
        message.setData(bundle);
        handler.sendMessage(message);
    }

    public interface WeatherCallBack {
        void weatherCallback(String weather);
    }
}
