package com.account.king.presenter.contract;

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

    }

    public interface IPresenter {

        public void select(int accountType, int start, int end, int type, String content);

    }
}
