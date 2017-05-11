package com.account.king.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.account.king.DetailAccountActivity;
import com.account.king.R;
import com.account.king.adapter.HomeRecyclerAdapter;
import com.account.king.node.KingAccountNode;
import com.account.king.presenter.contract.AccountContract;
import com.account.king.presenter.contract.presenter.AccountPresenter;
import com.account.king.rxevent.RxBusEvent;
import com.account.king.util.ActivityLib;
import com.account.king.util.CalendarUtil;
import com.account.king.util.LogUtil;
import com.account.king.util.ToastUtil;

import java.util.ArrayList;

/***
 * @author King
 */
public class AccountFragment extends BaseFragment implements View.OnClickListener, AccountContract.IView, HomeRecyclerAdapter.OnItemClickListener {

    private View root;
    private AccountPresenter accountPresenter;
    private Context mContext;

    private RecyclerView mRecyclerView;

    private HomeRecyclerAdapter mAdapter;

    private SparseBooleanArray mBooleanArray = new SparseBooleanArray();

    private TextView home_month_income;
    private TextView home_money_cost;
    ArrayList<KingAccountNode> mAccountNodes = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (null == root) {
            root = inflater.inflate(R.layout.fragment_home, container, false);
            initView();
            initPresenter();
            initRMethod();
            updateViewData();
        }
        ViewGroup parent = (ViewGroup) root.getParent();
        if (parent != null) {
            parent.removeView(root);
        }
        return root;
    }

    @Override
    public void initView() {
        super.initView();
        mRecyclerView = (RecyclerView) root.findViewById(R.id.account_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        mRecyclerView.setLayoutManager(layoutManager);
        mAdapter = new HomeRecyclerAdapter(mContext);
        mAdapter.setClickListener(this);
        mRecyclerView.setAdapter(mAdapter);
        home_month_income = (TextView) root.findViewById(R.id.home_month_income);
        home_money_cost = (TextView) root.findViewById(R.id.home_money_cost);

    }

    @Override
    public void initPresenter() {
        super.initPresenter();
        accountPresenter = new AccountPresenter(mActivity, this);
    }


    @Override
    public void initRMethod() {
        super.initRMethod();
        accountPresenter.selectAllAccount();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = context;
    }


    @Override
    public void onClick(View v) {

    }

    @Override
    public void selectAccountSuccess(ArrayList<KingAccountNode> accountNodes) {
//        bookNodes.addAll(nodes);
        mBooleanArray.clear();
        this.mAccountNodes = accountNodes;
        handBookNodes(mContext, accountNodes);
        mAdapter.setParams(accountNodes, mBooleanArray);
    }


    /**
     * 处理List数据
     *
     * @param mList
     */
    public void handBookNodes(Context context, ArrayList<KingAccountNode> mList) {

        int newYear = 0;
        int newMonth = 0;
        int newDay = 0;
        for (int i = 0; i < mList.size(); i++) {
            KingAccountNode vo = mList.get(i);
            long date = vo.getYmd_hms();
            int date1 = CalendarUtil.timeMilis2Date(date);
            int year = CalendarUtil.getYear(date1);
            int month = CalendarUtil.getMonth(date1);
            int day = CalendarUtil.getDay(date1);
            //分隔每天数据
            if (newYear != year || newMonth != month || newDay != day) {
                mBooleanArray.put(i, true);
                newYear = year;
                newMonth = month;
                newDay = day;
            }
        }

    }

    @Override
    public void seletcAccountFailure() {
        ToastUtil.makeToast(mContext, "还没有记账哦~");
    }

    @Override
    public void showIncomeOutcome(float inCome, float outCome) {
        home_month_income.setText(activity.getResources().getString(R.string.show_money, inCome + ""));
        home_money_cost.setText(activity.getResources().getString(R.string.show_money, outCome + ""));
    }

    @Override
    public void call(RxBusEvent rxBusEvent) {
        super.call(rxBusEvent);
        switch (rxBusEvent.getId()) {
            case RxBusEvent.REFRESH_ACCOUNT_LIST:
                initRMethod();
                break;
            default:
                break;
        }

    }

    @Override
    public void onItemClick(View view, int position) {
        // // TODO: 2017/3/14 进行界面跳转到详情界面
        LogUtil.d(TAG, "onItemClick->position=" + position);
        Intent intent = new Intent(activity, DetailAccountActivity.class);
        intent.putExtra(ActivityLib.INTENT_PARAM, mAccountNodes.get(position));
        activity.startActivity(intent);
    }

    @Override
    public void onLongClick(View view, int position) {

    }
}
