package com.basicarch.base.exception;

import lombok.Getter;

import java.io.Serial;

@Getter
public class BaseException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 2288443710582826194L;

    private final int status;
    
    public BaseException(int status, String message) {
        super(message);
        this.status = status;
    }
    
    public BaseException(int status, String message, String appendMesage) {
        super(message + (null == appendMesage ? "" : appendMesage));
        this.status = status;
    }
    
    public BaseException(int status, String message, Exception e) {
        super(message, e);
        this.status = status;
    }
    
    public BaseException(int status, String message, String appendMesage, Exception e) {
        super(message + (null == appendMesage ? "" : appendMesage), e);
        this.status = status;
    }
}
