package com.account.king.thread;


import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by xupangen
 * on 2016/10/15.
 */
public class DefaultThreadPool {
    public static ExecutorService executorService = Executors.newFixedThreadPool(3);
    public static DefaultThreadPool defaultThreadPool;
    public static synchronized DefaultThreadPool getInstances (){
        if (null == defaultThreadPool){
            synchronized (DefaultThreadPool.class){
                if (null == defaultThreadPool){
                    defaultThreadPool = new DefaultThreadPool();
                }
            }
        }
        return defaultThreadPool;
    }

    public void excute (Runnable runnable){
        if (null == defaultThreadPool){
            executorService = Executors.newFixedThreadPool(3);
            executorService.equals(runnable);
        }else {
            executorService.execute(runnable);
        }
    }

}
