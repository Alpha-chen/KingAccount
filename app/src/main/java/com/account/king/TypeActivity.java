package com.account.king;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.account.king.adapter.TypeRecycleAdapter;
import com.account.king.node.KingAccountNode;
import com.account.king.util.ActivityLib;

/**
 * 类别
 */
public class TypeActivity extends BaseActivity implements View.OnClickListener, TypeRecycleAdapter.onRecyclerViewItemClickListener {

    private RecyclerView mRecyclerView;

    private TypeRecycleAdapter mAdapter;
    private KingAccountNode mNode;
    private int selectPosition = 0;
    private int accountType = KingAccountNode.MONEY_IN;
    private ImageView mType_back;
    private ImageView mType_save;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initIntent();
        initViewData();
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_type;
    }

    @Override
    public void initView() {
        super.initView();
        mRecyclerView = (RecyclerView) findViewById(R.id.type_recycle);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        mType_back = (ImageView) findViewById(R.id.type_back);
        mType_save = (ImageView) findViewById(R.id.type_save);
        mType_back.setOnClickListener(this);
        mType_save.setOnClickListener(this);
    }

    @Override
    public void initIntent() {
        super.initIntent();
        mNode = (KingAccountNode) getIntent().getSerializableExtra(ActivityLib.INTENT_PARAM);
    }

    @Override
    public void initViewData() {
        super.initViewData();
        String[] arrays = null;
        if (accountType == KingAccountNode.MONEY_IN) {
            arrays = getResources().getStringArray(R.array.account_income_type);
        } else if (accountType == KingAccountNode.MONEY_OUT) {
            arrays = getResources().getStringArray(R.array.account_outcome_type);
        }
        mAdapter = new TypeRecycleAdapter(arrays, TypeActivity.this);
        mAdapter.setItemClickListener(this);
        selectPosition = mNode.getType();
        mAdapter.setSelectPosition(selectPosition);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.type_back:
                finish();
                break;
            case R.id.type_save:
                Intent intent = new Intent();
                intent.putExtra(ActivityLib.INTENT_PARAM, selectPosition);
                setResult(Activity.RESULT_OK, intent);
                finish();
                break;
            default:
                break;

        }
    }

    @Override
    public void onItemClick(int typePosition) {
        selectPosition = typePosition;
    }
}
