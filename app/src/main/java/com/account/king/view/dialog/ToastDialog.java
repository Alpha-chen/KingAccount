package com.account.king.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.account.king.R;


/**
 * @author king
 */
public class ToastDialog extends Dialog implements View.OnClickListener {


    private TextView hintText;
    private String hint;
    private int type = 0; //type=0  single || type=1 double btn
    private Context context;

    private RelativeLayout singleRela;
    private RelativeLayout doubleRela;
    private View.OnClickListener clickListener;

    public ToastDialog(Context context) {
        this(context, R.style.dialog_transparent);
    }

    public ToastDialog(Context context, int themeResId) {
        super(context, themeResId);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_toast);

        initView();
        updateViewData();
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setHintText(String msg) {
        this.hint = msg;
    }

    public void setHintText(int resId) {
        this.hint = context.getResources().getString(resId);
    }

    public void setOnclick(View.OnClickListener clickListener) {
        this.clickListener = clickListener;
    }

    private void initView() {
        hintText = (TextView) findViewById(R.id.toast_hint);
        findViewById(R.id.dialog_ok).setOnClickListener(this);
        findViewById(R.id.dialog_ok2).setOnClickListener(this);
        findViewById(R.id.dialog_cancle).setOnClickListener(this);
        singleRela = (RelativeLayout) findViewById(R.id.content_down_single);
        doubleRela = (RelativeLayout) findViewById(R.id.content_down);
    }

    private void updateViewData() {
        hintText.setText(hint);
        if (type == 0) {
            singleRela.setVisibility(View.VISIBLE);
            doubleRela.setVisibility(View.GONE);
        } else {
            singleRela.setVisibility(View.GONE);
            doubleRela.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.dialog_ok:
                dismiss();
                break;
            case R.id.dialog_ok2:
                if (clickListener != null) {
                    clickListener.onClick(v);
                }
                dismiss();
                break;
            case R.id.dialog_cancle:
                dismiss();
                break;
        }
    }
}
