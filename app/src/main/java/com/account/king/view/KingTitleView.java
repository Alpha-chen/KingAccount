package com.account.king.view;

import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.account.king.R;
import com.account.king.SearchActivity;
import com.account.king.StaticsActivity;
import com.account.king.util.ActivityManager;
import com.account.king.util.LogUtil;

/**
 * 顶部的title
 * Created by King
 * on 2016/12/19.
 */

public class KingTitleView extends LinearLayout implements View.OnClickListener {
    private String TAG = "KingTitleView";
    private Context mContext;
    private ImageView leftView;
    private boolean hideLeft = false;

    private LinearLayout titleLay;
    private ImageView titleLeftIv;
    private int titleLeftRes;
    private TextView title;
    private String titleStr;

    private ImageView rightView;
    private int rightViewRes;
    private String rightViewAction;

    private TextView rightText;
    private String rightTextStr;
    private int rightTextBg;

    public KingTitleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.KingTitleView, defStyleAttr, 0);
        if (null != array) {
            titleLeftRes = array.getResourceId(R.styleable.KingTitleView_titleLeftImg, 0);
            titleStr = array.getString(R.styleable.KingTitleView_kingTitle);
            rightViewRes = array.getResourceId(R.styleable.KingTitleView_rightImg, 0);
            rightTextStr = array.getString(R.styleable.KingTitleView_rightText);
            hideLeft = array.getBoolean(R.styleable.KingTitleView_leftBackImg, false);
            rightTextBg = array.getResourceId(R.styleable.KingTitleView_rightTextBg, 0);
            array.recycle();
        }
    }

    public KingTitleView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public KingTitleView(Context context) {
        this(context, null);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.title_view, this);
        leftView = (ImageView) view.findViewById(R.id.title_left_back_img);
        leftView.setOnClickListener(this);
        titleLeftIv = (ImageView) view.findViewById(R.id.title_left_img);
        title = (TextView) view.findViewById(R.id.title);

        rightView = (ImageView) view.findViewById(R.id.title_right_img);
        rightView.setOnClickListener(this);

        rightText = (TextView) view.findViewById(R.id.title_right_text);
        rightText.setOnClickListener(this);
        rendView();

    }

    public void rendView() {
        LogUtil.d(TAG, "hideLeft=" + hideLeft);
        if (hideLeft) {
            leftView.setVisibility(GONE);
        } else {
            leftView.setVisibility(VISIBLE);
        }
        if (!TextUtils.isEmpty(titleStr)) {
            title.setText(titleStr);
        }
        if (0 != titleLeftRes) {
            titleLeftIv.setVisibility(VISIBLE);
            titleLeftIv.setImageResource(titleLeftRes);
        } else {
            titleLeftIv.setVisibility(GONE);
        }

        if (0 != rightViewRes) {
            rightView.setVisibility(VISIBLE);
            rightView.setImageResource(rightViewRes);
        } else {
            rightView.setVisibility(GONE);
        }
        if (TextUtils.isEmpty(rightTextStr)) {
            rightText.setVisibility(GONE);
        } else {
            rightText.setVisibility(VISIBLE);
            rightText.setText(rightTextStr);
        }
        if (0 != rightTextBg) {
            rightText.setVisibility(VISIBLE);
            rightText.setBackgroundResource(rightTextBg);
        } else {
            rightText.setVisibility(GONE);
        }
    }


    public void setTitleLeftRes(int titleLeftRes) {
        this.titleLeftRes = titleLeftRes;
        rendView();
    }

    public void setTitleStr(String titleStr) {
        this.titleStr = titleStr;
        rendView();
    }

    public void setRightViewRes(int rightViewRes) {
        this.rightViewRes = rightViewRes;
        rendView();
    }

    public void setRightViewAction(String rightViewAction) {
        this.rightViewAction = rightViewAction;
        rendView();
    }

    public void setRightText(TextView rightText) {
        this.rightText = rightText;
        rendView();
    }

    public void setRightTextStr(String rightTextStr) {
        this.rightTextStr = rightTextStr;
        rendView();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_left_back_img: // 退出
                ActivityManager.getInstance().getLastActivity().finish();
                break;
            case R.id.title_right_text: // 右边文字的点击事件
                Intent intent1 = new Intent(mContext, StaticsActivity.class);
                mContext.startActivity(intent1);
                break;
            case R.id.title_right_img: // 右边按钮的点击事件
                Intent intent = new Intent(mContext, SearchActivity.class);
                mContext.startActivity(intent);
                break;
            default:
                break;
        }
    }

}
