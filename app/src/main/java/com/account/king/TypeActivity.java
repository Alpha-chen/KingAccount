package com.account.king;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.account.king.adapter.TypeRecycleAdapter;
import com.account.king.constant.WhatConstants;
import com.account.king.node.KingAccountNode;
import com.account.king.node.TypeNode;
import com.account.king.rxevent.RxBusEvent;
import com.account.king.util.ActivityLib;
import com.account.king.util.KingJson;
import com.account.king.util.SPUtils;

import java.util.ArrayList;

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
    private ArrayList<TypeNode> inTypeNodes = new ArrayList<>();
    private ArrayList<TypeNode> outTypeNodes = new ArrayList<>();
    private int type = 0;

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
        mType_save = (ImageView) findViewById(R.id.type_new);
        mType_back.setOnClickListener(this);
        mType_save.setOnClickListener(this);
    }

    @Override
    public void initIntent() {
        super.initIntent();
        mNode = (KingAccountNode) getIntent().getSerializableExtra(ActivityLib.INTENT_PARAM);
        accountType = getIntent().getIntExtra(ActivityLib.INTENT_PARAM2, 0);
        type = getIntent().getIntExtra(ActivityLib.INTENT_PARAM3, 0);
    }

    @Override
    public void initViewData() {
        super.initViewData();
        String[] arrays = null;
        String typeOut = SPUtils.getString(this, SPUtils.MONEY_TYPE_OUT, "");
        String typeIn = SPUtils.getString(this, SPUtils.MONEY_TYPE_IN, "");
        if (accountType == KingAccountNode.MONEY_OUT) {
            if (TextUtils.isEmpty(typeOut)) {
                arrays = getResources().getStringArray(R.array.account_outcome_type);
                for (int i = 0; i < arrays.length; i++) {
                    TypeNode typeNode = new TypeNode();
                    typeNode.setId(i);
                    typeNode.setName(arrays[i]);
                    outTypeNodes.add(typeNode);
                }
                SPUtils.put(this, SPUtils.MONEY_TYPE_OUT, KingJson.toJSON(outTypeNodes));
                mAdapter = new TypeRecycleAdapter(TypeActivity.this);
            } else {
                outTypeNodes = (ArrayList<TypeNode>) KingJson.parseArray(typeOut, TypeNode.class);
                mAdapter = new TypeRecycleAdapter(TypeActivity.this);
            }
            mAdapter.setArrys(outTypeNodes);

        } else {
            if (TextUtils.isEmpty(typeIn)) {
                arrays = getResources().getStringArray(R.array.account_income_type);
                for (int i = 0; i < arrays.length; i++) {
                    TypeNode typeNode = new TypeNode();
                    typeNode.setId(i);
                    typeNode.setName(arrays[i]);
                    inTypeNodes.add(typeNode);
                }
                mAdapter = new TypeRecycleAdapter(TypeActivity.this);
                SPUtils.put(this, SPUtils.MONEY_TYPE_IN, KingJson.toJSON(inTypeNodes));
            } else {
                inTypeNodes = (ArrayList<TypeNode>) KingJson.parseArray(typeIn, TypeNode.class);
                mAdapter = new TypeRecycleAdapter(TypeActivity.this);
            }
            mAdapter.setArrys(inTypeNodes);
        }

        mAdapter.setItemClickListener(this);
        if (null == mNode) {
            selectPosition = type;
        } else {
            selectPosition = mNode.getType();
        }
        mAdapter.setSelectPosition(selectPosition);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.type_back:
                Intent intent = new Intent();
                intent.putExtra(ActivityLib.INTENT_PARAM, selectPosition);
                setResult(Activity.RESULT_OK, intent);
                finish();
                break;
            case R.id.type_new:
                Intent intent1 = new Intent();
                intent1.putExtra(ActivityLib.INTENT_PARAM, accountType);
                if (accountType == KingAccountNode.MONEY_IN) {
                    intent1.putExtra(ActivityLib.INTENT_PARAM2, inTypeNodes);
                } else {
                    intent1.putExtra(ActivityLib.INTENT_PARAM2, outTypeNodes);
                }
                intent1.setClass(this, AddTypeActivity.class);
                startActivity(intent1);
                break;
            default:
                break;

        }
    }

    @Override
    public void call(RxBusEvent event) {
        super.call(event);
        switch (event.getId()) {
            case WhatConstants.ACCOUNT_TYPE.ADD_TYPE_SUCCESS:
                if (KingAccountNode.MONEY_IN == accountType) {
                    inTypeNodes = (ArrayList<TypeNode>) event.getObject();
                    mAdapter.setArrys(inTypeNodes);
                } else {
                    outTypeNodes = (ArrayList<TypeNode>) event.getObject();
                    mAdapter.setArrys(outTypeNodes);
                }
                mAdapter.notifyDataSetChanged();
                break;
            default:
                break;
        }
    }

    @Override
    public void onItemClick(int typePosition) {
        selectPosition = typePosition;
    }

    @Override
    public void onItemLongClick(int typePosition) {
        selectPosition = 0;
        if (KingAccountNode.MONEY_IN == accountType) {
            SPUtils.put(this, SPUtils.MONEY_TYPE_IN, KingJson.toJSON(inTypeNodes));
        } else {
            SPUtils.put(this, SPUtils.MONEY_TYPE_OUT, KingJson.toJSON(outTypeNodes));
        }
    }
}
