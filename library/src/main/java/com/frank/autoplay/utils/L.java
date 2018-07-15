package com.frank.autoplay.utils;

import android.util.Log;

import java.util.Locale;

/**
 * Created by frank on 2018/3/10.
 */

public class L {

    private static final String PREFIX = "AUTO_PLAY";

    private static boolean sShowLog = false;

    public static int i(Object object, String method, String msg) {
        if (sShowLog) {
            return Log.i(PREFIX, createLogMsg(object, method, msg));
        }
        return -1;
    }

    public static String toString(Object o) {
        return o == null ? "null" : String.format(Locale.getDefault(), "[%s:%d]", o.getClass().getSimpleName(), o.hashCode());
    }

    private static String createLogMsg(Object object, String method, String msg) {
        return String.format(Locale.getDefault(), "[%s:%d]#%s %s", object.getClass().getSimpleName(), object.hashCode(), method, msg);
    }
}
