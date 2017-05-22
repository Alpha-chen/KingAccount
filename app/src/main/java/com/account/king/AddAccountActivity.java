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
import android.widget.TextView;
import android.widget.Toast;

import com.account.king.constant.WhatConstants;
import com.account.king.node.Attachment;
import com.account.king.node.KingAccountNode;
import com.account.king.node.TypeNode;
import com.account.king.presenter.contract.AddAccountContract.IAddAcountView;
import com.account.king.presenter.contract.presenter.AddAccountPresenter;
import com.account.king.rxevent.RxBus;
import com.account.king.rxevent.RxBusEvent;
import com.account.king.util.ActivityLib;
import com.account.king.util.CalendarUtil;
import com.account.king.util.KingJson;
import com.account.king.util.LogUtil;
import com.account.king.util.SPUtils;
import com.account.king.util.ToastUtil;
import com.account.king.util.TypeUtil;
import com.account.king.view.KeyBoardView;
import com.account.king.view.RoundCornerImageView;

import java.util.ArrayList;

import pink.net.multiimageselector.MultiImageSelectorActivity;
import pink.net.multiimageselector.bean.SelectedImages;
import pink.net.multiimageselector.utils.ImageLoadUtil;
import pink.net.multiimageselector.utils.ImageSelector;
import pink.net.multiimageselector.utils.MultiSelectorUtils;


/**
 * 添加记账
 *
 * @author king
 */
