package com.account.king.util;

import com.alibaba.fastjson.JSON;

import java.util.List;

/**
 * Created by King
 * on 2016/11/21.
 */

public class KingJson {
    /**
     *  json -> model
     * @param text
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T parseObject(String text, Class<T> clazz) {
        return JSON.parseObject(text,clazz);
    }

    /**
     * model -> json
     * @param javaObject
     * @return
     */
    public static Object toJSON(Object javaObject) {
        return JSON.toJSON(javaObject);
    }

    /**
     * jsonArray -> List<model>
     * @param text
     * @param clazz
     * @return
     */
    public static <T> List<T> parseArray(String text, Class<T> clazz){
        return JSON.parseArray(text,clazz);
    }
}
