package com.account.king;

import android.os.Bundle;
import android.view.View;

public class SearchActivity extends BaseActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }


    @Override
    public void initView() {
        super.initView();
        findViewById(R.id.title_left_image).setOnClickListener(this);
        findViewById(R.id.search).setOnClickListener(this);

    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_search;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_left_image:
                finish();
                break;
            case R.id.search:
                break;
            default:
                break;
        }
    }
}
