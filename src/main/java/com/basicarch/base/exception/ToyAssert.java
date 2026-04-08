package com.basicarch.base.exception;

import com.basicarch.base.utils.StringUtils;

import java.util.Collection;

public class ToyAssert {
    public static void notBlank(String value, ErrorCode errorCode) {
        if (StringUtils.isBlank(value)) {
            throw new CustomException(errorCode);
        }
    }

    public static void notBlank(String value, ErrorCode errorCode, String message) {
        if (StringUtils.isBlank(value)) {
            throw new CustomException(errorCode, message);
        }
    }

    public static void notNull(Object value, ErrorCode errorCode) {
        if (value == null) {
            throw new CustomException(errorCode);
        }
    }

    public static void notNull(Object value, ErrorCode errorCode, String message) {
        if (value == null) {
            throw new CustomException(errorCode, message);
        }
    }

    public static void isTrue(boolean condition, ErrorCode errorCode) {
        if (condition) {
            throw new CustomException(errorCode);
        }
    }

    public static void isTrue(boolean condition, ErrorCode errorCode, String message) {
        if (condition) {
            throw new CustomException(errorCode, message);
        }
    }

    public static void notEmpty(Collection<?> collection, ErrorCode errorCode) {
        if (collection == null || collection.isEmpty()) {
            throw new CustomException(errorCode);
        }
    }

    public static void notEmpty(Collection<?> collection, ErrorCode errorCode, String message) {
        if (collection == null || collection.isEmpty()) {
            throw new CustomException(errorCode, message);
        }
    }
}
