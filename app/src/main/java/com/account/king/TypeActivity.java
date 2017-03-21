package com.account.king;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.account.king.adapter.TypeRecycleAdapter;
import com.account.king.node.KingAccountNode;
import com.account.king.util.ActivityLib;

/**
 * 类别
 */
public class TypeActivity extends BaseActivity {

    private RecyclerView mRecyclerView;

    private TypeRecycleAdapter mAdapter;
    private KingAccountNode mNode;
    private int selectPosition = 0;
    private int accountType = KingAccountNode.MONEY_IN;

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
    }

    @Override
    public void initIntent() {
        super.initIntent();
        mNode = (KingAccountNode) getIntent().getSerializableExtra(ActivityLib.INTENT_PARAM);
        if (null != mNode) {
            selectPosition = mNode.getType();
            accountType = mNode.getAccount_type();
        }
    }

    @Override
    public void initViewData() {
        super.initViewData();
        String[] arrays = null;
        if (accountType == KingAccountNode.MONEY_IN) {
            arrays = getResources().getStringArray(R.array.account_income_type);
        } else {
            arrays = getResources().getStringArray(R.array.account_outcome_type);
        }
        mAdapter = new TypeRecycleAdapter(arrays, TypeActivity.this);
        mAdapter.setSelectPosition(selectPosition);
        mRecyclerView.setAdapter(mAdapter);
    }
}
