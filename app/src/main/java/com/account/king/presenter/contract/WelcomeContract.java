package com.account.king.presenter.contract;

import android.os.Handler;

/**
 * 引导页
 *
 * @King
 */
public class WelcomeContract {

    public interface IView {
        public void startMainScreen();
    }

    public interface IPresenter {
        public void showGuide(Handler handler);
    }
}
