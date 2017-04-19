package com.account.king;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.account.king.util.ActivityLib;
import com.account.king.util.ActivityManager;
import com.account.king.util.LockUtil;
import com.account.king.util.SPUtils;
import com.account.king.util.ToastUtil;

/**
 * 清楚密码锁界面
 *
 * @author king
 */
public class LockPasswordActivity extends BaseActivity implements View.OnClickListener {


    private EditText mPassword;

    /**
     * 0 表示 设置界面
     * 1 表示清除
     */
    private int type = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initIntent();
    }

    @Override
    public void initIntent() {
        super.initIntent();
        type = getIntent().getExtras().getInt(ActivityLib.INTENT_PARAM, 0);
    }

    @Override
    public void initView() {
        super.initView();
        findViewById(R.id.title_left_image).setOnClickListener(this);
        mPassword = (EditText) findViewById(R.id.password);
        findViewById(R.id.email_sign_in_button).setOnClickListener(this);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_lock_password;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_left_image:
                finish();
                break;
            case R.id.email_sign_in_button:
                String s = mPassword.getText().toString();
                if (TextUtils.isEmpty(s)) {
                    ToastUtil.makeToast(this, "请输入清除密码");
                } else {
                    if (type == 1) {
                        String oldStr = SPUtils.getString(this, SPUtils.REMIND_LOCK_PASSWORD, "");
                        if (TextUtils.equals(s, oldStr)) {
                            Intent data = new Intent(this, MainActivity.class);
                            startActivity(data);
                            LockUtil.resetLock(this, 1);
                            KingApplication.mApplication.restoreData();
                            ActivityManager.getInstance().finishAllActivity();
                        } else {
                            ToastUtil.makeToast(this, "请在想想");
                        }
                    } else {
                        SPUtils.put(this, SPUtils.REMIND_LOCK_PASSWORD, s);
                        finish();
                    }
                }
                break;
            default:
                break;
        }
    }
}

