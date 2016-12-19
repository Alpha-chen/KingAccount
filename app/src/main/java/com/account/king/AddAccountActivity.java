package com.account.king;

import android.os.Bundle;

public class AddAccountActivity extends BaseActivity {
    //关闭动画部分机型在style中设置无效
    protected int activityCloseEnterAnimation;
    protected int activityCloseExitAnimation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_add_account;
    }

    @Override
    public void initView() {
        super.initView();
        activityCloseEnterAnimation = android.R.anim.fade_in;
        activityCloseExitAnimation = R.anim.activity_push_bottom_out;
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(activityCloseEnterAnimation, activityCloseExitAnimation);
    }
}
