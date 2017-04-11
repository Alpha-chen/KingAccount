package com.account.king;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.view.KeyEvent;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.account.king.node.AlarmNode;
import com.account.king.presenter.contract.SetAccountContract;
import com.account.king.presenter.contract.presenter.SetAccountPresenter;
import com.account.king.rxevent.RxBus;
import com.account.king.rxevent.RxBusEvent;
import com.account.king.util.ActivityLib;
import com.account.king.util.SPUtils;

/**
 * 记账设置界面
 *
 * @author king
 */
public class SetAccountActivity extends BaseActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener
        , SetAccountContract.ISetAccountView {


    private static final int BUDGET_DAY = 1;
    private SetAccountPresenter presenter;

    private SwitchCompat switchRemind;
    private SwitchCompat switchLock;
    private LinearLayout remindContent;

    private int oldBudgetDay = BUDGET_DAY;
    private int budgetDay;

    private String[] repeats;
    private AlarmNode oldAlarmNode;
    private AlarmNode alarmNode;

    private RelativeLayout remindTime;
    private RelativeLayout repeatModel;
    private RelativeLayout lockEdit;

    private TextView remind_text;
    private TextView repeat_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initView();
        initTitleBar();
        initPresenter();
        initRMethod();
        updateViewData();
        initNew();
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_set_account;
    }

    @Override
    public void initView() {
        super.initView();
        switchRemind = (SwitchCompat) findViewById(R.id.switch_remind);
        switchRemind.setOnCheckedChangeListener(this);
        remindContent = (LinearLayout) findViewById(R.id.remind_content);
        findViewById(R.id.remind_date).setOnClickListener(this);
        findViewById(R.id.repeat).setOnClickListener(this);
        remindTime = (RelativeLayout) findViewById(R.id.remind_date);
        repeatModel = (RelativeLayout) findViewById(R.id.repeat);
        switchLock = (SwitchCompat) findViewById(R.id.switch_lock);
        lockEdit = (RelativeLayout) findViewById(R.id.lock_edit);
        lockEdit.setOnClickListener(this);

        remind_text = (TextView) findViewById(R.id.remind_text);
        repeat_text = (TextView) findViewById(R.id.repeat_text);
        findViewById(R.id.title_left_image).setOnClickListener(this);
    }

    @Override
    public void initPresenter() {
        super.initPresenter();
        presenter = new SetAccountPresenter(this);
    }

    @Override
    public void initTitleBar() {
        super.initTitleBar();
    }

    @Override
    public void initRMethod() {
        super.initRMethod();
        //预算结算日
        oldBudgetDay = SPUtils.getInt(this, SPUtils.BUDGET_DAY);
        if (oldBudgetDay == 0) {
            oldBudgetDay = BUDGET_DAY;
        }
        budgetDay = oldBudgetDay;
        //记账提醒
        repeats = getResources().getStringArray(R.array.repeat_time);

        oldAlarmNode = new AlarmNode();
        oldAlarmNode.setRemind(SPUtils.getBoolean(this, SPUtils.REMIND_START));
        oldAlarmNode.setRepeat_mode(SPUtils.getInt(this, SPUtils.REPEAT_MODEL));//重复模式
        oldAlarmNode.setHour(SPUtils.getInt(this, SPUtils.REMIND_HOUR));
        oldAlarmNode.setMinute(SPUtils.getInt(this, SPUtils.REMIND_MINUTE));
        if (oldAlarmNode.getHour() == 0 && !oldAlarmNode.isRemind()) {
            oldAlarmNode.setHour(19);
        }
        alarmNode = oldAlarmNode.copy();
    }

    @Override
    public void updateViewData() {
        super.updateViewData();
        if (KingApplication.isLock) {
            isOpenLock(true);
        }
        switchLock.setOnCheckedChangeListener(this);
        if (alarmNode.isRemind()) {
            switchRemind.setChecked(true);
            startRemind();
        }
        remind_text.setText(alarmNode.getTime());
        repeat_text.setText(repeats[alarmNode.getRepeat_mode()]);
    }

    private void initNew() {
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.remind_date:
                presenter.showTimePicker(this, alarmNode.getHour(), alarmNode.getMinute());
                break;
            case R.id.repeat:
                presenter.showWeekPicker(this, 0);
                break;
            case R.id.lock_edit:
                Intent data = new Intent(this, SetLockActivity.class);
                data.putExtra(ActivityLib.START_TYPE, true);
                startActivity(data);
                break;
            case R.id.title_left_image:
                finish();
                break;
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.switch_remind:
                checkRemind(isChecked);
                break;
            case R.id.switch_lock:
                presenter.switchLock(this, isChecked);
                break;
        }
    }

    private void checkRemind(boolean isChecked) {
        presenter.switchRemind(isChecked);
        alarmNode.setRemind(isChecked);
    }

    private void isOpenLock(boolean isOpen) {
        switchLock.setChecked(isOpen);
        lockEdit.setVisibility(isOpen ? View.VISIBLE : View.GONE);
    }

    @Override
    public void startRemind() {
        remindContent.setVisibility(View.VISIBLE);
    }

    @Override
    public void stopRemind() {
        remindContent.setVisibility(View.GONE);
    }


    //预算结算日
    @Override
    public void setDayPicker(int position, String day) {
        this.budgetDay = position;
        SPUtils.put(this, SPUtils.BUDGET_DAY, position);
    }

    //提醒时间
    @Override
    public void setTimePicker(String hour, String minute) {
        alarmNode.setHour(Integer.parseInt(hour));
        alarmNode.setMinute(Integer.parseInt(minute));
        remind_text.setText(alarmNode.getTime());
    }

    //重复模式
    @Override
    public void setRepeatPicker(int position, String day) {
        alarmNode.setRepeat_mode(position);
        repeat_text.setText(day);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
            return true;
        }
        return false;
    }

    @Override
    public void finish() {
        super.finish();
        if (oldBudgetDay != budgetDay) {
            //LogUtil.d("nnn","refresh budget="+budgetDay);
            RxBus.getDefault().send(new RxBusEvent(RxBusEvent.BUDGET_DAY_UPDATE));
        }
        if (!alarmNode.beCompare(oldAlarmNode)) {
            //LogUtil.d("nnn","refresh alarmNode="+alarmNode.toString());
            if (switchRemind.isChecked()) {
                SPUtils.put(this, SPUtils.REMIND_START, true);
                presenter.openAlarm(this, alarmNode);
            } else {
                SPUtils.put(this, SPUtils.REMIND_START, false);
                presenter.closeAlarm(this);
            }
            SPUtils.put(this, SPUtils.REPEAT_MODEL, alarmNode.getRepeat_mode());
            SPUtils.put(this, SPUtils.REMIND_HOUR, alarmNode.getHour());
            SPUtils.put(this, SPUtils.REMIND_MINUTE, alarmNode.getMinute());
        }
    }

    @Override
    public void call(RxBusEvent rxBusEvent) {
        switch (rxBusEvent.getId()) {
            case RxBusEvent.LOCK_FAIL:
                isOpenLock(false);
                break;
            case RxBusEvent.LOCK_SUCCESS:
                openLockEdit();
                break;
        }
        super.call(rxBusEvent);
    }

    @Override
    public void closeLockEdit() {
        lockEdit.setVisibility(View.GONE);
    }

    @Override
    public void openLockEdit() {
        lockEdit.setVisibility(View.VISIBLE);
    }
}
