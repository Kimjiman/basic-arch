package com.example.basicarch.base.constants;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum RegexPattern {

    ID("^[a-z\\-_.0-9]{6,16}$", "아이디는 영문 소문자, 숫자, 특수문자(-_.)만 사용하여 6~16자로 입력해주세요."),
    PASSWORD("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,20}$", "비밀번호는 대문자, 소문자, 숫자, 특수문자(@$!%*?&)를 포함하여 8~20자로 입력해주세요."),
    NAME("^[a-zA-Z가-힣~!@#$%^&()\\-_+]{2,10}$", "이름은 한글, 영문, 특수문자 포함 2~10자로 입력해주세요."),
    EMAIL("^[a-zA-Z0-9_+.-]+@([a-zA-Z0-9-]+\\.)+[a-zA-Z0-9]{2,4}$", "올바른 이메일 형식으로 입력해주세요."),
    MOBILE("^[0-9]{10,11}$", "휴대전화번호는 숫자만 10~11자로 입력해주세요.");

    private final String pattern;
    private final String message;

    public static boolean matches(String value, RegexPattern regexPattern) {
        return value != null && value.matches(regexPattern.pattern);
    }
}
