package com.account.king;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.account.king.constant.WhatConstants;
import com.account.king.node.KingAccountNode;
import com.account.king.node.TypeNode;
import com.account.king.rxevent.RxBus;
import com.account.king.rxevent.RxBusEvent;
import com.account.king.util.ActivityLib;
import com.account.king.util.KingJson;
import com.account.king.util.SPUtils;

import java.util.ArrayList;

public class AddTypeActivity extends BaseActivity {
    private RelativeLayout mTitle;
    private ImageView mType_add_back;
    private TextView mType_add_save;
    private EditText editText;
    private int accountType;

    private ArrayList<TypeNode> typeNodes = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initIntent();
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_add_type;
    }

    @Override
    public void initIntent() {
        super.initIntent();
        accountType = getIntent().getIntExtra(ActivityLib.INTENT_PARAM, KingAccountNode.MONEY_IN);
        typeNodes = (ArrayList<TypeNode>) getIntent().getSerializableExtra(ActivityLib.INTENT_PARAM2);
    }

    @Override
    public void initView() {
        super.initView();
        mTitle = (RelativeLayout) findViewById(R.id.title);
        mType_add_back = (ImageView) findViewById(R.id.type_add_back);
        mType_add_save = (TextView) findViewById(R.id.type_add_save);
        editText = (EditText) findViewById(R.id.editText);

        mType_add_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mType_add_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newType = editText.getText().toString();
                TypeNode typeNode = new TypeNode();
                typeNode.setId(typeNodes.size());
                typeNode.setName(newType);
                typeNodes.add(typeNode);
                if (KingAccountNode.MONEY_IN == accountType){
                    SPUtils.put(AddTypeActivity.this,SPUtils.MONEY_TYPE_IN, KingJson.toJSON(typeNodes));
                }else {
                    SPUtils.put(AddTypeActivity.this,SPUtils.MONEY_TYPE_OUT, KingJson.toJSON(typeNodes));
                }
                RxBus.getDefault().send(new RxBusEvent(WhatConstants.ACCOUNT_TYPE.ADD_TYPE_SUCCESS,typeNodes));
                finish();
            }
        });
    }
}
