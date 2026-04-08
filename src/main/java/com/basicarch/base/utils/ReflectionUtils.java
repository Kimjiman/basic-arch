package com.basicarch.base.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

/**
 * packageName    : com.basicarch.utils
 * fileName       : ReflatorUtils
 * author         : KIM JIMAN
 * date           : 24. 5. 22.
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 24. 5. 22.        KIM JIMAN       최초 생성
 */
public class ReflectionUtils {
    public static Object getFieldValue(Object obj, String fieldName) throws Exception {
        Field field = obj.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        return field.get(obj);
    }

    public static void setFieldValue(Object obj, String fieldName, Object value) throws Exception {
        Field field = obj.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(obj, value);
    }

    public static Object invokeMethod(Object obj, String methodName, Class<?>[] parameterTypes, Object[] args) throws Exception {
        Method method = obj.getClass().getDeclaredMethod(methodName, parameterTypes);
        List<String> clazzMethodsNameList = listMethodsName(obj.getClass());
        clazzMethodsNameList.stream()
                .filter(it -> it.equals(methodName))
                .findAny()
                .orElseThrow(() -> new NoSuchElementException("해당 클래스에 일치하는 메소드 명이 존재하지 않습니다"));
        method.setAccessible(true);
        return method.invoke(obj, args);
    }

    public static List<Method> listMethods(Class<?> clazz) {
        Method[] methods = clazz.getDeclaredMethods();
        return CollectionUtils.arrayToList(methods);
    }

    public static List<String> listMethodsName(Class<?> clazz) {
        List<Method> clazzMethodList = listMethods(clazz);
        return clazzMethodList.stream()
                .map(Method::getName)
                .collect(Collectors.toList());
    }
}
