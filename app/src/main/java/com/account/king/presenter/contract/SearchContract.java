package com.account.king.presenter.contract;

import android.content.Context;

import com.account.king.node.KingAccountNode;

import java.util.ArrayList;

/**
 * 搜索
 * Created by King
 * on 2017/4/21.
 */

public class SearchContract {

    public interface IView {
        public void selectSucces(ArrayList<KingAccountNode> arrayList);

        public void selectFailure();
        public void setDateText(long  date);
    }

    public interface IPresenter {

        public void select(int accountType, long start, long end, int type, String content);

        public void selectDate(Context context, long selectDate);
    }
}
