package com.account.king.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.account.king.AboutActivity;
import com.account.king.R;
import com.account.king.SetAccountActivity;

/**
 * 我的界面
 */
public class MineFragment extends BaseFragment implements View.OnClickListener {
    private View root;
    private Context context;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (null == root) {
            root = inflater.inflate(R.layout.fragment_mine, container, false);
            initView();
            initPresenter();
            updateViewData();
        }
        ViewGroup parent = (ViewGroup) root.getParent();
        if (parent != null) {
            parent.removeView(root);
        }
        return root;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void initView() {
        super.initView();
        root.findViewById(R.id.mine_setting).setOnClickListener(this);
        root.findViewById(R.id.about).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.mine_setting:
                Intent intent = new Intent(activity, SetAccountActivity.class);
                activity.startActivity(intent);
                break;
            case R.id.about:
                Intent intent1 = new Intent(activity, AboutActivity.class);
                activity.startActivity(intent1);
                break;
            default:
                break;
        }
    }
}
