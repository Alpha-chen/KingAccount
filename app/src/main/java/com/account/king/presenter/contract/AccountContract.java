package com.account.king.presenter.contract;

import com.account.king.node.KingAccountNode;

import java.util.ArrayList;

/**
 * 首页 列表界面
 * Created by King
 * on 2017/2/22.
 */

public class AccountContract {

    public interface IView{
        void selectAccountSuccess(ArrayList<KingAccountNode> accountNodes);
        void seletcAccountFailure();
    }
    public interface IPresenter{
        void selectAllAccount();
        void selectByDate(int date);
    }
}
