package com.account.king.util;

import android.content.Context;

import com.account.king.R;
import com.account.king.node.KingAccountNode;

/**
 * Created by King
 * on 2017/4/25.
 */

public class TypeUtil {

    public static String getType(Context context,int accountType, int type) {
        String[] arrays = null;
        if (KingAccountNode.MONEY_OUT == accountType) {
            arrays = context.getResources().getStringArray(R.array.account_outcome_type);
        } else if (KingAccountNode.MONEY_IN == accountType) {
            arrays = context.getResources().getStringArray(R.array.account_income_type);
        }
        return arrays[type];
    }

}
