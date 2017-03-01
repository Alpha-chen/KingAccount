package com.account.king.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.account.king.BaseActivity;
import com.account.king.rxevent.RxBus;
import com.account.king.rxevent.RxBusEvent;

import rx.Subscription;
import rx.functions.Action1;


public class BaseFragment extends Fragment implements Action1<RxBusEvent>{

    public BaseActivity mActivity;

    public String TAG = "BaseFragment";

    public Context activity;

    public Subscription subscription ;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.activity = context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = (BaseActivity) this.getActivity();

        subscription = RxBus.getDefault().toObserverable(RxBusEvent.class).subscribe(this);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {

    }

    public void initView() {

    }

    public void initPresenter(){



    }

    public void initIntent() {
    }

    public void initRMethod() {
    }

    public void initViewData() {
    }


    /**
     * 请求服务器之后更新界面数据
     */
    public void updateViewData() {
    }


    public void initData() {
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (subscription != null && !subscription.isUnsubscribed()){
            subscription.unsubscribe();
        }
    }

    @Override
    public void call(RxBusEvent rxBusEvent) {

    }
}
