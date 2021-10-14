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
			return new ValidationException(String.format("외부 API 호출 중 클라이언트 에러가 발생하였습니다. (%s)", response.body()), ErrorCode.VALIDATION_EXCEPTION);
		}
		return new BadGatewayException(String.format("외부 API 호출 중 서버 에러가 발생하였습니다. (%s)", response.body()), ErrorCode.BAD_GATEWAY_EXCEPTION);
	}

}