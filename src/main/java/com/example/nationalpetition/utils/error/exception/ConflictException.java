package com.example.nationalpetition.utils.error.exception;

import com.example.nationalpetition.utils.error.ErrorCode;

public class ConflictException extends BusinessException {

	public ConflictException(String message, ErrorCode errorCode) {
		super(message, errorCode);
	}

}
