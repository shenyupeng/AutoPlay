package com.frank.autoplay.utils;

import java.lang.reflect.Field;

/**
 * Created by frank on 2018/3/11.
 */

class ReflectUtils {

    static Class<?> getInnerClass(Class<?> outerClazz, String innerClassName) {
        try {
            Class<?> innerClass = Class.forName(outerClazz.getName() + "$" + innerClassName);
            return innerClass;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    static <T> T getField(Object o, Class<?> clazz, String fieldName) {
        try {
            Field field = clazz.getDeclaredField(fieldName);
            field.setAccessible(true);
            Object object = field.get(o);
            if (object != null) {
                return (T) object;
            }
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }
}
