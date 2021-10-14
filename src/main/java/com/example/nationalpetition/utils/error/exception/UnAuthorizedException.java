package com.example.nationalpetition.utils.error.exception;

import com.example.nationalpetition.utils.error.ErrorCode;

public class UnAuthorizedException extends BusinessException {

	public UnAuthorizedException(String message, ErrorCode errorCode) {
		super(message, errorCode);
	}

	public UnAuthorizedException(String message) {
		super(message, ErrorCode.UNAUTHORIZED_EXCEPTION);
	}

}

