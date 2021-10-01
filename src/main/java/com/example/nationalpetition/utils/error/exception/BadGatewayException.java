package com.example.nationalpetition.utils.error.exception;

import com.example.nationalpetition.utils.error.ErrorCode;

public class BadGatewayException extends BusinessException {

    public BadGatewayException(ErrorCode errorCode) {
        super(errorCode);
    }

}
