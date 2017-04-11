package com.account.king;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.account.king.util.ActivityLib;
import com.account.king.util.LockUtil;
import com.account.king.util.SPUtils;


/**
 * @author king
 *         <p>
 *         通知栏点击预处理界面
 */
public class PreNotificationActivity extends Activity {


    private int type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initIntent();
        //打开了密码锁 并且程序处于后台状态
        if (KingApplication.isLock && (LockUtil.isBackApp(this) || !LockUtil.pwdlocker_open)) {
            SPUtils.remove(this, SPUtils.LOCK_BACK_APP);
            Intent intent = new Intent(this, InputLockActivity.class);
            intent.putExtra(ActivityLib.INTENT_PARAM, true);
            startActivity(intent);
            finish();
        } else {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
    }

    private void initIntent() {
        Intent data = getIntent();
        type = data.getIntExtra(ActivityLib.INTENT_PARAM, 0);
    }


}
