package com.example.nationalpetition.error.exception;

import com.example.nationalpetition.error.errorCode.ErrorCode;

public class ValidationException extends BusinessException {

    public ValidationException(String message) {
        super(message, ErrorCode.VALIDATION_EXCEPTION);
    }

}
