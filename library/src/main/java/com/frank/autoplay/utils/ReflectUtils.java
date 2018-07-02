package com.frank.autoplay.utils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by frank on 2018/3/11.
 */

public class ReflectUtils {

    public static Class<?> getInnerClass(Class<?> outerClazz, String innerClassName) {
        try {
            Class<?> innerClass = Class.forName(outerClazz.getName() + "$" + innerClassName);
            return innerClass;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static <T> T getField(Object o, Class<?> clazz, String fieldName) {
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

    public static <T> T invokeMethod(Object o, Class<?> clazz, String methodName, Class<?> parameterType, Object param) {
        if (parameterType.isInstance(param)) {
            try {
                Method method = clazz.getDeclaredMethod(methodName, parameterType);
                return (T) method.invoke(o, param);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
