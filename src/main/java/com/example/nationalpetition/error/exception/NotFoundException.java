package com.example.nationalpetition.error.exception;

import com.example.nationalpetition.error.errorCode.ErrorCode;

public class NotFoundException extends BusinessException {

    public NotFoundException(String message) {
        super(message, ErrorCode.NOTFOUND_EXCEPTION);
    }

}
