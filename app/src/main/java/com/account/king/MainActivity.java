package com.account.king;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.account.king.fragment.HomeFragment;
import com.account.king.fragment.MineFragment;
import com.account.king.presenter.contract.MainContract;

public class MainActivity extends BaseActivity implements MainContract.IMainView{
    private FragmentManager fragmentManager;
    private Fragment fragmentArray[] = new Fragment[3];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTabSelection(0);

    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_main;
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
                return new HomeFragment();
            case 2:
                return new MineFragment();
            default:
                return new HomeFragment();
        }
    }
}
