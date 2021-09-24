package com.example.nationalpetition.security.jwt;

import com.example.nationalpetition.utils.error.exception.JwtTokenException;
import com.example.nationalpetition.utils.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@RequiredArgsConstructor
public class JwtAuthFilter extends GenericFilterBean {

	private static final String[] requiredTokenList = {
			"/api/**/mypage/**",
			"/api/**/board",
			"/api/**/board/**",
			"/api/**/comment/**"
	};

	private final TokenService tokenService;

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		final HttpServletRequest httpServletRequest = (HttpServletRequest) request;
		final String header = httpServletRequest.getHeader("Authorization");
		final String requestURI = httpServletRequest.getRequestURI();

		if (!isRequiredTokenPath(requestURI)) {
			log.info("token 유효성 검사가 필요없는 url들 입니다., uri: {}", requestURI);
			chain.doFilter(request, response);
			return;
		}

		if (!(StringUtils.hasText(header))) {
			log.info("유효한 JWT 토큰이 없습니다, uri: {}", requestURI);
			throw new JwtTokenException(ErrorCode.JWT_TOKEN_EXCEPTION_INVALID);
		}
		request.setAttribute("MEMBER_ID", tokenService.getMemberIdFromToken(removeBear(header)));
		chain.doFilter(request, response);
	}

	private boolean isRequiredTokenPath(String requestURI) {
		return PatternMatchUtils.simpleMatch(requiredTokenList, requestURI);
	}

	private String removeBear(String token) {
		if (StringUtils.hasText(token) && token.startsWith("Bearer ")) {
			token = token.substring(7);
		}
		return token;
	}

}
