package com.account.king;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.account.king.adapter.HomeRecyclerAdapter;
import com.account.king.node.KingAccountNode;
import com.account.king.presenter.contract.SearchContract;
import com.account.king.presenter.contract.presenter.SearchPresenter;
import com.account.king.util.ActivityLib;

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
        account_list = (RecyclerView) findViewById(R.id.account_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        account_list.setLayoutManager(layoutManager);
        mAdapter = new HomeRecyclerAdapter(this);
        mAdapter.setClickListener(this);
        account_list.setAdapter(mAdapter);
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
    }

    @Override
    public void selectFailure() {

    }

    @Override
    public void setDateText(long date) {

    }
}
