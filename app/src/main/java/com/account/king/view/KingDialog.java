package com.account.king.view;

import android.app.Dialog;
import android.content.Context;
import android.view.View;

/**
 * Created by King
 * on 2016/12/19.
 */

public class KingDialog extends Dialog implements View.OnClickListener {


    public KingDialog(Context context) {
        super(context);
    }

    public KingDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    protected KingDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }


    @Override
    public void onClick(View v) {

    }
}
