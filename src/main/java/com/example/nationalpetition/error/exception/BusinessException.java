package com.example.nationalpetition.error.exception;

import com.example.nationalpetition.error.errorCode.ErrorCode;

public class BusinessException extends RuntimeException {

    private final ErrorCode code;

    public BusinessException(String message, ErrorCode code) {
        super(message);
        this.code = code;
    }

    public ErrorCode getErrorCode() {
        return code;
    }

}
