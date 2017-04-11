package com.account.king;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.account.king.util.ActivityLib;
import com.account.king.util.ActivityManager;
import com.account.king.util.LockUtil;
import com.account.king.util.MD5;
import com.account.king.util.SPUtils;
import com.account.king.view.Lock9View;
import com.account.king.view.dialog.ToastDialog;

/**
 * 开启了密码锁之后,进入验证界面
 *
 * @author king
 */
public class InputLockActivity extends BaseActivity implements Lock9View.CallBack, View.OnClickListener {


    private String pwd;
    private TextView lockInput;
    private boolean isStartMain;
    private int inputCount;

    //手势密码
    private Lock9View lock9View;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initView();
        initData();
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_lock_input;
    }

    @Override
    public void initView() {
        super.initView();
        lockInput = (TextView) findViewById(R.id.input_hint);
        lockInput.setOnClickListener(this);
        findViewById(R.id.lock_login).setOnClickListener(this);
        lock9View = (Lock9View) findViewById(R.id.lock_9_view);
        lock9View.setCallBack(this);
    }

    @Override
    public void initData() {
        super.initData();
        pwd = SPUtils.getString(this, SPUtils.LOCK_);
        isStartMain = getIntent().getBooleanExtra(ActivityLib.INTENT_PARAM, false);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            //If false then this only works if the activity is the root
            //of a task; if true it will work for any activity in a task.
            if (isStartMain) {
                moveTaskToBack(false);
            } else {
                moveTaskToBack(true);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.lock_login://重新登录同一个账号后提示密码锁清除
                resetLock2Login(1);
                break;
        }
    }

    private void lockError() {
        ToastDialog dialog = new ToastDialog(this);
        dialog.setHintText(R.string.lock_input_error);
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                resetLock2Login(2);
            }
        });
        dialog.show();
    }

    private void resetLock2Login(int index) {
        Intent data = new Intent(this, MainActivity.class);
        startActivity(data);
        LockUtil.resetLock(this, index);
        KingApplication.restoreData();
        ActivityManager.getInstance().finishAllActivity();
    }

    @Override
    public void onFinish(String psw) {
        if (MD5.hexdigest(psw).equals(pwd)) {
            if (isStartMain) {
                LockUtil.pwdlocker_open = true;
                startActivity(new Intent(this, MainActivity.class));
            }
            finish();
            return;
        }
        inputCount++;
        if (inputCount == 5) {
            lockError();
            return;
        }
        lockInput.setTextColor(getResources().getColor(R.color.color4));
        lockInput.setText(getString(R.string.lock_input_count, 5 - inputCount));
        //摇摆
        TranslateAnimation animation = new TranslateAnimation(-50, 50, 0, 0);
        animation.setDuration(100);
        animation.setRepeatCount(4);
        animation.setRepeatMode(Animation.REVERSE);
        lockInput.startAnimation(animation);
    }
}
