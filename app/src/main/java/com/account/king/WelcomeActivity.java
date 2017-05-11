package com.account.king;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.account.king.constant.WhatConstants;
import com.account.king.presenter.contract.WelcomeContract;
import com.account.king.presenter.contract.WelcomePresenter;
import com.account.king.util.ActivityLib;
import com.account.king.util.AppUtils;
import com.account.king.util.SPUtils;

/**
 * @King
 */
public class WelcomeActivity extends BaseActivity implements Handler.Callback, WelcomeContract.IView {

    private Handler handler;
    private WelcomePresenter presenter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initPresenter();
        handler = new Handler(this);
        handler.sendEmptyMessageDelayed(WhatConstants.Refresh.LOGO_FINISH, 1200);
        initRMethod();
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_logo;
    }


    @Override
    public void initPresenter() {
        super.initPresenter();
        presenter = new WelcomePresenter(this, this);
    }

    @Override
    public boolean handleMessage(Message msg) {
        switch (msg.what) {
            case WhatConstants.Refresh.LOGO_FINISH:
                boolean isFirst = SPUtils.getBoolean(this, AppUtils.getVersionName(this));
                if (!isFirst) {
                    presenter.showGuide(handler);
                } else {
                    startMainScreen();
                }
                break;
            case WhatConstants.What.UPGRADE_VERSION_START:
                startMainScreen();
                break;
        }
        return false;
    }

    @Override
    public void startMainScreen() {
        Intent data = null;
        if (KingApplication.isLock) {
            data = new Intent(this, InputLockActivity.class);
            data.putExtra(ActivityLib.INTENT_PARAM, true);//是否启动main页面
        } else {
            data = new Intent(this, MainActivity.class);
        }
        data.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(data);
        finish();
    }
}
