package com.account.king.presenter.contract.presenter;

import android.content.Context;

import com.account.king.db.storage.KingAccountStorage;
import com.account.king.node.KingAccountNode;
import com.account.king.presenter.contract.SearchContract;

import java.util.ArrayList;

/**
 * Created by King
 * on 2017/4/25.
 */

public class SearchPresenter implements SearchContract.IPresenter {
    private Context mContext;
    private SearchContract.IView mView;
    private KingAccountStorage mStorage;

    public SearchPresenter(Context context, SearchContract.IView view) {
        mContext = context;
        mView = view;
        mStorage = new KingAccountStorage(mContext);
    }

    @Override
    public void select(int accountType, int start, int end, int type, String content) {
        ArrayList<KingAccountNode> datas =
                (ArrayList<KingAccountNode>) mStorage.queryForTimeAndNoteType(accountType, start, end, type, content);
        if (null != datas && datas.size() > 0) {
            mView.selectSucces(datas);
        } else {
            mView.selectFailure();
        }
    }
}
