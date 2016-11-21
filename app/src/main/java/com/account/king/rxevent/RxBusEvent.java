package com.account.king.rxevent;

/**
 * Created by King
 * on 2016/11/21.
 */

public class RxBusEvent {
    private int id;
    private Object object;

    public RxBusEvent(int id, Object object) {
        this.id = id;
        this.object = object;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }
}
