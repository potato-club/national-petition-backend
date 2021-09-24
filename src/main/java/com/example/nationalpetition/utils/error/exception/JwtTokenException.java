package com.example.nationalpetition.utils.error.exception;

import com.example.nationalpetition.utils.error.ErrorCode;

public class JwtTokenException extends BusinessException {

    public JwtTokenException(ErrorCode errorCode) {
        super(errorCode);
    }
}
