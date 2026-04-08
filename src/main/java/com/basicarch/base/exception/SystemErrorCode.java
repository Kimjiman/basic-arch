package com.basicarch.base.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SystemErrorCode implements ErrorCode {
    // 공통
    REQUIRED(1001, "필수값이 누락되었습니다."),
    NOT_FOUND(1002, "데이터를 찾을 수 없습니다."),
    DUPLICATE(1003, "이미 존재하는 데이터입니다."),
    INVALID_FORMAT(1004, "형식이 올바르지 않습니다."),
    INTERNAL_ERROR(1005, "시스템 오류가 발생했습니다."),

    // JWT
    INVALID_TOKEN(1101, "유효하지 않은 토큰입니다."),
    EXPIRED_TOKEN(1102, "만료된 토큰입니다."),
    UNSUPPORTED_TOKEN(1103, "지원하지 않는 토큰입니다."),
    EMPTY_CLAIMS(1104, "클레임이 비어 있습니다."),
    TOKEN_NOT_FOUND(1105, "토큰값이 존재하지 않습니다."),
    DUPLICATE_LOGIN(1106, "중복로그인이 발생했습니다."),
    TOKEN_PROCESSING_ERROR(1107, "토큰 처리 오류입니다."),

    // 파일
    FILE_ERROR(1201, "파일 처리 중 오류가 발생했습니다."),

    // 권한
    FORBIDDEN(1301, "접근 권한이 없습니다.");

    private final int code;
    private final String message;
}
