package com.example.nationalpetition.utils.error.exception;

import com.example.nationalpetition.utils.error.ErrorCode;

public class DuplicateException extends BusinessException{
    public DuplicateException(ErrorCode errorCode) {
        super(errorCode);
    }
}
