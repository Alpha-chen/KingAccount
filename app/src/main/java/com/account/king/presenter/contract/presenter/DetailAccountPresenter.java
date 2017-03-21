package com.account.king.presenter.contract.presenter;

import android.content.Context;

import com.account.king.db.storage.KingAccountStorage;
import com.account.king.node.KingAccountNode;
import com.account.king.presenter.contract.DetailAccountContract;

/**
 * Created by King
 * on 2017/3/21.
 */

public class DetailAccountPresenter implements DetailAccountContract.IPresenter {

    private DetailAccountContract.IView mView;
    private Context mContext;
    private KingAccountStorage mAccountStorage;
    private KingAccountNode mAccountNodes;

    public DetailAccountPresenter(DetailAccountContract.IView view, Context context) {
        mView = view;
        mContext = context;
        mAccountStorage = new KingAccountStorage(context);
    }

    @Override
    public void delete(KingAccountNode node) {
        if (mAccountStorage.delete(node)) {
            mView.deleteSuccess();
        }
    }
}
