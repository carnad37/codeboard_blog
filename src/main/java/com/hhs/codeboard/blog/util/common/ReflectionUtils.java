package com.hhs.codeboard.blog.util.common;

import lombok.experimental.UtilityClass;

import java.lang.reflect.Method;

@UtilityClass
public class ReflectionUtils {

    public static Method findGetMethod(Class<?> classInfo, String name) {
        return findMethod(classInfo, "get" + name.substring(0, 1).toUpperCase() + name.substring(1));
    }

    public static Method findSetMethod(Class<?> classInfo, String name) {
        return findMethod(classInfo, "set" + name.substring(0, 1).toUpperCase() + name.substring(1));
    }

    public static Method findMethod(Class<?> classInfo, String name) {
        //Object 나오기전까지만 조회
        for (Method method : classInfo.getMethods()) {
            if (method.getName().equals(name)) {
                return method;
            }
        }
        // 주회를 끝내도 없으면 재귀
        Class<?> superClass = classInfo.getSuperclass();
        return Object.class.isAssignableFrom(superClass) ? null : findMethod(classInfo, name);
    }

}
