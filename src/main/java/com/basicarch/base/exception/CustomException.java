package com.basicarch.base.exception;

import lombok.Getter;

@Getter
public class CustomException extends BaseException {
    private final ErrorCode errorCode;

    public CustomException(ErrorCode errorCode) {
        super(errorCode.getCode(), errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public CustomException(ErrorCode errorCode, String message) {
        super(errorCode.getCode(), message);
        this.errorCode = errorCode;
    }

    public CustomException(ErrorCode errorCode, String message, Exception e) {
        super(errorCode.getCode(), message, e);
        this.errorCode = errorCode;
    }
}
