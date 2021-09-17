package com.example.nationalpetition.security.jwt;

import com.example.nationalpetition.exception.JwtTokenException;
import com.example.nationalpetition.utils.error.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.PatternMatchUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Slf4j
public class JwtAuthFilter extends GenericFilterBean {

    private static final String[] whiteList = {"/", "/css/*"};

    private final TokenService tokenService;

    public JwtAuthFilter(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        final HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        final String token = httpServletRequest.getHeader("Auth");
        final String requestURI = httpServletRequest.getRequestURI();

        if (isWhiteListPath(requestURI)) {
            log.info("token 유효성 검사가 필요없는 url들 입니다., uri: {}", requestURI);
            chain.doFilter(request, response);
            return;
        }

        if (!(StringUtils.hasText(token)) || !tokenService.validateToken(token)) {
            log.info("유효한 JWT 토큰이 없습니다, uri: {}", requestURI);
            throw new JwtTokenException(ErrorCode.JWT_TOKEN_EXCEPTION_INVALID);
        }

        final Authentication authentication = tokenService.getAuthentication(token);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        chain.doFilter(request, response);
    }

    private boolean isWhiteListPath(String requestURI) {
        return PatternMatchUtils.simpleMatch(whiteList, requestURI);
    }
}
