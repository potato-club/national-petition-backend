package com.example.nationalpetition.utils.error.exception;

import com.example.nationalpetition.utils.error.ErrorCode;

public class BadGatewayException extends BusinessException {

	public BadGatewayException(String message, ErrorCode errorCode) {
		super(message, errorCode);
	}

}
