package com.account.king;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.account.king.fragment.AccountFragment;
import com.account.king.fragment.MineFragment;
import com.account.king.presenter.contract.MainContract;
import com.account.king.util.LogUtil;

/**
 * 主界面
 */
public class MainActivity extends BaseActivity implements MainContract.IMainView,View.OnClickListener{
    private FragmentManager fragmentManager;
    private Fragment fragmentArray[] = new Fragment[3];

    private Button mMain_detail;
    private Button mMain_add;
    private Button mMain_mine;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        setTabSelection(0);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_main;
    }

    @Override
    public void initView() {
        super.initView();
        fragmentManager = getSupportFragmentManager();
        mMain_detail = (Button) findViewById(R.id.main_detail);
        mMain_add = (Button) findViewById(R.id.main_add);
        mMain_mine = (Button) findViewById(R.id.main_mine);

        mMain_detail.setOnClickListener(this);
        mMain_add.setOnClickListener(this);
        mMain_mine.setOnClickListener(this);

    }

    /**
     * 根据传入的index参数来设置选中的tab页。
     *
     * @param index 每个tab页对应的下标。0表示消息，1表示联系人，2表示动态，3表示设置。
     */
    @Override
    public void setTabSelection(int index) {
        clearSelection(index);
        setFragment(index);
    }
    /**
     * 清除掉所有的选中状态。
     */
    @Override
    public void clearSelection(int index) {
        for (int i = 0; i < 3; i++) {
//            imgs[i].setImageResource(mImageViewArray[i]);
        }
//        imgs[index].setImageResource(mImagePress[index]);
    }


    private void setFragment(int index) {
        // 开启一个Fragment事务
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        // 先隐藏掉所有的Fragment，以防止有多个Fragment显示在界面上的情况
        for (int i = 0; i < 3; i++) {
            Fragment fragment = fragmentArray[i];
            if (null != fragment) {
                transaction.hide(fragment);
            }
        }
        Fragment fragment = fragmentArray[index];
        if (null == fragment) {
            fragment = getFragment(index);
            fragmentArray[index] = fragment;
            transaction.add(R.id.content, fragment);
        } else {
            transaction.show(fragment);
        }
        transaction.commitAllowingStateLoss();
    }
    private Fragment getFragment(int index) {
        switch (index) {
            case 0:
                return new AccountFragment();
            case 1:
                return new MineFragment();
            default:
                return new AccountFragment();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.main_detail:
                LogUtil.d("MainActivity","0");
                setTabSelection(0);
                break;
            case R.id.main_add:
                setTabSelection(0);
                LogUtil.d("MainActivity","AddAccountActivity");
                startActivity(new Intent(this, AddAccountActivity.class));
                break;
            case R.id.main_mine:
                LogUtil.d("MainActivity","1");
                setTabSelection(1);
                break;
            default:
                break;
        }

    }
}
