package com.account.king.presenter.contract.presenter;

import android.content.Context;

import com.account.king.db.storage.KingAccountStorage;
import com.account.king.node.KingAccountNode;
import com.account.king.presenter.contract.SearchContract;
import com.account.king.util.CalendarUtil;
import com.account.king.util.PermissionUtil;
import com.account.king.view.dialog.CalendarDialog;

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
    public void select(int accountType, long start, long end, int type, String content) {
        ArrayList<KingAccountNode> datas =
                (ArrayList<KingAccountNode>) mStorage.queryForTimeAndNoteType(accountType, start, end, type, content);
        if (null != datas && datas.size() > 0) {
            mView.selectSucces(datas);
        } else {
            mView.selectFailure();
        }
    }

    @Override
    public void selectDate(Context context, long selectDate) {
        final CalendarDialog calendarDialog = new CalendarDialog(context);
        calendarDialog.setDate(0 == selectDate ? CalendarUtil.timeMilis2Date(CalendarUtil.getNowTimeMillis()) : CalendarUtil.timeMilis2Date(selectDate));
        calendarDialog.setOnSelectDateListener(new CalendarDialog.OnSelectDateListener() {
            @Override
            public void okDate(long date) {
                calendarDialog.dismiss();
                mView.setDateText(date);
            }
        });
        PermissionUtil.getAlertPermission(context, calendarDialog);
    }
}
