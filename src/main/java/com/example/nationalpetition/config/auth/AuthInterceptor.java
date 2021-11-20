package com.example.nationalpetition.config.auth;

import com.example.nationalpetition.security.jwt.TokenService;
import com.example.nationalpetition.utils.error.exception.UnAuthorizedException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;

import static com.example.nationalpetition.config.auth.AuthConstants.MEMBER_ID;

@RequiredArgsConstructor
@Component
public class AuthInterceptor implements HandlerInterceptor {

    private static final String BEARER_PREFIX = "Bearer";

    private final TokenService tokenService;

    @Override
    public boolean preHandle(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response, Object handler) {
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Auth auth = handlerMethod.getMethodAnnotation(Auth.class);
        if (auth == null) {
            return true;
        }

        String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (StringUtils.hasLength(header) && header.startsWith(BEARER_PREFIX)) {
            String token = removeBearer(header);
            if (auth.required()) {
                request.setAttribute(MEMBER_ID, tokenService.getMemberIdFromToken(token));
                return true;
            }
            request.setAttribute(MEMBER_ID, tokenService.getMemberIdFromTokenOrNull(token));
            return true;
        }

        if (auth.required()) {
            throw new UnAuthorizedException(String.format("Authorization Header (%s)이 비어있습니다.", header));
        }
        return true;
    }

    private String removeBearer(String header) {
        return header.split(BEARER_PREFIX)[1];
    }

}
