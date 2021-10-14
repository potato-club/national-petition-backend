package com.example.nationalpetition.utils.error.exception;

import com.example.nationalpetition.utils.error.ErrorCode;

public class NotFoundException extends BusinessException {

	public NotFoundException(String message, ErrorCode errorCode) {
		super(message, errorCode);
	}

}
