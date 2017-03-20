package com.account.king;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.account.king.constant.WhatConstants;
import com.account.king.node.Attachment;
import com.account.king.node.KingAccountNode;
import com.account.king.util.ActivityLib;
import com.account.king.util.ArithUtil;
import com.account.king.util.CalendarUtil;
import com.account.king.util.glide.GlideUtil;

public class DetailAccountActivity extends BaseActivity implements View.OnClickListener {
    private ImageView mDetail_account_back;
    private TextView mDetail_account_edit;
    private TextView mAccount_date;
    private TextView mAccount_time;
    private TextView mMoney_type;
    private TextView mMoeny;
    private TextView mPrice;
    private TextView mNumber;
    private TextView mTip;
    private ImageView mDetail_account_pic;
    private Button mDetail_delete;
    private KingAccountNode mAccountNode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initView() {
        super.initView();
        mDetail_account_back = (ImageView) findViewById(R.id.detail_account_back);
        mDetail_account_back.setOnClickListener(this);
        mDetail_account_edit = (TextView) findViewById(R.id.detail_account_edit);
        mDetail_account_edit.setOnClickListener(this);
        mAccount_date = (TextView) findViewById(R.id.account_date);
        mAccount_time = (TextView) findViewById(R.id.account_time);
        mMoney_type = (TextView) findViewById(R.id.money_type);
        mMoeny = (TextView) findViewById(R.id.moeny);
        mPrice = (TextView) findViewById(R.id.price);
        mNumber = (TextView) findViewById(R.id.number);
        mTip = (TextView) findViewById(R.id.tip);
        mDetail_account_pic = (ImageView) findViewById(R.id.detail_account_pic);
        mDetail_account_pic.setOnClickListener(this);
        mDetail_delete = (Button) findViewById(R.id.detail_delete);
        mDetail_delete.setOnClickListener(this);
    }

    @Override
    public void initIntent() {
        super.initIntent();
        mAccountNode = (KingAccountNode) getIntent().getSerializableExtra(ActivityLib.INTENT_PARAM);
        if (null != mAccountNode) {
            initViewData();
        }
    }

    @Override
    public void initPresenter() {
        super.initPresenter();
    }

    @Override
    public void initViewData() {
        super.initViewData();
        mAccount_date.setText(CalendarUtil.timeMilis2Date(mAccountNode.getYmd_hms()));
        mAccount_time.setText(CalendarUtil.timeMilis2Time(mAccountNode.getYmd_hms()));
        String[] typeArrays = null;
        if (mAccountNode.getAccount_type() == KingAccountNode.MONEY_OUT) {
            typeArrays = getResources().getStringArray(R.array.account_outcome_type);
        } else {
            typeArrays = getResources().getStringArray(R.array.account_income_type);
        }
        mMoney_type.setText(typeArrays[mAccountNode.getType()]);
        mMoeny.setText(ArithUtil.mul(mAccountNode.getPrice(), mAccountNode.getCount(), 2) + "");
        mPrice.setText(mAccountNode.getPrice() + "");
        mNumber.setText(mAccountNode.getCount() + "");
        Attachment attachment = new Attachment();
        if (null != mAccountNode.getAttachment()) {
            attachment = mAccountNode.getAttachment();
            mTip.setText(attachment.getContent());
            GlideUtil.load(this, attachment.getAttachment_path(), mDetail_account_pic);
        }

    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_detail_account;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.detail_account_back:
                finish();
                break;
            case R.id.detail_account_edit:
                Intent intent = new Intent(DetailAccountActivity.this, AddAccountActivity.class);
                intent.putExtra(ActivityLib.INTENT_PARAM, mAccountNode);
                startActivity(intent);
                break;
            case R.id.detail_account_pic:
                Intent data = new Intent(this, PhotoActivity.class);
                data.putExtra(ActivityLib.INTENT_PARAM, mAccountNode.getAttachment().getAttachment_path());
                data.putExtra(ActivityLib.INTENT_PARAM2, false);
                startActivityForResult(data, WhatConstants.Refresh.PHOTO_DELETE);
                break;
        }
    }
}