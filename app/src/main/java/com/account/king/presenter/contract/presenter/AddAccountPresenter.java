package com.account.king.presenter.contract.presenter;

import android.Manifest;
import android.animation.Keyframe;
import android.animation.PropertyValuesHolder;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.account.king.NoteActivity;
import com.account.king.PhotoActivity;
import com.account.king.R;
import com.account.king.callback.AMapLocationCallBack;
import com.account.king.constant.WhatConstants;
import com.account.king.db.storage.KingAccountStorage;
import com.account.king.manager.AMapLocationManager;
import com.account.king.node.KingAccountNode;
import com.account.king.presenter.contract.AddAccountContract;
import com.account.king.rxevent.RxBus;
import com.account.king.rxevent.RxBusEvent;
import com.account.king.util.ActivityLib;
import com.account.king.util.CalendarUtil;
import com.account.king.util.PermissionUtil;
import com.account.king.util.TypeUtil;
import com.account.king.util.glide.GlideUtil;
import com.account.king.view.dialog.CalendarDialog;
import com.amap.api.location.AMapLocation;

import java.util.HashMap;
import java.util.List;

import pink.net.multiimageselector.utils.ImageSelector;
import pink.net.multiimageselector.utils.MultiSelectorUtils;


/**
 * @author king
 */
public class AddAccountPresenter implements AddAccountContract.IAddAcountPresenter, AMapLocationCallBack {


    private int REQUEST_CODE_ASK_IMAGE_PHONE = 123;
    private AddAccountContract.IAddAcountView addAcountView;
    private Context mContext;
    private AMapLocationManager aMapLocationManager;
    // 四个直辖市
    private HashMap<String, String> municipalitys = new HashMap<>();
    private TextView locationView;
    private ImageView locationIcon;

    public AddAccountPresenter(AddAccountContract.IAddAcountView addAcountView, Context context) {
        this.addAcountView = addAcountView;
        this.mContext = context;
        initLocation();
    }

    private void initLocation() {
        municipalitys.put("010", "北京");
        municipalitys.put("021", "上海");
        municipalitys.put("022", "天津");
        municipalitys.put("023", "重庆");
        aMapLocationManager = new AMapLocationManager(mContext);
    }

    @Override
    public void loadType(int accountType, int type) {
        addAcountView.showType(TypeUtil.getType(mContext,accountType,type));
    }

    @Override
    public List<KingAccountNode> getTypeNodes(Context context) {
        return null;
    }

    @Override
    public boolean validation(Context context, KingAccountNode bookNode, String price, String count) {
     /*   try {
            float pricef = Float.parseFloat(price);
            float countf = Float.parseFloat(count);
            float moneyf = (float) ArithUtil.mul(pricef, countf);
            bookNode.setMoney(moneyf);
            if (bookNode.getMoney() <= 0) {
                jitterMoney();
                return false;
            }
            if (TextUtils.isEmpty("")) {
                ToastUtil.makeToast(context, context.getString(R.string.type_error));
                return false;
            }
            return true;
        } catch (NumberFormatException e) {
            jitterMoney();
            return false;
        }*/
        return false;
    }

    private void jitterMoney() {
        PropertyValuesHolder pvhRotate = PropertyValuesHolder.ofKeyframe(View.ROTATION,
                Keyframe.ofFloat(0f, 0f),
                Keyframe.ofFloat(.1f, -3f),
                Keyframe.ofFloat(.2f, -3f),
                Keyframe.ofFloat(.3f, 3f),
                Keyframe.ofFloat(.4f, -3f),
                Keyframe.ofFloat(.5f, 3f),
                Keyframe.ofFloat(.6f, -3f),
                Keyframe.ofFloat(.7f, 3f),
                Keyframe.ofFloat(.8f, -3f),
                Keyframe.ofFloat(.9f, 3f),
                Keyframe.ofFloat(1f, 0)
        );
        addAcountView.jitterMoney(pvhRotate);
    }

    @Override
    public boolean saveBookNode(Context context, boolean isEdit, KingAccountNode bookNode) {
        boolean result = false;
        if (!isEdit) {
            result = insertBookNode(context, bookNode);
            RxBus.getDefault().send(new RxBusEvent(RxBusEvent.ADD_ACCOUNT, bookNode));
        } else {
            result = updateBookNode(context, bookNode);
            RxBus.getDefault().send(new RxBusEvent(RxBusEvent.UPDATE_ACCOUNT, bookNode));
        }
        return result;
    }

    @Override
    public boolean insertBookNode(Context context, final KingAccountNode bookNode) {
        final KingAccountStorage bookStorage = new KingAccountStorage(context);
        boolean result = bookStorage.create(bookNode);
        return result;
    }

    @Override
    public boolean updateBookNode(Context context, KingAccountNode bookNode) {
        KingAccountStorage bookStorage = new KingAccountStorage(context);
        bookStorage.update(bookNode);
        return true;
    }

