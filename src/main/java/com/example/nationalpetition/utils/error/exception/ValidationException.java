package com.example.nationalpetition.utils.error.exception;

import com.example.nationalpetition.utils.error.ErrorCode;

public class ValidationException extends BusinessException {

	public ValidationException(String message, ErrorCode errorCode) {
		super(message, errorCode);
	}

}
