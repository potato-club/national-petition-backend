package com.example.nationalpetition.utils.error.exception;

import com.example.nationalpetition.utils.error.ErrorCode;

public class AlreadyExistException extends BusinessException{
    public AlreadyExistException(ErrorCode errorCode) {
        super(errorCode);
    }
}
