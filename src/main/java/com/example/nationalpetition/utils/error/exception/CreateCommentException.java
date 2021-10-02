package com.example.nationalpetition.utils.error.exception;

import com.example.nationalpetition.utils.error.ErrorCode;

public class CreateCommentException extends BusinessException{

    public CreateCommentException(ErrorCode errorCode) {
        super(errorCode);
    }
}
