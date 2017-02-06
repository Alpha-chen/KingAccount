package com.account.king;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.EditText;
import android.widget.TextView;

import com.account.king.util.ActivityLib;


/**
 * @author King
 */
public class NoteActivity extends BaseActivity implements TextWatcher, View.OnClickListener {

    private EditText editText;
    private TextView lengthText;

    private String note;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initTitleBar();
        initView();
        initIntent();
        initViewData();
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_note;
    }

    @Override
    public void initTitleBar() {
        super.initTitleBar();

    }

    @Override
    public void initView() {
        super.initView();
        editText = (EditText) findViewById(R.id.book_edit);
        lengthText = (TextView) findViewById(R.id.tv_length);
        editText.addTextChangedListener(this);
        findViewById(R.id.title_left_image).setOnClickListener(this);
        findViewById(R.id.add_account_done).setOnClickListener(this);
    }

    @Override
    public void initIntent() {
        super.initIntent();
        Intent data = getIntent();
        note = data.getStringExtra(ActivityLib.INTENT_PARAM);
    }

    @Override
    public void initViewData() {
        super.initViewData();
        editText.setText(note);
        if (!TextUtils.isEmpty(note)) {
            editText.setSelection(note.length());
        }
        setLengthText(note);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        setLengthText(s.toString());
    }

    private void setLengthText(String note) {
        int length = 0;
        if (!TextUtils.isEmpty(note)) {
            length = note.length();
        }
        lengthText.setText("(" + length + "/100)");
        lengthText.setTextColor(getResources().getColor(R.color.my_color));
        if (length > 100) {
            toastMoreLength();
            lengthText.setTextColor(getResources().getColor(R.color.my_color));
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_left_image:
                finish();
                break;
            case R.id.add_account_done:
                String note = editText.getText().toString();
                if (!TextUtils.isEmpty(note) && note.length() > 100) {
                    toastMoreLength();
                    return;
                }
                Intent data = new Intent();
                data.putExtra(ActivityLib.INTENT_PARAM, note);
                setResult(Activity.RESULT_OK, data);
                finish();
                break;
        }
    }

    private void toastMoreLength() {
        TranslateAnimation animation = new TranslateAnimation(-20, 20, 0, 0);
        animation.setDuration(200);
        animation.setRepeatCount(2);
        animation.setRepeatMode(Animation.REVERSE);
        lengthText.startAnimation(animation);
    }
}
