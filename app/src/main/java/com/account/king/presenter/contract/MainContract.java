package com.account.king.presenter.contract;

/**
 * Created by King
 * on 2016/11/21.
 */

public class MainContract {
    public interface IMainView {
        public void setTabSelection(int position);
        public void clearSelection(int position);
    }

    public interface IMainPresenter {

    }
}
