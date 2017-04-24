package com.account.king;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.account.king.node.KingAccountNode;
import com.account.king.presenter.contract.SearchContract;
import com.account.king.presenter.contract.presenter.SearchPresenter;
import com.account.king.util.ToastUtil;

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
    private int type = -1;
    private String content;
    private int selectDateStatus = 0; //起始时间

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
        findViewById(R.id.search).setOnClickListener(this);
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
            case R.id.search:
                break;
            case R.id.search_button:
                if (checkInput()) {
                    mPresenter.select(accouType, start, end, type, content);
                }
                break;
            default:
                break;
        }
    }

    private boolean checkInput() {
        if (start > end) {
            ToastUtil.makeToast(this, getString(R.string.search_select_date_tip));
            return false;
        }
        return true;
    }

    @Override
    public void selectSucces(ArrayList<KingAccountNode> arrayList) {

    }

    @Override
    public void selectFailure() {

    }
}
