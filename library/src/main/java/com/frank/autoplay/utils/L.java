package com.frank.autoplay.utils;

import android.util.Log;

import java.util.Locale;

/**
 * Created by frank on 2018/3/10.
 */

public class L {

    private static final String PREFIX = "AUTO_PLAY";

    private static boolean sShowLog = true;

    public static int i(Object object, String msg) {
        if (sShowLog) {
            return Log.i(PREFIX, createLogMsg(object, msg));
        }
        return -1;
    }

    public static int i(Object object, String msg, Throwable tr) {
        if (sShowLog) {
            return Log.i(PREFIX, createLogMsg(object, msg), tr);
        }
        return -1;
    }

    private static String createLogMsg(Object object, String msg) {
        return String.format(Locale.getDefault(), "[%s:%d] %s", object.getClass().getSimpleName(), object.hashCode(), msg);
    }
}
