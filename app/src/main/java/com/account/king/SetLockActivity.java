package com.account.king;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.TextView;

import com.account.king.rxevent.RxBus;
import com.account.king.rxevent.RxBusEvent;
import com.account.king.util.ActivityLib;
import com.account.king.util.MD5;
import com.account.king.util.SPUtils;
import com.account.king.util.ToastUtil;
import com.account.king.view.Lock9View;


/**
 *
 */
public class SetLockActivity extends BaseActivity implements Lock9View.CallBack, View.OnClickListener {

    private TextView inputHint;

    private String pwd1;
    private boolean isFlag;
    //编辑模式返回不发送设置失败事件
    private boolean isEdit;

    //手势
    private Lock9View lock9View;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initIntent();
        initTitleBar();
        initView();
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_lock_set;
    }

    @Override
    public void initIntent() {
        super.initIntent();
        isEdit = getIntent().getBooleanExtra(ActivityLib.START_TYPE, false);
    }

    @Override
    public void initTitleBar() {
        super.initTitleBar();

    }

    @Override
    public void initView() {
        super.initView();
        inputHint = (TextView) findViewById(R.id.input_hint);
        lock9View = (Lock9View) findViewById(R.id.lock_9_view);
        lock9View.setCallBack(this);
        findViewById(R.id.title_left_image).setOnClickListener(this);

    }

    //继续输入
    private void inputAgain() {
        inputHint.setText(R.string.lock_input_2);
        inputHint.setTextColor(getResources().getColor(R.color.color2));
    }

    //2次输入不一致
    private void inputError() {
        pwd1 = "";
        inputHint.setText(R.string.lock_input_2_error);
        inputHint.setTextColor(getResources().getColor(R.color.color4));
        //摇摆
        TranslateAnimation animation = new TranslateAnimation(-50, 50, 0, 0);
        animation.setDuration(100);
        animation.setRepeatCount(4);
        animation.setRepeatMode(Animation.REVERSE);
        inputHint.startAnimation(animation);
    }

    //至少连接4个点
    private void inputLowPoint() {
        inputHint.setText(R.string.lock_input_low_point);
        inputHint.setTextColor(getResources().getColor(R.color.color2));
    }

    @Override
    public void finish() {
        super.finish();
        if (!isFlag && !isEdit) {
            RxBus.getDefault().send(new RxBusEvent(RxBusEvent.LOCK_FAIL));
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.KEYCODE_BACK) {
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onFinish(String password) {
        if (TextUtils.isEmpty(pwd1)) {
            if (password.length() < 4) {  //首次输入连接点<4
                inputLowPoint();
                return;
            }
            pwd1 = password;
            inputAgain();   //继续输入
            return;
        }
        if (!pwd1.equals(password)) {   //2次密码不一致
            inputError();
            return;
        }
        isFlag = true;
        SPUtils.put(this, SPUtils.LOCK_OPEN_, true);
        SPUtils.put(this, SPUtils.LOCK_, MD5.hexdigest(password));
        ToastUtil.makeToast(this, R.string.lock_save_success);
        RxBus.getDefault().send(new RxBusEvent(RxBusEvent.LOCK_SUCCESS));
        finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_left_image:
                finish();
                break;
            default:
                break;
        }
    }
}
