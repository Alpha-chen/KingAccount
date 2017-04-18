package com.account.king.adapter;

import android.content.Context;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

import com.account.king.R;
import com.account.king.constant.WhatConstants;


/**
 * 引导也的adapter
 */
public class UpgradeVersionGuideAdapter extends PagerAdapter {

    private String TAG = "UpgradeVersionGuideAdapter";
    private int[] imgResourceId;
    private LayoutInflater inflater;
    private Context context;
    private Handler handler;

    public UpgradeVersionGuideAdapter(Context context, int[] imgResourceId, Handler handler) {
        super();
        this.context = context;
        this.imgResourceId = imgResourceId;
        this.handler = handler;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return null != imgResourceId ? imgResourceId.length : 0;
    }

    @Override
    public Object instantiateItem(ViewGroup view, int position) {
        if (null != imgResourceId && imgResourceId.length > 0) {
            View imageLayout = inflater.inflate(
                    R.layout.item_version_pager_image, view, false);
            ImageView imageView = (ImageView) imageLayout
                    .findViewById(R.id.upgrade_version_item_image);
            imageView.setBackgroundResource(imgResourceId[position]);
            imageView.setScaleType(ScaleType.CENTER_CROP);
            view.addView(imageLayout, 0);
            if (position + 1 == imgResourceId.length) {
                Button imageViewSure = (Button) imageLayout
                        .findViewById(R.id.upgrade_version_sure);
                imageViewSure.setVisibility(View.VISIBLE);
                imageViewSure.setOnClickListener(new OnClickListener() {
                    public void onClick(View v) {
                        handler.sendEmptyMessage(WhatConstants.What.UPGRADE_VERSION_START);
                    }
                });
            }
            return imageLayout;
        }
        return null;
    }

    @Override
    public void destroyItem(View arg0, int arg1, Object arg2) {
        ((ViewPager) arg0).removeView((View) arg2);
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0.equals(arg1);
    }
}
