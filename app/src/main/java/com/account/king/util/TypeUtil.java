package com.account.king.util;

import android.content.Context;
import android.text.TextUtils;

import com.account.king.R;
import com.account.king.node.KingAccountNode;
import com.account.king.node.TypeNode;

import java.util.ArrayList;

/**
 * Created by King
 * on 2017/4/25.
 */

public class TypeUtil {

    public static String getType(Context context, int accountType, int type) {
        String[] arrays = null;
        if (KingAccountNode.MONEY_OUT == accountType) {
            String out = SPUtils.getString(context, SPUtils.MONEY_TYPE_OUT);
            if (TextUtils.isEmpty(out)) {
                arrays = context.getResources().getStringArray(R.array.account_outcome_type);
                return arrays[type];
            } else {
                ArrayList<TypeNode> typeNodes = new ArrayList<>();
                typeNodes = (ArrayList<TypeNode>) KingJson.parseArray(out, TypeNode.class);
                return type >= typeNodes.size() ?  typeNodes.get(0).getName() :typeNodes.get(type).getName();

            }
        } else if (KingAccountNode.MONEY_IN == accountType) {
            String in = SPUtils.getString(context, SPUtils.MONEY_TYPE_IN);
            if (TextUtils.isEmpty(in)) {
                arrays = context.getResources().getStringArray(R.array.account_income_type);
                return arrays[type];
            } else {
                ArrayList<TypeNode> typeNodes = new ArrayList<>();
                typeNodes = (ArrayList<TypeNode>) KingJson.parseArray(in, TypeNode.class);
                return type >= typeNodes.size() ?  typeNodes.get(0).getName() :typeNodes.get(type).getName();
            }
        }
        return arrays[type];
    }

}
