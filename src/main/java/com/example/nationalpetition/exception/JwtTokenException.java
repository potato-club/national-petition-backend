package com.example.nationalpetition.exception;

import com.example.nationalpetition.utils.error.ErrorCode;

public class JwtTokenException extends BusinessException {

    public JwtTokenException(ErrorCode errorCode) {
        super(errorCode);
    }
}
