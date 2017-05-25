package com.account.king;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.account.king.adapter.HomeRecyclerAdapter;
import com.account.king.node.KingAccountNode;
import com.account.king.presenter.contract.SearchContract;
import com.account.king.presenter.contract.presenter.SearchPresenter;
import com.account.king.util.ActivityLib;
import com.account.king.util.ArithUtil;
import com.account.king.util.CalendarUtil;
import com.account.king.util.TypeUtil;

import java.util.ArrayList;

/**
 * @king
 */
public class SearchResultActivity extends BaseActivity implements HomeRecyclerAdapter.OnItemClickListener,
        SearchContract.IView {

    private RecyclerView account_list;
    private HomeRecyclerAdapter mAdapter;
    private SearchPresenter mPresenter;
    private int accoutType = 0;
    private long start = 0;
    private long end = 0;
    private int type = 0;
    private String content;
    private TextView mSearch_result_date;
    private TextView mAccount_type;
    private TextView mSearch_result_type;
    private TextView mSearch_result_total_money;
    private double totalMoney = 0d;

    private LinearLayout search_result_detail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initIntent();
        initView();
        initPresenter();
        initRMethod();
    }

    @Override
    public void initIntent() {
        super.initIntent();
        accoutType = getIntent().getIntExtra(ActivityLib.INTENT_PARAM, 0);
        start = getIntent().getLongExtra(ActivityLib.INTENT_PARAM2, 0);
        end = getIntent().getLongExtra(ActivityLib.INTENT_PARAM3, 0);
        type = getIntent().getIntExtra(ActivityLib.INTENT_PARAM4, 0);
        content = getIntent().getStringExtra(ActivityLib.INTENT_PARAM5);
    }

    @Override
    public void initRMethod() {
        super.initRMethod();
        mPresenter.select(accoutType, start, end, type, content);
    }

    @Override
    public void initPresenter() {
        super.initPresenter();
        mPresenter = new SearchPresenter(this, this);
    }

    @Override
    public void initView() {
        super.initView();
        search_result_detail = (LinearLayout) findViewById(R.id.search_result_detail);
        account_list = (RecyclerView) findViewById(R.id.account_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        account_list.setLayoutManager(layoutManager);
        mAdapter = new HomeRecyclerAdapter(this);
        mAdapter.setClickListener(this);
        account_list.setAdapter(mAdapter);
        mSearch_result_date = (TextView) findViewById(R.id.search_result_date);
        mSearch_result_type = (TextView) findViewById(R.id.search_result_type);
        mSearch_result_total_money = (TextView) findViewById(R.id.search_result_total_money);
        mAccount_type = (TextView) findViewById(R.id.account_type);
        mSearch_result_date.setText(CalendarUtil.getStringMD(CalendarUtil.timeMilis2Date(start)) + "~"
                + CalendarUtil.getStringMD(CalendarUtil.timeMilis2Date(end)));
        if (accoutType == KingAccountNode.MONEY_IN) {
            mAccount_type.setText(getResources().getString(R.string.type_income));
            mSearch_result_total_money.setTextColor(R.color.int_come);
        } else {
            mAccount_type.setText(getResources().getString(R.string.type_cost));
            mSearch_result_total_money.setTextColor(R.color.out_come);
        }
        mSearch_result_type.setText(TypeUtil.getType(this, accoutType, type));
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_search_result;
    }

    @Override
    public void onItemClick(View view, int position) {

    }

    @Override
    public void onLongClick(View view, int position) {

    }

    @Override
    public void selectSucces(ArrayList<KingAccountNode> arrayList) {
        mAdapter.setParams(arrayList);
        for (KingAccountNode node : arrayList) {
            totalMoney = ArithUtil.mul(node.getCount(), node.getPrice(), 2) + totalMoney;
        }
        mSearch_result_total_money.setVisibility(View.VISIBLE);
        mSearch_result_total_money.setText(getString(R.string.search_result_total_money, totalMoney+""));
    }

    @Override
    public void selectFailure() {
        mSearch_result_total_money.setVisibility(View.GONE);

    }

    @Override
    public void setDateText(long date) {

    }
}
