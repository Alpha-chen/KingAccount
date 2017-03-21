package com.account.king.presenter.contract;

import com.account.king.node.KingAccountNode;

/**
 * Created by King
 * on 2017/3/21.
 */

public class DetailAccountContract  {
    public interface IView {
        void deleteSuccess();
    }

    public interface IPresenter {
        void delete(KingAccountNode node);
    }

}
