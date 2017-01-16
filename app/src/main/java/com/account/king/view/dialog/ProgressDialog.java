package com.account.king.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import com.account.king.R;


/**
 * @author King
 */
public class ProgressDialog extends Dialog {


    public ProgressDialog(Context context) {
        this(context, R.style.dialog_transparent);
    }

    public ProgressDialog(Context context, int themeResId) {
        super(context, R.style.dialog_transparent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_progress);
    }
}