    @Override
    public void loadImg(Context context, String path, ImageView selectPhotoImg) {
        if (TextUtils.isEmpty(path)) {
            selectPhotoImg.setImageResource(R.drawable.ic_add_photo);
        } else {
            GlideUtil.loadRound(context, path, selectPhotoImg);
        }
    }

    @Override
    public void loadNote(Context context, String note) {
        if (!TextUtils.isEmpty(note)) {
            addAcountView.setNoteRes(0, note);
        } else {
            addAcountView.setNoteRes(R.drawable.ic_pencil, note);
        }
    }

    @Override
    public void selectDate(Context context, final KingAccountNode bookNode) {
        final CalendarDialog calendarDialog = new CalendarDialog(context);
        calendarDialog.setDate(CalendarUtil.timeMilis2Date(bookNode.getYmd_hms()));
        calendarDialog.setOnSelectDateListener(new CalendarDialog.OnSelectDateListener() {
            @Override
            public void okDate(long date) {
                calendarDialog.dismiss();
                bookNode.setYmd_hms(date);
                addAcountView.setDateText(CalendarUtil.getStringMD(CalendarUtil.timeMilis2Date(date)));
            }
        });
        PermissionUtil.getAlertPermission(context, calendarDialog);
        //calendarDialog.show();
    }

    @Override
    public void selectPhoto(Activity context, KingAccountNode bookNode) {
        if (null != bookNode.getAttachment() && !TextUtils.isEmpty(bookNode.getAttachment().getAttachment_path())) {
            Intent data = new Intent(context, PhotoActivity.class);
            data.putExtra(ActivityLib.INTENT_PARAM, bookNode.getAttachment().getAttachment_path());
            context.startActivityForResult(data, WhatConstants.Refresh.PHOTO_DELETE);
        } else {
            if (Build.VERSION.SDK_INT >= 23) {
                int checkCallPhonePermission = ContextCompat.checkSelfPermission(mContext, Manifest.permission.CALL_PHONE);
                if (checkCallPhonePermission != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions((Activity) mContext, new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CODE_ASK_IMAGE_PHONE);
                    return;
                }
            }
            MultiSelectorUtils.selectImage(context,
                    new ImageSelector.Builder().editMode(2).build());
        }
    }

    @Override
    public void selectType(Activity context, KingAccountNode bookNode) {
    }

    @Override
    public void clickWriterNote(final Activity context, final KingAccountNode bookNode) {
        Intent data = new Intent(context, NoteActivity.class);
        if (null == bookNode.getAttachment()) {
            data.putExtra(ActivityLib.INTENT_PARAM, "");
        } else {
            data.putExtra(ActivityLib.INTENT_PARAM, bookNode.getAttachment().getContent());
        }
        context.startActivityForResult(data, WhatConstants.Refresh.ACCOUNT_INPUT_NOTE);
    }

    @Override
    public void onLocationSuccess(AMapLocation aMapLocation) {
        locationView.setVisibility(View.VISIBLE);
        // 如果是四个直辖市，就选择直辖市,区;相反就是显示省,市;保持与服务器返回数据保持一致
        if (!TextUtils.isEmpty(municipalitys.get(aMapLocation.getCityCode()))) {
            locationView.setText(aMapLocation.getCity() + aMapLocation.getDistrict());
            addAcountView.loadLocationSuccess(aMapLocation.getCity() + aMapLocation.getDistrict());
        } else {
            locationView.setText(aMapLocation.getCity() + aMapLocation.getDistrict());
            addAcountView.loadLocationSuccess(aMapLocation.getCity() + aMapLocation.getDistrict());
        }
        locationIcon.setImageResource(R.drawable.ic_place_blue);
    }

    @Override
    public void onLocationFailed(int errorCode, String errorMessage) {
        locationView.setVisibility(View.GONE);
        addAcountView.loadLocationSuccess("");
        locationView.setText(mContext.getResources().getString(R.string.sns_location_failed));
        locationView.setTextColor(mContext.getResources().getColor(R.color.light_gray));
        locationIcon.setImageResource(R.drawable.ic_place_gray);
    }

    @Override
    public void loadLocation(TextView locationView, String location) {
        if (TextUtils.isEmpty(location)) {
            locationView.setVisibility(View.GONE);
        } else {
            locationView.setVisibility(View.VISIBLE);
            locationView.setText(location);
        }
    }

    @Override
    public void getAccountLocation(final TextView locationView, final ImageView locationIcon) {
        this.locationIcon = locationIcon;
        this.locationView = locationView;
        aMapLocationManager.setAMapLocationCallBack(this);
        aMapLocationManager.startOnceLocation();
    }


    @Override
    public void onItemClick(Context context, RecyclerView.ViewHolder vh, int type, List<KingAccountNode> typeNodes, ImageView moveTypeIcon, ImageView icon) {

    }


}
