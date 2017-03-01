package com.account.king.presenter.contract.presenter;

import android.content.Context;

import com.account.king.db.storage.KingAccountStorage;
import com.account.king.node.KingAccountNode;
import com.account.king.presenter.contract.AccountContract;

import java.util.ArrayList;

/**
 * Created by King
 * on 2017/2/22.
 */

public class AccountPresenter implements AccountContract.IPresenter {

    private AccountContract.IView mView;
    private Context mContext;
    private KingAccountStorage mAccountStorage;
    private ArrayList<KingAccountNode> mAccountNodes;
    public AccountPresenter(Context context, AccountContract.IView view) {
        mContext = context;
        mView = view;
        initStorage();
    }

    private void initStorage() {
        mAccountStorage = new KingAccountStorage(mContext);
    }

    @Override
    public void selectAllAccount() {
        mAccountNodes = mAccountStorage.queryAll();
        if (null!=mAccountNodes && mAccountNodes.size()>0){
            mView.selectAccountSuccess(mAccountNodes);
        }else {
            mView.seletcAccountFailure();
        }
    }

    @Override
    public void selectByDate(int date) {

    }
}
