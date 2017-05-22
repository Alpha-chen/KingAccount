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
import com.account.king.util.TypeUtil;
import com.account.king.view.dialog.CalendarDialog;

import java.util.ArrayList;

import static com.account.king.R.id.endTime;
import static com.account.king.R.id.startTime;

public class TypeStaticsActivity extends BaseActivity implements HomeRecyclerAdapter.OnItemClickListener, View.OnClickListener {

    private RelativeLayout mTitle;
    private ImageView mType_back;
    private LinearLayout mTime;
    private TextView mStartTime;
    private TextView mEndTime;
    private RecyclerView mStaticsRecycleView;
    private HomeRecyclerAdapter mAdapter;
    private KingAccountStorage mAccountStorage;
    private ArrayList<KingAccountNode> mAccountNodes;
    private long start = CalendarUtil.getNowTimeMillis() - 30 * 86400;
    private long end = CalendarUtil.getNowTimeMillis();
    private int type = 0;
    private KingAccountNode node = new KingAccountNode();
    private TextView title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initIntent();
        initViewData();
        initRMethod();
    }

    @Override
    public void initView() {
        super.initView();
        title = (TextView) findViewById(R.id.title);
        mType_back = (ImageView) findViewById(R.id.type_back);
        mType_back.setOnClickListener(this);
        mTime = (LinearLayout) findViewById(R.id.time);
        mStartTime = (TextView) findViewById(startTime);
        mEndTime = (TextView) findViewById(endTime);
        mStaticsRecycleView = (android.support.v7.widget.RecyclerView) findViewById(R.id.staticsRecycleView);
        mAdapter = new HomeRecyclerAdapter(this);
        mStaticsRecycleView = (RecyclerView) findViewById(R.id.staticsRecycleView);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        mStaticsRecycleView.setLayoutManager(layoutManager);
        mStaticsRecycleView.setAdapter(mAdapter);
        mAdapter.setClickListener(this);
        mStartTime.setOnClickListener(this);
        mEndTime.setOnClickListener(this);
    }

    @Override
    public void initViewData() {
        super.initViewData();
        title.setText(TypeUtil.getType(this, node.getAccount_type(), node.getType()));
        mStartTime.setText(CalendarUtil.getStringYMD(CalendarUtil.timeMilis2Date(start)));
        mEndTime.setText(CalendarUtil.getStringYMD(CalendarUtil.timeMilis2Date(end)));

    }

    @Override
    public void initIntent() {
        super.initIntent();
        node = (KingAccountNode) getIntent().getSerializableExtra(ActivityLib.INTENT_PARAM);
        type = node.getType();
        start = getIntent().getLongExtra(ActivityLib.INTENT_PARAM2, 0);
        end = getIntent().getLongExtra(ActivityLib.INTENT_PARAM3, 0);
    }

    @Override
    public void initRMethod() {
        super.initRMethod();
        mAccountStorage = new KingAccountStorage(this);
        search();
    }

    public void search() {
        mAccountNodes =
                (ArrayList<KingAccountNode>) mAccountStorage.queryForType(node.getAccount_type(),start, end, type);
        if (null != mAccountNodes && mAccountNodes.size() > 0) {
            mAdapter.setParams(mAccountNodes);
            mAdapter.notifyDataSetChanged();
        } else {
            mAdapter.setParams(null);
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_type_statics;
    }

    @Override
    public void onItemClick(View view, int position) {
        LogUtil.d(TAG, "onItemClick->position=" + position);
        Intent intent = new Intent(this, DetailAccountActivity.class);
        intent.putExtra(ActivityLib.INTENT_PARAM, mAccountNodes.get(position));
        startActivity(intent);
    }

    @Override
    public void onLongClick(View view, int position) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.startTime:
                getDate(start, 0);
                break;
            case R.id.endTime:
                getDate(end, 1);
                break;
            case R.id.type_back:
                finish();
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
                    mStartTime.setText(CalendarUtil.getStringYMD(CalendarUtil.timeMilis2Date(start)));
                    search();
                } else if (startType == 1) {
                    end = date;
                    mEndTime.setText(CalendarUtil.getStringYMD(CalendarUtil.timeMilis2Date(end)));
                    search();
                }
            }
        });
        PermissionUtil.getAlertPermission(this, calendarDialog);
    }

    @Override
    public void call(RxBusEvent event) {
        super.call(event);
        switch (event.getId()) {
            case RxBusEvent.REFRESH_ACCOUNT_LIST:
                initRMethod();
            default:
                break;
        }
    }
}
