package com.example.basicarch.base.constants;

import com.example.basicarch.base.exception.SystemErrorCode;
import com.example.basicarch.base.exception.ToyAssert;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

import java.util.Arrays;

public enum YN {
    Y ("true", "Y"),
    N ("false", "N");

    private final String key;
    @Getter
    private final String value;

    YN(String key, String value) {
        this.key = key;
        this.value = value;
    }

    /*
        RequestBody로 받기위해서 사용
        또한 넘어오는 key값이 boolean 타입이더라도 jackson 때문인지,
        String 형태로 받아진다. 그래서 String으로 받고 대소문자 구분없이
        데이터를 받게 변경한다.
    */

    @JsonValue
    public String isKey() {
        return key;
    }

    // RequestBody로 받기위해서 JsonCreator로 key(String/Boolean)을 받아서 ENUM값을 전달한다.
    @JsonCreator
    public static YN of(String key) {
        ToyAssert.notNull(key, SystemErrorCode.REQUIRED, "[YN] key must not be null.");

        return Arrays.stream(YN.values())
                .filter(it -> it.key.equals(key.toLowerCase()))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException(String.format("%s %s %s", "[YN]", key, "is illegal argument.")));
    }

    public static YN fromValue(String value) {
        ToyAssert.notNull(value, SystemErrorCode.REQUIRED, "[YN] value must not be null.");

        return Arrays.stream(YN.values())
                .filter(it -> it.value.equals(value))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException(String.format("%s %s %s", "[YN]", value, "is illegal argument.")));
    }
}
