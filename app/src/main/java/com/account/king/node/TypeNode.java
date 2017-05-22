package com.account.king.node;

import java.io.Serializable;

/**
 * Created by King
 * on 2017/5/22.
 */

public class TypeNode implements Serializable {

    private String name = "";
    private int id = 0;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
