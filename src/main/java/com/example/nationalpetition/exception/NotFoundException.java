package com.example.nationalpetition.exception;

import com.example.nationalpetition.utils.error.ErrorCode;

public class NotFoundException extends BusinessException{

    public NotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }
}
