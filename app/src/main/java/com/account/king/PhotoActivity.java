package com.account.king;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.account.king.util.ActivityLib;
import com.account.king.util.glide.GlideUtil;


/**
 * @author King
 */
public class PhotoActivity extends BaseActivity implements View.OnClickListener {


    private ImageView icon;
    private String path;
    private boolean isDelete = true;//有删除按钮

    private RelativeLayout deleteBtn;

    private GestureDetector gestureDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initView();
        initIntent();
        updateViewData();

        gestureDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                finish();
                return super.onSingleTapUp(e);
            }
        });
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_photo;
    }

    @Override
    public void initView() {
        super.initView();
        findViewById(R.id.title_left).setOnClickListener(this);
        deleteBtn = (RelativeLayout) findViewById(R.id.title_right);
        deleteBtn.setOnClickListener(this);
        icon = (ImageView) findViewById(R.id.icon);
    }

    @Override
    public void initIntent() {
        super.initIntent();
        Intent data = getIntent();
        path = data.getStringExtra(ActivityLib.INTENT_PARAM);
        isDelete = data.getBooleanExtra(ActivityLib.INTENT_PARAM2, true);
    }

    @Override
    public void updateViewData() {
        super.updateViewData();
        if (!isDelete) {
            deleteBtn.setVisibility(View.GONE);
        }
        GlideUtil.loadFitCenter(this, path, icon);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_left:
                finish();
                break;
            case R.id.title_right:
                setResult(Activity.RESULT_OK);
                finish();
                break;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return gestureDetector.onTouchEvent(event);
    }
}
