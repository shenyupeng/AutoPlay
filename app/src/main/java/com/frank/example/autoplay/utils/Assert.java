package com.frank.example.autoplay.utils;

/**
 * Created by frank on 2018/3/10.
 */

public class Assert {

    public static void notNull(Object object) {
        if (object == null) {
            throw new NullPointerException("object is null");
        }
    }
}
