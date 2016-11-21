package com.account.king.rxevent;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subjects.PublishSubject;
import rx.subjects.SerializedSubject;
import rx.subjects.Subject;

/**
 * Created by shaoqianqian on 16/7/7.
 */
public class RxBus {
    private static volatile RxBus defaultInstance;
    private final Subject bus;
    public RxBus() {
        bus = new SerializedSubject<>(PublishSubject.create());
    }
    // 单例RxBus
    public static RxBus getDefault() {
        RxBus rxBus = defaultInstance;
        if (defaultInstance == null) {
            synchronized (RxBus.class) {
                rxBus = defaultInstance;
                if (defaultInstance == null) {
                    rxBus = new RxBus();
                    defaultInstance = rxBus;
                }
            }
        }
        return rxBus;
    }
    public void send(Object o) {
        if (null==o){
            return;
        }
        bus.onNext(o);
    }

    public void send(Object... o) {
        if (null==o){
            return;
        }
        for(int i = 0 ; i < o.length ; i++){
            bus.onNext(o[i]);
        }
    }

    // 根据传递的 eventType 类型返回特定类型(eventType)的 被观察者  设置消费线程为主线程
    public <T> Observable<T> toObserverable (Class<T> eventType) {
        return bus.ofType(eventType).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread());
    }
}
