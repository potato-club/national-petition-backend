package com.example.nationalpetition.config.auth;

import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.validation.constraints.NotNull;

import static com.example.nationalpetition.config.auth.AuthConstants.MEMBER_ID;

@Component
public class MemberIdArgumentResolver implements HandlerMethodArgumentResolver {

	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		return parameter.getParameterAnnotation(MemberId.class) != null && parameter.getParameterType().equals(Long.class);
	}

	@Override
	public Object resolveArgument(@NotNull MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
		return webRequest.getAttribute(MEMBER_ID, 0);
	}

}
