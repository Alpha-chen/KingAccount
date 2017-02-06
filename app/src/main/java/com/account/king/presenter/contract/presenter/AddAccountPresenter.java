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

import com.account.king.NoteActivity;
import com.account.king.PhotoActivity;
import com.account.king.R;
import com.account.king.constant.WhatConstants;
import com.account.king.db.storage.KingAccountStorage;
import com.account.king.node.KingAccountNode;
import com.account.king.presenter.contract.AddAccountContract;
import com.account.king.rxevent.RxBus;
import com.account.king.rxevent.RxBusEvent;
import com.account.king.util.ActivityLib;
import com.account.king.util.CalendarUtil;
import com.account.king.util.PermissionUtil;
import com.account.king.util.glide.GlideUtil;
import com.account.king.view.dialog.CalendarDialog;

import java.util.List;

import pink.net.multiimageselector.utils.ImageSelector;
import pink.net.multiimageselector.utils.MultiSelectorUtils;


/**
 * @author king
 */
public class AddAccountPresenter implements AddAccountContract.IAddAcountPresenter {

    private int REQUEST_CODE_ASK_IMAGE_PHONE = 123;
    private AddAccountContract.IAddAcountView addAcountView;
    private Context mContext;

    public AddAccountPresenter(AddAccountContract.IAddAcountView addAcountView, Context context) {
        this.addAcountView = addAcountView;
        this.mContext = context;
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
    public void onItemClick(Context context, RecyclerView.ViewHolder vh, int type, List<KingAccountNode> typeNodes, ImageView moveTypeIcon, ImageView icon) {

    }


  /*
    private void showSelectAnim(Context context, RecyclerView.ViewHolder vh, final ImageView moveTypeIcon, ImageView icon
            , final AccountTypeNode node) {
        ImageView typeIconImg = ((BaseViewHolder) vh).getView(R.id.type_icon);
        //获取坐标
        int[] location = new int[2];
        typeIconImg.getLocationOnScreen(location);
        int status = ScreenUtils.getStatusHeight(context);
        int top = DensityUtils.dp2px(context, 50);
        moveTypeIcon.setVisibility(View.VISIBLE);
        moveTypeIcon.setX(location[0]);
        moveTypeIcon.setY(location[1] - status - top);
        moveTypeIcon.setImageDrawable(typeIconImg.getDrawable());
        ObjectAnimator moveX = ObjectAnimator.ofFloat(moveTypeIcon, "x", location[0], icon.getX());
        ObjectAnimator moveY = ObjectAnimator.ofFloat(moveTypeIcon, "y", location[1] - status - top, icon.getY());
        AnimatorSet animSet = new AnimatorSet();
        animSet.setDuration(300);
        animSet.setInterpolator(new LinearInterpolator());
        animSet.playTogether(moveX, moveY);
        animSet.start();
        animSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                addAcountView.selectTypeNode(node);
                moveTypeIcon.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }

    @Override
    public void sortTypeNodes(Context context, List<AccountTypeNode> costTypeNodes, List<AccountTypeNode> incomeTypeNodes) {
        AccountTypeStorage typeStorage = new AccountTypeStorage(context);
        if (costTypeNodes != null) {
            for (int i = 0; i < costTypeNodes.size() - 1; i++) {
                AccountTypeNode typeNode = costTypeNodes.get(i);
                if (typeNode != null && typeNode.getSort() != i) {
                    typeNode.setSort(i);
                    typeStorage.updateSort(typeNode);
                }
            }
        }
        if (incomeTypeNodes != null) {
            for (int i = 0; i < incomeTypeNodes.size() - 1; i++) {
                AccountTypeNode typeNode = incomeTypeNodes.get(i);
                if (typeNode != null && typeNode.getSort() != i) {
                    typeNode.setSort(i);
                    typeStorage.updateSort(typeNode);
                }
            }
        }
    }*/
}
