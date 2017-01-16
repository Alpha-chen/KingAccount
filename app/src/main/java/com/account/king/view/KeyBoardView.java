package com.account.king.view;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.account.king.R;
import com.account.king.util.ScreenUtils;

import java.util.ArrayList;
import java.util.List;


public class KeyBoardView extends LinearLayout implements View.OnClickListener {

    //方块宽高比为  5;3

    private Context context;
    public NumClickListener numClickListener;
    private LinearLayout keyHeightView;

    private List<Object> mList2 = new ArrayList<>();

    private static final int decimal = 2;
    private static final int integer = 6;

    private Button okBtn;

    public KeyBoardView(Context context) {
        super(context, null);
    }

    public KeyBoardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public void setNumClickListener(NumClickListener numClickListener) {
        this.numClickListener = numClickListener;
    }

    private void initView(Context context) {
        this.context = context;
        View root = View.inflate(context, R.layout.view_keyboard, null);
        root.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        addView(root);
        root.findViewById(R.id.keyboard_num_1).setOnClickListener(this);
        root.findViewById(R.id.keyboard_num_2).setOnClickListener(this);
        root.findViewById(R.id.keyboard_num_3).setOnClickListener(this);
        root.findViewById(R.id.keyboard_num_4).setOnClickListener(this);
        root.findViewById(R.id.keyboard_num_5).setOnClickListener(this);
        root.findViewById(R.id.keyboard_num_6).setOnClickListener(this);
        root.findViewById(R.id.keyboard_num_7).setOnClickListener(this);
        root.findViewById(R.id.keyboard_num_8).setOnClickListener(this);
        root.findViewById(R.id.keyboard_num_9).setOnClickListener(this);
        root.findViewById(R.id.keyboard_num_0).setOnClickListener(this);
        root.findViewById(R.id.keyboard_num_x).setOnClickListener(this);
        okBtn = (Button) root.findViewById(R.id.keyboard_num_ok);
        okBtn.setOnClickListener(this);
        root.findViewById(R.id.keyboard_num_dian).setOnClickListener(this);
//        root.findViewById(R.id.keyboard_num_add).setOnClickListener(this);
//        root.findViewById(R.id.keyboard_num_sub).setOnClickListener(this);
        keyHeightView = (LinearLayout) root.findViewById(R.id.key_height);
        int width = ScreenUtils.getScreenWidth(context);
        int height = (((width / 4) * 3) / 5) * 4;
        keyHeightView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, height));
    }

    @Override
    public void onClick(View v) {
        if (numClickListener != null) {
            switch (v.getId()) {
                case R.id.keyboard_num_0:
                    input(0);
                    break;
                case R.id.keyboard_num_1:
                    input(1);
                    break;
                case R.id.keyboard_num_2:
                    input(2);
                    break;
                case R.id.keyboard_num_3:
                    input(3);
                    break;
                case R.id.keyboard_num_4:
                    input(4);
                    break;
                case R.id.keyboard_num_5:
                    input(5);
                    break;
                case R.id.keyboard_num_6:
                    input(6);
                    break;
                case R.id.keyboard_num_7:
                    input(7);
                    break;
                case R.id.keyboard_num_8:
                    input(8);
                    break;
                case R.id.keyboard_num_9:
                    input(9);
                    break;
                case R.id.keyboard_num_dian:
                    input(".");
                    break;
                case R.id.keyboard_num_x:   //回退
                    if (mList2.size() != 0) {
                        removeEnd(mList2);
                    }
                    showInput();
                    break;
            }
        }
    }

    /**
     * 1.index=0时不能为0
     * 2."."只能添加一个
     * 3.小数位只能添加2位
     * 4.整数位添加6位
     * 5.添加小数位符不返回
     *
     * @param obj
     */
    private void input(Object obj) {
        //输入 数字或.
        handInput(obj, mList2);
        showInput();
    }

    /**
     * 显示输出
     */
    private void showInput() {
        StringBuffer sb = new StringBuffer("");
        if (mList2.size() != 0) {
            for (Object obj : mList2) {
                sb.append(obj.toString());
            }
        }
        if (TextUtils.isEmpty(sb.toString()) && mList2.size() == 1 && mList2.get(0).toString().equals("0")) {
            numClickListener.numClick("0.00");
        } else {
            numClickListener.numClick(sb.toString());
        }

    }

    /**
     * 处理非运算符输入
     *
     * @param obj
     * @param mList
     */
    private void handInput(Object obj, List<Object> mList) {
        //是否已经存在小数
        boolean isHasdecimal = false;
        int index = 0;
        for (int i = 0; i < mList.size(); i++) {
            String temp = mList.get(i).toString();
            if (temp.equals(".")) {
                isHasdecimal = true;
                index = i;
            }
        }
        //2."."只能添加一个
        if (isHasdecimal && obj.toString().equals(".")) {
            return;
        }
        //3.小数位只能添加2位
        if (isHasdecimal && mList.size() - index - 1 >= decimal) {
            return;
        }
        //4.整数位添加6位
        if (!isHasdecimal && mList.size() >= integer && !obj.toString().equals(".")) {
            return;
        }
        mList.add(obj);
    }

    /**
     * List 转  Float
     *
     * @param mList
     * @return
     */
    private String calculateNum(List<Object> mList) {
        if (mList.size() == 0) {
            return "0";
        }
        StringBuffer sb = new StringBuffer();
        for (Object obj : mList) {
            sb.append(obj);
        }
        String results = sb.toString();
        if (results.startsWith(".")) {
            results = "0" + results;
        }
        if (results.endsWith(".")) {
            results = results + "0";
        }
        return results;
    }


    /**
     * 回退一位
     *
     * @param mList
     */
    private void removeEnd(List<Object> mList) {
        if (mList.size() == 0) {
            return;
        }
        mList.remove(mList.size() - 1);
    }

    /**
     * 增加有小数点时末尾的0
     *
     * @param results
     * @return
     */
    private String addEnd0(String results) {
        if (!results.contains(".")) {
            return results;
        }
        int lastIndex = results.lastIndexOf(".");
        if (results.substring(lastIndex).length() > 3) {
            results = results.substring(0, lastIndex + 3);
        }
        while (results.length() - lastIndex < 3) {
            results = results + "0";
        }
        return results;
    }


    public interface NumClickListener {
        public void numClick(String currentMoney);

        public void okClick();
    }
}
