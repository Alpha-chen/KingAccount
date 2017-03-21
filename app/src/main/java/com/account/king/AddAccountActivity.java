package com.account.king;

import android.Manifest;
import android.animation.PropertyValuesHolder;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.account.king.constant.WhatConstants;
import com.account.king.node.Attachment;
import com.account.king.node.KingAccountNode;
import com.account.king.presenter.contract.AddAccountContract.IAddAcountView;
import com.account.king.presenter.contract.presenter.AddAccountPresenter;
import com.account.king.rxevent.RxBus;
import com.account.king.rxevent.RxBusEvent;
import com.account.king.util.ActivityLib;
import com.account.king.util.CalendarUtil;
import com.account.king.util.LogUtil;
import com.account.king.util.ToastUtil;
import com.account.king.view.KeyBoardView;
import com.account.king.view.RoundCornerImageView;

import pink.net.multiimageselector.MultiImageSelectorActivity;
import pink.net.multiimageselector.bean.SelectedImages;
import pink.net.multiimageselector.utils.ImageLoadUtil;
import pink.net.multiimageselector.utils.ImageSelector;
import pink.net.multiimageselector.utils.MultiSelectorUtils;


public class AddAccountActivity extends BaseActivity implements View.OnClickListener
        , KeyBoardView.NumClickListener, IAddAcountView {

    private int REQUEST_CODE_ASK_IMAGE_PHONE = 123;

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
    private TextView mAdd_account_note;
    private RoundCornerImageView mAdd_account_select;
    private ImageView mAdd_account_writer_note;

    private int inputType = 0; // 0  单价 1  数量
    private KingAccountNode oldAccountNode;
    private KingAccountNode accountNode;
    private AddAccountPresenter presenter;
    private boolean isEdit = false;
    private Attachment attachment = new Attachment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initIntent();
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
            mAdd_account_time.setText(CalendarUtil.TimeStamp2Date(System.currentTimeMillis()));
        }
        accountNode = (KingAccountNode) oldAccountNode.copy();
        type = accountNode.getAccount_type();
    }

    @Override
    public void initPresenter() {
        super.initPresenter();
        presenter = new AddAccountPresenter(this, this);

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
        mTitle_left = (RelativeLayout) findViewById(R.id.title_left);
        mTitle_left_image = (ImageView) findViewById(R.id.title_left_image);
        mAdd_account_done = (ImageView) findViewById(R.id.add_account_done);
        mAdd_account_done.setOnClickListener(this);
        mAdd_account_lay = (LinearLayout) findViewById(R.id.add_account_lay);
        mAccount_price_lay = (RelativeLayout) findViewById(R.id.account_price_lay);
        mAccount_price_iv = (ImageView) findViewById(R.id.account_price_iv);
        mAccount_price_tv = (TextView) findViewById(R.id.account_price_tv);
        mAccount_price_input = (EditText) findViewById(R.id.account_price_input);
        mAccount_price_input.setOnClickListener(this);
        mAccount_count_lay = (RelativeLayout) findViewById(R.id.account_count_lay);
        mAccount_count_iv = (ImageView) findViewById(R.id.account_count_iv);
        mAccount_count_iv.setOnClickListener(this);
        mAccount_count_tv = (TextView) findViewById(R.id.account_count_tv);
        mAccount_count_input = (EditText) findViewById(R.id.account_count_input);
        mAccount_count_input.setOnClickListener(this);
        mAccount_count_input.setSelection(1);
        mAccount_type_lay = (RelativeLayout) findViewById(R.id.account_type_lay);
        mAccount_type_iv = (ImageView) findViewById(R.id.account_type_iv);
        mAccount_type_tv = (TextView) findViewById(R.id.account_type_tv);
        mAccount_type_select_tv = (TextView) findViewById(R.id.account_type_select_tv);
        mAccount_type_select_tv.setOnClickListener(this);
        mAdd_account_click_input = (RelativeLayout) findViewById(R.id.add_account_click_input);
        mAdd_account_time = (Button) findViewById(R.id.add_account_time);
        mAdd_account_time.setOnClickListener(this);
        mAdd_account_note = (TextView) findViewById(R.id.add_account_note);
        mAdd_account_note.setOnClickListener(this);
        mAdd_account_select = (RoundCornerImageView) findViewById(R.id.add_account_select);
        mAdd_account_select.setOnClickListener(this);
        mAdd_account_writer_note = (ImageView) findViewById(R.id.add_account_writer_note);
        mAdd_account_writer_note.setOnClickListener(this);
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
            case R.id.add_account_done:
                if (getNode()) {
                    LogUtil.d(TAG, "accountNode=" + accountNode.toString());
                    if (presenter.insertBookNode(AddAccountActivity.this, accountNode)) {
                        ToastUtil.makeToast(AddAccountActivity.this, "插入成功");
                    } else {
                        ToastUtil.makeToast(AddAccountActivity.this, "插入失败");
                    }
                    RxBus.getDefault().send(new RxBusEvent(RxBusEvent.REFRESH_ACCOUNT_LIST));
                    finish();
                }
                break;
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
            case R.id.add_account_writer_note:
                presenter.clickWriterNote(this, accountNode);
                break;
            case R.id.add_account_note:
                presenter.clickWriterNote(this, accountNode);
                break;
            case R.id.account_type_select_tv:
                Intent intent = new Intent(AddAccountActivity.this, TypeActivity.class);
                intent.putExtra(ActivityLib.INTENT_PARAM, accountNode);
                startActivity(intent);
                break;
            case R.id.add_account_select:
//                presenter.selectPhoto(this, accountNode);
                if (null != accountNode.getAttachment() && !TextUtils.isEmpty(accountNode.getAttachment().getAttachment_path())) {
                    Intent data = new Intent(AddAccountActivity.this, PhotoActivity.class);
                    data.putExtra(ActivityLib.INTENT_PARAM, accountNode.getAttachment().getAttachment_path());
                    startActivityForResult(data, WhatConstants.Refresh.PHOTO_DELETE);
                } else {
                    if (Build.VERSION.SDK_INT >= 23) {
                        int checkCallPhonePermission = ContextCompat.checkSelfPermission(AddAccountActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
                        if (checkCallPhonePermission != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(AddAccountActivity.this,
                                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE_ASK_IMAGE_PHONE);
                            return;
                        }
                    }
                    MultiSelectorUtils.selectImage(AddAccountActivity.this,
                            new ImageSelector.Builder().editMode(2).build());
                }
                break;
            default:
                break;
        }
    }

    public boolean getNode() {
        String price = mAccount_price_input.getText().toString().trim();
        String count = mAccount_count_input.getText().toString().trim();
        if (TextUtils.isEmpty(price) || TextUtils.isEmpty(count)) {
            ToastUtil.makeToast(AddAccountActivity.this, "单价与数量任何一个都不能是空哦~~~");
            return false;
        }
        accountNode.setCount(Double.parseDouble(count));
        accountNode.setPrice(Double.parseDouble(price));
        accountNode.setAttachment(attachment);
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 123:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission Granted
                    MultiSelectorUtils.selectImage(AddAccountActivity.this,
                            new ImageSelector.Builder().editMode(2).build());
                } else {
                    // Permission Denied
                    Toast.makeText(AddAccountActivity.this, "没有媒体访问权限", Toast.LENGTH_SHORT)
                            .show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }


    //图片选择删除
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case MultiSelectorUtils.SELECTOR_REQUES_CODE:
                    if (data != null) {
                        SelectedImages selectedImages = (SelectedImages) data.getSerializableExtra(MultiImageSelectorActivity.EXTRA_RESULT);
                        String path = selectedImages.getEditPath(0);
                        attachment.setAttachment_path(path);
                        ImageLoadUtil.loadRound(this, path, mAdd_account_select);
                    }
                    break;
                case WhatConstants.Refresh.PHOTO_DELETE:
                    mAdd_account_select.setImageResource(R.drawable.ic_add_photo);
                    attachment.setAttachment_path("");
                    break;
                case WhatConstants.Refresh.ACCOUNT_INPUT_NOTE:
                    String note = data.getStringExtra(ActivityLib.INTENT_PARAM);
                    attachment.setContent(note);
                    presenter.loadNote(this, note);
                    break;
            }
        }
    }

    public void selectCostType(boolean isSelectType) {
        LogUtil.d(TAG, "type=" + type);
        if (type != KingAccountNode.MONEY_OUT || !isSelectType) {
            type = KingAccountNode.MONEY_OUT;
            costBtn.setBackgroundResource(R.drawable.add_count_type_select);
            costBtn.setTextColor(getResources().getColor(R.color.my_color));
            incomeBtn.setBackgroundDrawable(null);
            incomeBtn.setTextColor(getResources().getColor(R.color.white));
//            dragTypeAdapter.setParams(isTypeEdit,costTypeNodes);
//            moneyText.setTextColor(getResources().getColor(R.color.color5));
//            if (isSelectTyp e) {
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
        LogUtil.d(TAG, "type=" + type);
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
    public void setNoteRes(int Res, String note) {
        if (0 == Res) {
            mAdd_account_note.setVisibility(View.VISIBLE);
            mAdd_account_writer_note.setVisibility(View.GONE);
            mAdd_account_note.setText(note);
        } else {
            mAdd_account_note.setVisibility(View.GONE);
            mAdd_account_writer_note.setVisibility(View.VISIBLE);
            mAdd_account_writer_note.setImageResource(Res);
        }
    }

    @Override
    public void setDateText(String date) {
        mAdd_account_time.setText(date);
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
