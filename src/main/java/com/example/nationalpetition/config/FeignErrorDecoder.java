package com.example.nationalpetition.config;

import feign.Response;
import feign.codec.ErrorDecoder;
import org.springframework.stereotype.Component;

@Component
public class FeignErrorDecoder implements ErrorDecoder {

	@Override
	public Exception decode(String methodKey, Response response) {
		return new IllegalArgumentException(String.format("외부 API 연동 중 에러가 발생하였습니다. (%s) (%s)", response.status(), response.body()));
	}

}