package com.account.king;

import android.animation.PropertyValuesHolder;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.account.king.node.KingAccountNode;
import com.account.king.presenter.contract.AddAccountContract.IAddAcountView;
import com.account.king.presenter.contract.presenter.AddAccountPresenter;
import com.account.king.util.ActivityLib;
import com.account.king.util.CalendarUtil;
import com.account.king.util.LogUtil;
import com.account.king.view.KeyBoardView;
import com.account.king.view.RoundCornerImageView;


public class AddAccountActivity extends BaseActivity implements View.OnClickListener
        , KeyBoardView.NumClickListener, IAddAcountView {
    //关闭动画部分机型在style中设置无效
    protected int activityCloseEnterAnimation;
    protected int activityCloseExitAnimation;
    private TextView costBtn, incomeBtn;
    private int type = KingAccountNode.MONEY_OUT;

    private KeyBoardView keyBoardView;

    private RelativeLayout mActivity_add_account;
    private RelativeLayout mTop_bar;
    private TextView mAdd_type_income_btn;
    private TextView mAdd_type_cost_btn;
    private RelativeLayout mTitle_left;
    private ImageView mTitle_left_image;
    private ImageView mAdd_account_done;
    private LinearLayout mAdd_account_lay;
    private RelativeLayout mAccount_price_lay;
    private ImageView mAccount_price_iv;
    private TextView mAccount_price_tv;
    private EditText mAccount_price_input;
    private RelativeLayout mAccount_count_lay;
    private ImageView mAccount_count_iv;
    private TextView mAccount_count_tv;
    private EditText mAccount_count_input;
    private RelativeLayout mAccount_type_lay;
    private ImageView mAccount_type_iv;
    private TextView mAccount_type_tv;
    private TextView mAccount_type_select_tv;
    private ImageView mAccount_type_select;
    private RelativeLayout mAdd_account_click_input;
    private Button mAdd_account_time;
    private RoundCornerImageView mAdd_account_select;
    private ImageView mAdd_account_writer_note;

    private int inputType = 0; // 0  单价 1  数量
    private KingAccountNode oldAccountNode;
    private KingAccountNode accountNode;
    private AddAccountPresenter presenter;
    private boolean isEdit = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initIntent();
        initView();
        initPresenter();
    }

    @Override
    public void initIntent() {
        super.initIntent();
        Intent data = getIntent();
        oldAccountNode = (KingAccountNode) data.getSerializableExtra(ActivityLib.INTENT_PARAM);
        isEdit = data.getBooleanExtra(ActivityLib.START_TYPE, false);
        if (oldAccountNode == null) {
            oldAccountNode = new KingAccountNode();
//            oldAccountNode.setMoney_type(NodeUtil.MONEY_OUT);
            oldAccountNode.setYmd_hms(CalendarUtil.getNowTimeMillis());
        }

        accountNode = (KingAccountNode) oldAccountNode.copy();
        type = accountNode.getAccount_type();
    }

    @Override
    public void initPresenter() {
        super.initPresenter();
        presenter = new AddAccountPresenter(this);

    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_add_account;
    }

    @Override
    public void initView() {
        super.initView();
        activityCloseEnterAnimation = android.R.anim.fade_in;
        activityCloseExitAnimation = R.anim.activity_push_bottom_out;
        findViewById(R.id.title_left_image).setOnClickListener(this);

        costBtn = (TextView) findViewById(R.id.add_type_cost_btn);
        incomeBtn = (TextView) findViewById(R.id.add_type_income_btn);
        costBtn.setOnClickListener(this);
        incomeBtn.setOnClickListener(this);
        mActivity_add_account = (RelativeLayout) findViewById(R.id.activity_add_account);
        mTop_bar = (RelativeLayout) findViewById(R.id.top_bar);
        mAdd_type_income_btn = (TextView) findViewById(R.id.add_type_income_btn);
        mAdd_type_cost_btn = (TextView) findViewById(R.id.add_type_cost_btn);
        mTitle_left = (RelativeLayout) findViewById(R.id.title_left);
        mTitle_left_image = (ImageView) findViewById(R.id.title_left_image);
        mAdd_account_done = (ImageView) findViewById(R.id.add_account_done);
        mAdd_account_lay = (LinearLayout) findViewById(R.id.add_account_lay);
        mAccount_price_lay = (RelativeLayout) findViewById(R.id.account_price_lay);
        mAccount_price_iv = (ImageView) findViewById(R.id.account_price_iv);
        mAccount_price_tv = (TextView) findViewById(R.id.account_price_tv);
        mAccount_price_input = (EditText) findViewById(R.id.account_price_input);
        mAccount_price_input.setOnClickListener(this);
        mAccount_count_lay = (RelativeLayout) findViewById(R.id.account_count_lay);
        mAccount_count_iv = (ImageView) findViewById(R.id.account_count_iv);
        mAccount_count_tv = (TextView) findViewById(R.id.account_count_tv);
        mAccount_count_input = (EditText) findViewById(R.id.account_count_input);
        mAccount_count_input.setOnClickListener(this);
        mAccount_type_lay = (RelativeLayout) findViewById(R.id.account_type_lay);
        mAccount_type_iv = (ImageView) findViewById(R.id.account_type_iv);
        mAccount_type_tv = (TextView) findViewById(R.id.account_type_tv);
        mAccount_type_select_tv = (TextView) findViewById(R.id.account_type_select_tv);
        mAccount_type_select = (ImageView) findViewById(R.id.account_type_select);
        mAdd_account_click_input = (RelativeLayout) findViewById(R.id.add_account_click_input);
        mAdd_account_time = (Button) findViewById(R.id.add_account_time);
        mAdd_account_time.setOnClickListener(this);
        mAdd_account_select = (RoundCornerImageView) findViewById(R.id.add_account_select);
        mAdd_account_writer_note = (ImageView) findViewById(R.id.add_account_writer_note);
//        keyBoardView = (KeyBoardView) findViewById(R.id.keyboard_view);
//        keyBoardView.setNumClickListener(this);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(activityCloseEnterAnimation, activityCloseExitAnimation);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_left_image:
                finish();
                break;
            case R.id.add_type_cost_btn:
                selectCostType(true);
                break;
            case R.id.add_type_income_btn:
                selectIncomeType(true);
                break;
            case R.id.account_count_input: // 点击了数量
                inputType = 1;
                break;
            case R.id.account_price_input: // 点击了单价
                inputType = 0;
                break;
            case R.id.add_account_time:
                presenter.selectDate(this, accountNode);
                break;
            default:
                break;
        }
    }


    public void selectCostType(boolean isSelectType) {
        if (type != KingAccountNode.MONEY_OUT || !isSelectType) {
            type = KingAccountNode.MONEY_OUT;
            costBtn.setBackgroundResource(R.drawable.add_count_type_select);
            costBtn.setTextColor(getResources().getColor(R.color.my_color));
            incomeBtn.setBackgroundDrawable(null);
            incomeBtn.setTextColor(getResources().getColor(R.color.white));
//            dragTypeAdapter.setParams(isTypeEdit,costTypeNodes);
//            moneyText.setTextColor(getResources().getColor(R.color.color5));
//            if (isSelectType) {
//                selectFirst();
//            }
        }
    }

    //选中第一个类别
 /*   private void selectFirst() {
        if (type == NodeUtil.MONEY_OUT) {
            if (!ActivityLib.isEmpty(costTypeNodes)) {
                selectTypeNode(costTypeNodes.get(0));
            }
        } else {
            if (!ActivityLib.isEmpty(incomeTypeNodes)) {
                selectTypeNode(incomeTypeNodes.get(0));
            }
        }
    }*/

    public void selectIncomeType(boolean isSelectType) {
        if (type != KingAccountNode.MONEY_IN || !isSelectType) {
            type = KingAccountNode.MONEY_IN;
            costBtn.setBackgroundDrawable(null);
            costBtn.setTextColor(getResources().getColor(R.color.white));
            incomeBtn.setBackgroundResource(R.drawable.add_count_type_select);
            incomeBtn.setTextColor(getResources().getColor(R.color.my_color));
//            dragTypeAdapter.setParams(isTypeEdit,incomeTypeNodes);
//            moneyText.setTextColor(getResources().getColor(R.color.color6));
//            if (isSelectType) {
//                selectFirst();
//            }
        }
    }

    @Override
    public void setNoteRes(int Res) {

    }

    @Override
    public void setDateText(String date) {

    }

    @Override
    public void jitterMoney(PropertyValuesHolder pvhRotate) {

    }

    @Override
    public void showKeyBoard() {

    }

    @Override
    public void numClick(String currentMoney) {
        LogUtil.d(TAG, "currentMoney=" + currentMoney);
        if (inputType == 0) {
            mAccount_price_input.setText(currentMoney);
        } else if (inputType == 1) {
            mAccount_count_input.setText(currentMoney);
        }
    }

    @Override
    public void okClick() {

    }
}
