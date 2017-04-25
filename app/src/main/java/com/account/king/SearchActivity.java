package com.account.king;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.account.king.constant.WhatConstants;
import com.account.king.node.KingAccountNode;
import com.account.king.presenter.contract.SearchContract;
import com.account.king.presenter.contract.presenter.SearchPresenter;
import com.account.king.util.ActivityLib;
import com.account.king.util.CalendarUtil;
import com.account.king.util.LogUtil;
import com.account.king.util.ToastUtil;
import com.account.king.util.TypeUtil;

import java.util.ArrayList;

/**
 * 查询
 *
 * @king
 */
public class SearchActivity extends BaseActivity implements View.OnClickListener, SearchContract.IView {

    private TextView mAdd_type_income_btn;
    private TextView mAdd_type_cost_btn;
    private TextView mSearch_select_start_date;
    private TextView mSearch_select_end_date;
    private TextView mSearch_select_type;
    private EditText mSearch_content;

    private SearchPresenter mPresenter;

    private int accouType = KingAccountNode.MONEY_OUT;
    private int start = 0;
    private int end = 0;
    private int type = 0;
    private String content;
    private int selectDateStatus = 0; //起始时间

    private long selectStartDate = 0;
    private long selectEndDate = 0;

    private int selectDateType = 0; //  0 起始时间 1 截止时间

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initPresenter();
    }

    @Override
    public void initPresenter() {
        super.initPresenter();
        mPresenter = new SearchPresenter(this, this);
    }

    @Override
    public void initView() {
        super.initView();
        findViewById(R.id.title_left_image).setOnClickListener(this);
        mAdd_type_income_btn = (TextView) findViewById(R.id.add_type_income_btn);
        mAdd_type_income_btn.setOnClickListener(this);
        mAdd_type_cost_btn = (TextView) findViewById(R.id.add_type_cost_btn);
        mAdd_type_cost_btn.setOnClickListener(this);
        findViewById(R.id.search_start_date).setOnClickListener(this);
        mSearch_select_start_date = (TextView) findViewById(R.id.search_select_start_date);
        findViewById(R.id.search_end_date).setOnClickListener(this);
        mSearch_select_end_date = (TextView) findViewById(R.id.search_select_end_date);
        findViewById(R.id.search_type_lay).setOnClickListener(this);
        mSearch_select_type = (TextView) findViewById(R.id.search_select_type);
        findViewById(R.id.search_content_lay).setOnClickListener(this);
        mSearch_content = (EditText) findViewById(R.id.search_content);
        findViewById(R.id.search_button).setOnClickListener(this);

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

            case R.id.search_button:
                if (checkInput()) {
                    Intent intent = new Intent();
                    intent.setClass(SearchActivity.this, SearchResultActivity.class);
                    intent.putExtra(ActivityLib.INTENT_PARAM, accouType);
                    intent.putExtra(ActivityLib.INTENT_PARAM2, selectStartDate);
                    intent.putExtra(ActivityLib.INTENT_PARAM3, selectEndDate);
                    intent.putExtra(ActivityLib.INTENT_PARAM4, type);
                    intent.putExtra(ActivityLib.INTENT_PARAM5, content);
                    startActivity(intent);
                }
                break;
            case R.id.add_type_income_btn: //　收入
                accouType = KingAccountNode.MONEY_IN;
                selectIncomeType();
                break;
            case R.id.add_type_cost_btn: //　收入
                accouType = KingAccountNode.MONEY_OUT;
                selectIncomeType();
                break;
            case R.id.search_type_lay:
                Intent intent = new Intent(SearchActivity.this, TypeActivity.class);
                intent.putExtra(ActivityLib.INTENT_PARAM2, accouType);
                intent.putExtra(ActivityLib.INTENT_PARAM3, type);
                startActivityForResult(intent, WhatConstants.ACCOUNT_TYPE.SELECT_TYPE);
                break;
            case R.id.search_start_date: //  开始时间
                selectDateType = 0;
                mPresenter.selectDate(this, selectStartDate);
                break;
            case R.id.search_end_date: // 结束时间
                selectDateType = 1;
                mPresenter.selectDate(this, selectEndDate);
                break;
            default:
                break;
        }
    }

    public void selectIncomeType() {
        LogUtil.d(TAG, "type=" + type);
        if (accouType == KingAccountNode.MONEY_IN) {
            mAdd_type_income_btn.setBackgroundResource(R.drawable.search_type_left_bg_press);
            mAdd_type_income_btn.setTextColor(getResources().getColor(R.color.white));
            mAdd_type_cost_btn.setBackgroundResource(R.drawable.search_type_bg);
            mAdd_type_cost_btn.setTextColor(getResources().getColor(R.color.my_color));
        } else if (accouType == KingAccountNode.MONEY_OUT) {
            mAdd_type_cost_btn.setBackgroundResource(R.drawable.search_type_bg_press);
            mAdd_type_cost_btn.setTextColor(getResources().getColor(R.color.white));
            mAdd_type_income_btn.setBackgroundResource(R.drawable.search_type_left_bg);
            mAdd_type_income_btn.setTextColor(getResources().getColor(R.color.my_color));
        }
        mSearch_select_type.setVisibility(View.VISIBLE);
        mSearch_select_type.setText(TypeUtil.getType(this, accouType, type));
    }

    private boolean checkInput() {
        if (selectStartDate > selectEndDate || (selectEndDate == 0 || selectStartDate == 0)) {
            ToastUtil.makeToast(this, getString(R.string.search_select_date_tip));
            return false;
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case WhatConstants.ACCOUNT_TYPE.SELECT_TYPE:
                    if (data != null) {
                        int typeCount = data.getIntExtra(ActivityLib.INTENT_PARAM, 0);
                        type = typeCount;
                        mSearch_select_type.setVisibility(View.VISIBLE);
                        mSearch_select_type.setText(TypeUtil.getType(this, accouType, type));
                    }
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public void selectSucces(ArrayList<KingAccountNode> arrayList) {
        ToastUtil.makeToast(this, arrayList.size() + "");
    }

    @Override
    public void selectFailure() {
        ToastUtil.makeToast(this, "selectFailure");

    }

    @Override
    public void setDateText(long date) {
        if (selectDateType == 0) {
            selectStartDate = date;
            mSearch_select_start_date.setVisibility(View.VISIBLE);
            mSearch_select_start_date.setText(CalendarUtil.getStringMD(CalendarUtil.timeMilis2Date(date)));
        } else {
            selectEndDate = date;
            mSearch_select_end_date.setVisibility(View.VISIBLE);
            mSearch_select_end_date.setText(CalendarUtil.getStringMD(CalendarUtil.timeMilis2Date(date)));
        }
    }
}
