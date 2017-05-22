package com.account.king;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.account.king.adapter.HomeRecyclerAdapter;
import com.account.king.db.storage.KingAccountStorage;
import com.account.king.node.KingAccountNode;
import com.account.king.rxevent.RxBusEvent;
import com.account.king.util.ActivityLib;
import com.account.king.util.CalendarUtil;
import com.account.king.util.LogUtil;
import com.account.king.util.PermissionUtil;
import com.account.king.view.dialog.CalendarDialog;

import java.util.ArrayList;

public class StaticsActivity extends BaseActivity implements View.OnClickListener, HomeRecyclerAdapter.OnItemClickListener {
    private RelativeLayout mTitle;
    private ImageView mType_back;
    private LinearLayout mSearch_type;
    private TextView mAdd_type_income_btn;
    private TextView mAdd_type_cost_btn;
    private TextView mAdd_type_all_btn;
    private int accouType = KingAccountNode.MONEY_IN;
    private TextView startTime;
    private TextView endTime;

    private RecyclerView mRecyclerView;
    private HomeRecyclerAdapter mAdapter;
    private KingAccountStorage mAccountStorage;
    private ArrayList<KingAccountNode> mAccountNodes;
    private long start = CalendarUtil.getNowTimeMillis() - 30 * 86400;
    private long end = CalendarUtil.getNowTimeMillis();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initRMethod();

    }

    @Override
    public void initView() {
        super.initView();
        mTitle = (RelativeLayout) findViewById(R.id.title);
        mType_back = (ImageView) findViewById(R.id.type_back);
        mSearch_type = (LinearLayout) findViewById(R.id.search_type);
        mAdd_type_income_btn = (TextView) findViewById(R.id.add_type_income_btn);
        mAdd_type_cost_btn = (TextView) findViewById(R.id.add_type_cost_btn);
        mAdd_type_all_btn = (TextView) findViewById(R.id.add_type_all_btn);
        mAdd_type_all_btn.setOnClickListener(this);
        mAdd_type_income_btn.setOnClickListener(this);
        mAdd_type_cost_btn.setOnClickListener(this);
        mType_back.setOnClickListener(this);
        startTime = (TextView) findViewById(R.id.startTime);
        startTime.setText(CalendarUtil.getStringYMD(CalendarUtil.timeMilis2Date(start)));
        endTime = (TextView) findViewById(R.id.endTime);
        endTime.setText(CalendarUtil.getStringYMD(CalendarUtil.timeMilis2Date(end)));
        startTime.setOnClickListener(this);
        endTime.setOnClickListener(this);
        mAdapter = new HomeRecyclerAdapter(this);
        mRecyclerView = (RecyclerView) findViewById(R.id.staticsRecycleView);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setClickListener(this);
    }


    @Override
    public void initRMethod() {
        super.initRMethod();
        mAccountStorage = new KingAccountStorage(this);
        search();
    }

    public void search() {
        mAccountNodes =
                (ArrayList<KingAccountNode>) mAccountStorage.queryForTime(accouType, start, end);
        if (null != mAccountNodes && mAccountNodes.size() > 0) {
            mAdapter.setParams(mAccountNodes);
            mAdapter.notifyDataSetChanged();
        } else {
            mAdapter.setParams(null);
            mAdapter.notifyDataSetChanged();
        }
    }

    public void selectIncomeType() {
        if (accouType == KingAccountNode.MONEY_IN) {
            mAdd_type_income_btn.setBackgroundResource(R.drawable.search_type_left_bg_press);
            mAdd_type_income_btn.setTextColor(getResources().getColor(R.color.white));
            mAdd_type_cost_btn.setBackgroundResource(R.drawable.search_type_center_bg);
            mAdd_type_cost_btn.setTextColor(getResources().getColor(R.color.my_color));
            mAdd_type_all_btn.setBackgroundResource(R.drawable.search_type_bg);
            mAdd_type_all_btn.setTextColor(getResources().getColor(R.color.my_color));
            search();

        } else if (accouType == KingAccountNode.MONEY_OUT) {
            mAdd_type_cost_btn.setBackgroundResource(R.drawable.search_type_center_press_bg);
            mAdd_type_cost_btn.setTextColor(getResources().getColor(R.color.white));
            mAdd_type_income_btn.setBackgroundResource(R.drawable.search_type_left_bg);
            mAdd_type_income_btn.setTextColor(getResources().getColor(R.color.my_color));
            mAdd_type_all_btn.setBackgroundResource(R.drawable.search_type_bg);
            mAdd_type_all_btn.setTextColor(getResources().getColor(R.color.my_color));
            search();

        } else {
            mAdd_type_all_btn.setBackgroundResource(R.drawable.search_type_bg_press);
            mAdd_type_all_btn.setTextColor(getResources().getColor(R.color.white));
            mAdd_type_cost_btn.setBackgroundResource(R.drawable.search_type_center_bg);
            mAdd_type_cost_btn.setTextColor(getResources().getColor(R.color.my_color));
            mAdd_type_income_btn.setBackgroundResource(R.drawable.search_type_left_bg);
            mAdd_type_income_btn.setTextColor(getResources().getColor(R.color.my_color));
            accouType = 3;
            search();
        }
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_statics;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.type_back:
                finish();
                break;
            case R.id.add_type_income_btn: //　收入
                accouType = KingAccountNode.MONEY_IN;
                selectIncomeType();
                break;
            case R.id.add_type_cost_btn: //　收入
                accouType = KingAccountNode.MONEY_OUT;
                selectIncomeType();
                break;
            case R.id.add_type_all_btn:
                accouType = 3;
                selectIncomeType();
                break;
            case R.id.startTime:
                getDate(start, 0);
                break;
            case R.id.endTime:
                getDate(end, 1);
                break;
            default:
                break;
        }

    }

    public void getDate(long time, final int startType) {
        final CalendarDialog calendarDialog = new CalendarDialog(this);
        calendarDialog.setDate(0 == time ? CalendarUtil.timeMilis2Date(CalendarUtil.getNowTimeMillis()) : CalendarUtil.timeMilis2Date(time));
        calendarDialog.setOnSelectDateListener(new CalendarDialog.OnSelectDateListener() {
            @Override
            public void okDate(long date) {
                calendarDialog.dismiss();
                if (startType == 0) {
                    start = date;
                    startTime.setText(CalendarUtil.getStringYMD(CalendarUtil.timeMilis2Date(start)));
                    search();
                } else if (startType == 1) {
                    end = date;
                    endTime.setText(CalendarUtil.getStringYMD(CalendarUtil.timeMilis2Date(end)));
                    search();
                }
            }
        });
        PermissionUtil.getAlertPermission(this, calendarDialog);
    }

    @Override
    public void onItemClick(View view, int position) {
        // // TODO: 2017/3/14 进行界面跳转到详情界面
        LogUtil.d(TAG, "onItemClick->position=" + position);
        Intent intent = new Intent(this, TypeStaticsActivity.class);
        intent.putExtra(ActivityLib.INTENT_PARAM, mAccountNodes.get(position));
        intent.putExtra(ActivityLib.INTENT_PARAM2, start);
        intent.putExtra(ActivityLib.INTENT_PARAM3, end);
        startActivity(intent);
    }

    @Override
    public void onLongClick(View view, int position) {

    }

    @Override
    public void call(RxBusEvent event) {
        super.call(event);
        switch (event.getId()) {
            case RxBusEvent.REFRESH_ACCOUNT_LIST:
                initRMethod();
                break;
            default:
                break;
        }

    }
}
