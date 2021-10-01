package com.example.nationalpetition.config.feignClient;

import com.example.nationalpetition.utils.error.ErrorCode;
import com.example.nationalpetition.utils.error.exception.BadGatewayException;
import feign.Response;
import feign.codec.ErrorDecoder;
import org.springframework.stereotype.Component;

@Component
public class FeignErrorDecoder implements ErrorDecoder {

	@Override
	public Exception decode(String methodKey, Response response) {
		return new BadGatewayException(ErrorCode.BAD_GATEWAY_EXCEPTION);
	}

}