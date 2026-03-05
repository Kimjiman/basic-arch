package com.example.basicarch.base.constants;

import lombok.Getter;

import java.util.Arrays;

/**
 * packageName    : com.example.basicarch.base.constants
 * fileName       : CacheType
 * author         : KIM JIMAN
 * date           : 26. 3. 5. 화요일
 * description    :
 * ===========================================================
 * DATE           AUTHOR          NOTE
 * -----------------------------------------------------------
 * 26. 3. 5.     KIM JIMAN      First Commit
 */
@Getter
public enum CacheType {
    CODE(Names.CODE),
    MENU(Names.MENU);

    public static final String INVALIDATE_CHANNEL = "cache:invalidate";

    public static final class Names {
        public static final String CODE = "code";
        public static final String MENU = "menu";
    }

    private final String cacheName;

    CacheType(String cacheName) {
        this.cacheName = cacheName;
    }

    public static CacheType fromValue(String value) {
        return Arrays.stream(values())
                .filter(it -> it.getCacheName().equals(value))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown cache type: " + value));
    }
}