public class AddAccountActivity extends BaseActivity implements View.OnClickListener
        , KeyBoardView.NumClickListener, IAddAcountView {

    private int REQUEST_CODE_ASK_IMAGE_PHONE = 123;
    private int REQUEST_CODE_ASK_TYPE = 124;

    //关闭动画部分机型在style中设置无效
    protected int activityCloseEnterAnimation;
    protected int activityCloseExitAnimation;
    private TextView costBtn, incomeBtn;
    private int accpuntType = KingAccountNode.MONEY_OUT;


    private ImageView mAdd_account_done;
    private EditText mAccount_price_input;
    private ImageView mAccount_count_iv;
    private EditText mAccount_count_input;
    private TextView mAccount_type_select_tv;
    private Button mAdd_account_time;
    private TextView mAdd_account_note;
    private RoundCornerImageView mAdd_account_select;
    private ImageView mAdd_account_writer_note;
    private ImageView add_account_location;
    private TextView add_account_location_detail;

    private int inputType = 0; // 0  单价 1  数量
    private KingAccountNode oldAccountNode;
    private KingAccountNode accountNode;
    private AddAccountPresenter presenter;
    private boolean isEdit = false;
    private Attachment attachment = new Attachment();

    private ArrayList<TypeNode> inTypeNodes = new ArrayList<>();
    private ArrayList<TypeNode> outTypeNodes = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initIntent();
        initView();
        initPresenter();
        initViewData();
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

        } else {
            isEdit = true;
        }
        accountNode = (KingAccountNode) oldAccountNode.copy();
        accpuntType = accountNode.getAccount_type();
    }

    @Override
    public void initViewData() {
        super.initViewData();
        if (0 != accountNode.getPrice()) {
            mAccount_price_input.setText(accountNode.getPrice() + "");
        }
        if (0 != accountNode.getCount()) {
            mAccount_count_input.setText(accountNode.getCount() + "");
        }
        String[] arrays = null;

        String typeOut = SPUtils.getString(this, SPUtils.MONEY_TYPE_OUT, "");
        String typeIn = SPUtils.getString(this, SPUtils.MONEY_TYPE_IN, "");
        if (TextUtils.isEmpty(typeOut)){
            arrays = getResources().getStringArray(R.array.account_outcome_type);
            for (int i = 0; i < arrays.length; i++) {
                TypeNode typeNode= new TypeNode();
                typeNode.setId(i);
                typeNode.setName(arrays[i]);
                outTypeNodes.add(typeNode);
            }
            SPUtils.put(this,SPUtils.MONEY_TYPE_OUT, KingJson.toJSON(outTypeNodes));
        }else {
            outTypeNodes = (ArrayList<TypeNode>) KingJson.parseArray(typeOut,TypeNode.class);
        }
        if (TextUtils.isEmpty(typeIn)){
            arrays = getResources().getStringArray(R.array.account_income_type);
            for (int i = 0; i < arrays.length; i++) {
                TypeNode typeNode= new TypeNode();
                typeNode.setId(i);
                typeNode.setName(arrays[i]);
                inTypeNodes.add(typeNode);
            }
            SPUtils.put(this,SPUtils.MONEY_TYPE_IN, KingJson.toJSON(inTypeNodes));
        }else {
            inTypeNodes = (ArrayList<TypeNode>) KingJson.parseArray(typeIn,TypeNode.class);
        }
        if (KingAccountNode.MONEY_OUT == accountNode.getAccount_type()) {
            mAccount_type_select_tv.setText(outTypeNodes.get(accountNode.getType()).getName());
        } else if (KingAccountNode.MONEY_IN == accountNode.getAccount_type()) {
            mAccount_type_select_tv.setText(inTypeNodes.get(accountNode.getType()).getName());
        }
        mAdd_account_time.setText(CalendarUtil.getStringMD(CalendarUtil.timeMilis2Date(accountNode.getYmd_hms())));
        if (null != accountNode.getAttachment()) {
            presenter.loadNote(AddAccountActivity.this, accountNode.getAttachment().getContent());
            presenter.loadImg(AddAccountActivity.this, accountNode.getAttachment().getAttachment_path(), mAdd_account_select);
            presenter.loadLocation(add_account_location_detail, accountNode.getAttachment().getLocation());
        } else {
            if (Build.VERSION.SDK_INT >= 23) {
                int checkCallPhonePermission = ContextCompat.checkSelfPermission(AddAccountActivity.this, Manifest.permission.ACCESS_FINE_LOCATION);
                if (checkCallPhonePermission != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(AddAccountActivity.this,
                            new String[]{Manifest.permission.ACCESS_LOCATION_EXTRA_COMMANDS
                                    , Manifest.permission.ACCESS_COARSE_LOCATION
                                    , Manifest.permission.ACCESS_FINE_LOCATION
                                    , Manifest.permission.ACCESS_WIFI_STATE}, REQUEST_CODE_ASK_TYPE);
                    return;
                }
            }
            presenter.getAccountLocation(add_account_location_detail, add_account_location);
        }
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
        mAdd_account_done = (ImageView) findViewById(R.id.add_account_done);
        mAdd_account_done.setOnClickListener(this);
        mAccount_price_input = (EditText) findViewById(R.id.account_price_input);
        mAccount_price_input.setOnClickListener(this);
        mAccount_count_iv = (ImageView) findViewById(R.id.account_count_iv);
        mAccount_count_iv.setOnClickListener(this);
        mAccount_count_input = (EditText) findViewById(R.id.account_count_input);
        mAccount_count_input.setOnClickListener(this);
        mAccount_count_input.setSelection(1);
        mAccount_type_select_tv = (TextView) findViewById(R.id.account_type_select_tv);
        mAccount_type_select_tv.setOnClickListener(this);
        mAdd_account_time = (Button) findViewById(R.id.add_account_time);
        mAdd_account_time.setOnClickListener(this);
        mAdd_account_note = (TextView) findViewById(R.id.add_account_note);
        mAdd_account_note.setOnClickListener(this);
        mAdd_account_select = (RoundCornerImageView) findViewById(R.id.add_account_select);
        mAdd_account_select.setOnClickListener(this);
        mAdd_account_writer_note = (ImageView) findViewById(R.id.add_account_writer_note);
        mAdd_account_writer_note.setOnClickListener(this);
        add_account_location = (ImageView) findViewById(R.id.add_account_location);
        add_account_location_detail = (TextView) findViewById(R.id.add_account_location_detail);
//        keyBoardView = (KeyBoardView) findViewById(R.id.keyboard_view);
//        keyBoardView.setNumClickListener(this);
        mAdd_account_time.setText(CalendarUtil.TimeStamp2Date(System.currentTimeMillis()));
        selectType();

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
                    if (isEdit) {
                        if (presenter.updateBookNode(AddAccountActivity.this, accountNode)) {
                            ToastUtil.makeToast(AddAccountActivity.this, "更新成功");
                        } else {
                            ToastUtil.makeToast(AddAccountActivity.this, "更新失败");
                        }
                    } else {
                        if (presenter.insertBookNode(AddAccountActivity.this, accountNode)) {
                            ToastUtil.makeToast(AddAccountActivity.this, "插入成功");
                        } else {
                            ToastUtil.makeToast(AddAccountActivity.this, "插入失败");
                        }
                    }
                    RxBus.getDefault().send(new RxBusEvent(RxBusEvent.REFRESH_ACCOUNT_LIST));
                    finish();
                }
                break;
            case R.id.title_left_image:
                finish();
                break;
            case R.id.add_type_cost_btn:
                accpuntType = KingAccountNode.MONEY_OUT;
                selectType();
                break;
            case R.id.add_type_income_btn:
                accpuntType = KingAccountNode.MONEY_IN;
                selectType();
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
                intent.putExtra(ActivityLib.INTENT_PARAM2, accpuntType);
                intent.putExtra(ActivityLib.INTENT_PARAM3, accountNode.getType());
                startActivityForResult(intent, WhatConstants.ACCOUNT_TYPE.SELECT_TYPE);
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
        accountNode.setAccount_type(accpuntType);
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
                case WhatConstants.ACCOUNT_TYPE.SELECT_TYPE:
                    if (data != null) {
                        int typeCount = data.getIntExtra(ActivityLib.INTENT_PARAM, 0);
                        accountNode.setType(typeCount);
                        presenter.loadType(accpuntType, typeCount);
                    }
                    break;
                default:
                    break;
            }
        }
    }

    public void selectType() {
        LogUtil.d(TAG, "accpuntType=" + accpuntType);
        if (accpuntType == KingAccountNode.MONEY_IN) {
            incomeBtn.setBackgroundResource(R.drawable.search_type_left_bg_press);
            incomeBtn.setTextColor(getResources().getColor(R.color.white));
            costBtn.setBackgroundResource(R.drawable.search_type_bg);
            costBtn.setTextColor(getResources().getColor(R.color.my_color));
        } else if (accpuntType == KingAccountNode.MONEY_OUT) {
            costBtn.setBackgroundResource(R.drawable.search_type_bg_press);
            costBtn.setTextColor(getResources().getColor(R.color.white));
            incomeBtn.setBackgroundResource(R.drawable.search_type_left_bg);
            incomeBtn.setTextColor(getResources().getColor(R.color.my_color));
        }
        mAccount_type_select_tv.setText(TypeUtil.getType(this, accpuntType, 0));
        accountNode.setAccount_type(accpuntType);
    }

    @Override
    public void selectCostType(boolean isSelectType) {

    }

    public void selectIncomeType(boolean isSelectType) {
        LogUtil.d(TAG, "accpuntType=" + accpuntType);

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
    public void loadLocationSuccess(String location) {
        if (TextUtils.isEmpty(location)) {
            attachment.setLocation("");
        } else {
            attachment.setLocation(location);
        }
    }

    @Override
    public void showType(String type) {
        mAccount_type_select_tv.setText(type);
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
