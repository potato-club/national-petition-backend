package com.example.nationalpetition.config.feignClient;

import com.example.nationalpetition.utils.error.ErrorCode;
import com.example.nationalpetition.utils.error.exception.BadGatewayException;
import com.example.nationalpetition.utils.error.exception.ValidationException;
import feign.Response;
import feign.codec.ErrorDecoder;
import org.springframework.stereotype.Component;

@Component
public class FeignErrorDecoder implements ErrorDecoder {

	@Override
	public Exception decode(String methodKey, Response response) {
		if (400 <= response.status() && response.status() < 500) {
			return new ValidationException(ErrorCode.VALIDATION_EXCEPTION);
		}
		return new BadGatewayException(ErrorCode.BAD_GATEWAY_EXCEPTION);
	}

}