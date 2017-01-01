package com.account.king;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.account.king.node.KingAccountNode;


public class AddAccountActivity extends BaseActivity implements View.OnClickListener {
    //关闭动画部分机型在style中设置无效
    protected int activityCloseEnterAnimation;
    protected int activityCloseExitAnimation;
    private TextView costBtn, incomeBtn;
    private int type = KingAccountNode.MONEY_OUT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_add_account;
    }

    @Override
    public void initView() {
        super.initView();
        activityCloseEnterAnimation = android.R.anim.fade_in;
        activityCloseExitAnimation = R.anim.activity_push_bottom_out;
        findViewById(R.id.title_left_image).setOnClickListener(this);

        costBtn = (TextView) findViewById(R.id.add_type_cost_btn);
        incomeBtn = (TextView) findViewById(R.id.add_type_income_btn);
        costBtn.setOnClickListener(this);
        incomeBtn.setOnClickListener(this);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(activityCloseEnterAnimation, activityCloseExitAnimation);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_left_image:
                finish();
                break;
            case R.id.add_type_cost_btn:
                selectCostType(true);
                break;
            case R.id.add_type_income_btn:
                selectIncomeType(true);
                break;
            default:
                break;
        }
    }


    public void selectCostType(boolean isSelectType) {
        if (type != KingAccountNode.MONEY_OUT || !isSelectType) {
            type = KingAccountNode.MONEY_OUT;
            costBtn.setBackgroundResource(R.drawable.add_count_type_select);
            costBtn.setTextColor(getResources().getColor(R.color.my_color));
            incomeBtn.setBackgroundDrawable(null);
            incomeBtn.setTextColor(getResources().getColor(R.color.white));
//            dragTypeAdapter.setParams(isTypeEdit,costTypeNodes);
//            moneyText.setTextColor(getResources().getColor(R.color.color5));
//            if (isSelectType) {
//                selectFirst();
//            }
        }
    }

    //选中第一个类别
 /*   private void selectFirst() {
        if (type == NodeUtil.MONEY_OUT) {
            if (!ActivityLib.isEmpty(costTypeNodes)) {
                selectTypeNode(costTypeNodes.get(0));
            }
        } else {
            if (!ActivityLib.isEmpty(incomeTypeNodes)) {
                selectTypeNode(incomeTypeNodes.get(0));
            }
        }
    }*/

    public void selectIncomeType(boolean isSelectType) {
        if (type != KingAccountNode.MONEY_IN || !isSelectType) {
            type = KingAccountNode.MONEY_IN;
            costBtn.setBackgroundDrawable(null);
            costBtn.setTextColor(getResources().getColor(R.color.white));
            incomeBtn.setBackgroundResource(R.drawable.add_count_type_select);
            incomeBtn.setTextColor(getResources().getColor(R.color.my_color));
//            dragTypeAdapter.setParams(isTypeEdit,incomeTypeNodes);
//            moneyText.setTextColor(getResources().getColor(R.color.color6));
//            if (isSelectType) {
//                selectFirst();
//            }
        }
    }
}
