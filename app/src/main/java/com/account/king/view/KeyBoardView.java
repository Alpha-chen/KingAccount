package com.account.king.view;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.umeng.analytics.MobclickAgent;

import net.ffrj.pinkwallet.R;
import net.ffrj.pinkwallet.util.ArithUtil;
import net.ffrj.pinkwallet.util.ScreenUtils;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by lcp on 2016/6/2.
 */
public class KeyBoardView extends LinearLayout implements View.OnClickListener{

    //方块宽高比为  5;3

    private Context context ;
    public NumClickListener numClickListener ;
    private LinearLayout keyHeightView;

    private List<Object> mList = new ArrayList<>();
    private String operator ;//运算符
    private List<Object> mList2 = new ArrayList<>();

    private static final int decimal = 2;
    private static final int integer = 6;

    private Button okBtn;

    public KeyBoardView(Context context) {
        super(context,null);
    }

    public KeyBoardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public void setNumClickListener(NumClickListener numClickListener){
        this.numClickListener = numClickListener;
    }

    private void initView(Context context){
        this.context = context;
        View root = View.inflate(context, R.layout.view_keyboard,null);
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
        root.findViewById(R.id.keyboard_num_add).setOnClickListener(this);
        root.findViewById(R.id.keyboard_num_sub).setOnClickListener(this);
        keyHeightView = (LinearLayout) root.findViewById(R.id.key_height);
        int width = ScreenUtils.getScreenWidth(context);
        int height = (((width/4)*3)/5)*4;
        keyHeightView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,height));
    }

    @Override
    public void onClick(View v) {
        if(numClickListener != null){
            switch (v.getId()){
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
                case R.id.keyboard_num_add:
                    MobclickAgent.onEvent(context,"keyboard_add");
                    input("+");
                    break;
                case R.id.keyboard_num_sub:
                    MobclickAgent.onEvent(context,"keyboard_sub");
                    input("-");
                    break;
                case R.id.keyboard_num_x:   //回退
                    if(mList2.size() != 0){
                        removeEnd(mList2);
                    }else{
                        //删除操作符
                        if(!TextUtils.isEmpty(operator)){
                            operator = "";
                            okBtn.setText("ok");
                        }else{
                            removeEnd(mList);
                        }
                    }
                    showInput();
                    break;
                case R.id.keyboard_num_ok:
                    if(okBtn.getText().toString().equals("=")){
                        String num1 = calculateNum(mList);
                        String num2 = calculateNum(mList2);
                        mList.clear();
                        mList2.clear();
                        String result ;
                        if(operator.equals("+")){
                            result = ArithUtil.addSpit(num1,num2);
                        }else{
                            result = ArithUtil.sub(num1,num2,2)+"";
                        }
                        if(Float.parseFloat(result) == 0){
                            mList.add(0);
                        }else{
                            String results = result + "";
                            results = addEnd0(results);
                            for(char c : results.toCharArray()){
                                mList.add(c);
                            }

                        }
                        operator = "";
                        okBtn.setText("ok");
                        showInput();
                    }else{
                        numClickListener.okClick();
                    }
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
     * @param obj
     */
    private void input(Object obj){
        if(obj.toString().equals("+") || obj.toString().equals("-")){   //输入操作符
            // 只有一个负号时不允许输入 操作符
            if(mList.size() == 1 && mList.get(0).toString().equals("-")){
                return;
            }
            okBtn.setText("=");
            if(TextUtils.isEmpty(operator)){    //添加运算符
                operator = obj.toString();
                if(mList.size() == 0){
                    mList.add(0);
                }
            }else{
                if(mList2.size() == 0){
                    operator = obj.toString();
                }else{      //先做运算再添加运算符
                    String num1 = calculateNum(mList);
                    String num2 = calculateNum(mList2);
                    mList.clear();
                    mList2.clear();
                    String result ;
                    if(operator.equals("+")){
                        result = ArithUtil.addSpit(num1,num2);
                    }else{
                        result = (float) ArithUtil.sub(num1,num2,2) +"";
                    }
                    if(Float.parseFloat(result) == 0){
                        mList.add(0);
                    }else{
                        String results = result + "";
                        results = addEnd0(results);
                        for(char c : results.toCharArray()){
                            mList.add(c);
                        }
                    }
                    operator = obj.toString();
                }
            }
        }else{ //输入 数字或.
            if(TextUtils.isEmpty(operator)){
                handInput(obj,mList);
            }else{
                handInput(obj,mList2);
            }
        }
        showInput();
    }

    /**
     * 显示输出
     */
    private void showInput(){
        StringBuffer sb = new StringBuffer("");
        if(mList.size() != 0){
            for(Object obj : mList){
                sb.append(obj.toString());
            }
        }
        if(!TextUtils.isEmpty(operator)){
            sb.append(operator);
            if(mList2.size() != 0){
                for(Object obj : mList2){
                    sb.append(obj.toString());
                }
            }
        }
        if(TextUtils.isEmpty(sb.toString()) || (TextUtils.isEmpty(operator) && mList.size() == 1 && mList.get(0).toString().equals("0"))){
            numClickListener.numClick("0.00");
        }else{
            numClickListener.numClick(sb.toString());
        }

    }

    /**
     * 处理非运算符输入
     * @param obj
     * @param mList
     */
    private void handInput(Object obj, List<Object> mList){
        //是否已经存在小数
        boolean isHasdecimal = false ;
        int index = 0;
        for(int i = 0 ; i < mList.size() ; i++){
            String temp = mList.get(i).toString();
            if(temp.equals(".")){
                isHasdecimal = true ;
                index = i ;
            }
        }
        //2."."只能添加一个
        if(isHasdecimal && obj.toString().equals(".")){
            return;
        }
        //3.小数位只能添加2位
        if(isHasdecimal && mList.size() - index -1 >= decimal){
            return;
        }
        //4.整数位添加6位
        if(!isHasdecimal && mList.size() >= integer && !obj.toString().equals(".")){
            return;
        }
        // 只有一个负号时不允许输入 小数点
        if(mList.size() == 1 && mList.get(0).toString().equals("-") && obj.toString().equals(".")){
            return;
        }
        mList.add(obj);
    }

    /**
     * List 转  Float
     * @param mList
     * @return
     */
    private String calculateNum(List<Object> mList){
        if(mList.size() == 0){
            return "0";
        }
        StringBuffer sb = new StringBuffer();
        for(Object obj : mList){
            sb.append(obj);
        }
        String results = sb.toString();
        if(results.startsWith(".")){
            results = "0" + results;
        }
        if(results.endsWith(".")){
            results = results + "0";
        }
        return results;
    }


    /**
     * 回退一位
     * @param mList
     */
    private void removeEnd(List<Object> mList){
        if(mList.size() == 0){
            return;
        }
        mList.remove(mList.size()-1);
    }

    /**
     * 增加有小数点时末尾的0
     * @param results
     * @return
     */
    private String addEnd0(String results){
        if(!results.contains(".")){
            return results;
        }
        int lastIndex = results.lastIndexOf(".");
        if(results.substring(lastIndex).length() > 3){
            results = results.substring(0,lastIndex + 3);
        }
        while (results.length() - lastIndex < 3){
            results = results + "0";
        }
        return results;
    }


    public interface NumClickListener{
        public void numClick(String currentMoney);
        public void okClick();
    }
}