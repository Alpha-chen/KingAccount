package com.account.king;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.MotionEvent;

import com.account.king.rxevent.RxBus;
import com.account.king.rxevent.RxBusEvent;
import com.account.king.util.ActivityManager;
import com.account.king.util.KeyBoardUtils;
import com.account.king.util.statusbar.StatusBarUtil;

import rx.Subscription;
import rx.functions.Action1;

/**
 * Created by King
 * on 2016/11/21.
 */

public abstract class BaseActivity extends AppCompatActivity implements Action1<RxBusEvent> {

    public String TAG = "BaseActivity";
    public Subscription mSubscription;
    //背景式状态栏
    private static final String imageTranStatusBar = "LogoActivity,InputLockActivity";
    //由子类自定义
    private static final String customTranStatusBar = "CalendarActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResId());
        ActivityManager.getInstance().pushOneActivity(this);
        mSubscription = RxBus.getDefault().toObserverable(RxBusEvent.class).subscribe(this);
        StatusBarUtil.setTransparent(this);
        StatusBarUtil.setColor(this, getResources().getColor(R.color.color_bg), 0);

    }

    /**
     * 返回当前Activity布局文件的id
     *
     * @return
     */
    abstract protected int getLayoutResId();

    @Override
    public void call(RxBusEvent event) {

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        KeyBoardUtils.closeKeyboard(this, getCurrentFocus());
        return super.onTouchEvent(event);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public void finish() {
        super.finish();
        KeyBoardUtils.closeKeyboard(this, getCurrentFocus());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityManager.getInstance().popOneActivity(this);
        if (mSubscription != null && !mSubscription.isUnsubscribed()) {
            mSubscription.unsubscribe();
        }
    }

    /**
     * 初始化界面显示的控件
     */
    public void initView() {
    }

    public void initTitleBar() {

    }

    public void initPresenter() {

    }

    /**
     * 初始化上个activity传过来的参数
     */
    public void initIntent() {
    }

    /**
     * 初始化界面显示的控件中的数据
     */
    public void initViewData() {
    }

    /**
     * /**
     * 初始化请求方法
     */
    public void initRMethod() {

    }

    /**
     * 实例化变量
     */
    public void initData() {
    }


    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
    }


    /**
     * 请求服务器之后更新界面数据
     */
    public void updateViewData() {
    }

}
