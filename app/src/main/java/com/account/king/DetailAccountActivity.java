package com.account.king;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.account.king.constant.WhatConstants;
import com.account.king.node.Attachment;
import com.account.king.node.KingAccountNode;
import com.account.king.presenter.contract.DetailAccountContract;
import com.account.king.presenter.contract.presenter.DetailAccountPresenter;
import com.account.king.rxevent.RxBus;
import com.account.king.rxevent.RxBusEvent;
import com.account.king.util.ActivityLib;
import com.account.king.util.ArithUtil;
import com.account.king.util.CalendarUtil;
import com.account.king.util.LogUtil;
import com.account.king.util.ToastUtil;
import com.account.king.util.TypeUtil;
import com.account.king.util.glide.GlideUtil;

import static com.account.king.rxevent.RxBusEvent.REFRESH_ACCOUNT_LIST;

public class DetailAccountActivity extends BaseActivity implements View.OnClickListener, DetailAccountContract.IView {
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
    private TextView mDetail_delete;
    private KingAccountNode mAccountNode;
    private DetailAccountPresenter mPresenter;

    private LinearLayout location_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initIntent();
        initPresenter();
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
        mDetail_delete = (TextView) findViewById(R.id.detail_delete);
        mDetail_delete.setOnClickListener(this);
        location_view = (LinearLayout) findViewById(R.id.location_view);

    }

    @Override
    public void initIntent() {
        super.initIntent();
        mAccountNode = (KingAccountNode) getIntent().getSerializableExtra(ActivityLib.INTENT_PARAM);
        if (null != mAccountNode) {
            initViewData();
            LogUtil.d(TAG, "mAccountNode->=" + mAccountNode.toString());
        }
    }

    @Override
    public void initPresenter() {
        super.initPresenter();
        mPresenter = new DetailAccountPresenter(this, DetailAccountActivity.this);
    }

    @Override
    public void initViewData() {
        super.initViewData();
        mAccount_date.setText(CalendarUtil.getStringYMD(CalendarUtil.timeMilis2Date(mAccountNode.getYmd_hms())) + "");
        mAccount_time.setText(CalendarUtil.timeMilis2Time(mAccountNode.getYmd_hms()) + "");

        mMoney_type.setText(TypeUtil.getType(this,mAccountNode.getAccount_type(),mAccountNode.getType()));
        mMoeny.setText(ArithUtil.mul(mAccountNode.getPrice(), mAccountNode.getCount(), 2) + "");
        mPrice.setText(mAccountNode.getPrice() + "");
        mNumber.setText(mAccountNode.getCount() + "");
        Attachment attachment = new Attachment();
        if (null != mAccountNode.getAttachment()) {
            attachment = mAccountNode.getAttachment();
            mTip.setText(attachment.getContent());
            if (!TextUtils.isEmpty(attachment.getAttachment_path())) {
                mDetail_account_pic.setVisibility(View.VISIBLE);
                GlideUtil.loadCenterCrop(this, attachment.getAttachment_path(), mDetail_account_pic);
            } else {
                mDetail_account_pic.setVisibility(View.GONE);
            }

            if (TextUtils.isEmpty(attachment.getLocation())) {
                location_view.setVisibility(View.GONE);
            } else {
                location_view.setVisibility(View.VISIBLE);
                TextView add_account_location_detail = (TextView) findViewById(R.id.add_account_location_detail);
                add_account_location_detail.setText(attachment.getLocation());
            }
        } else {
            mDetail_account_pic.setVisibility(View.GONE);
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
                LogUtil.d(TAG, "mAccountNode->=" + mAccountNode.toString());
                startActivity(intent);
                finish();
                break;
            case R.id.detail_account_pic:
                Intent data = new Intent(this, PhotoActivity.class);
                data.putExtra(ActivityLib.INTENT_PARAM, mAccountNode.getAttachment().getAttachment_path());
                data.putExtra(ActivityLib.INTENT_PARAM2, false);
                startActivityForResult(data, WhatConstants.Refresh.PHOTO_DELETE);
                break;
            case R.id.detail_delete:
                if (null != mAccountNode) {
                    mPresenter.delete(mAccountNode);
                } else {
                    ToastUtil.makeToast(DetailAccountActivity.this, getResources().getString(R.string.delete_tip));
                }
                break;
        }
    }

    @Override
    public void deleteSuccess() {
        RxBus.getDefault().send(new RxBusEvent(REFRESH_ACCOUNT_LIST));
        finish();
    }
}
