package com.example.nationalpetition.security.oauth2;

import com.example.nationalpetition.domain.member.entity.Member;
import com.example.nationalpetition.domain.member.repository.MemberRepository;
import com.example.nationalpetition.security.jwt.JwtProperties;
import com.example.nationalpetition.utils.error.exception.NotFoundException;
import com.example.nationalpetition.security.jwt.Token;
import com.example.nationalpetition.security.jwt.TokenService;
import com.example.nationalpetition.utils.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

	private static final String register = "register";
	private static final String idToken = "idToken";
	private static final String refreshToken = "refreshToken";

	private final OAuth2Properties oAuth2Properties;
	private final TokenService tokenService;
	private final MemberRepository memberRepository;

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
		final OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
		final Member member = memberRepository.findByEmail((String) oAuth2User.getAttributes().get("email"))
				.orElseThrow(() -> new NotFoundException(String.format("해당하는 이메일 (%s)을 가진 유저는 존재하지 않습니다", oAuth2User.getAttributes().get("email")), ErrorCode.NOT_FOUND_EXCEPTION_USER));
		final Token token = tokenService.generateToken(member.getId());

		if (!StringUtils.hasText(member.getNickName())) {
			getRedirectStrategy().sendRedirect(request, response, createRedirectUri(false, token));
			return;
		}

		response.addHeader(JwtProperties.HEADER_NAME, JwtProperties.TOKEN_PREFIX + token);
		getRedirectStrategy().sendRedirect(request, response, createRedirectUri(true, token));
	}

	private String createRedirectUri(boolean isRegister, Token token) {
		return UriComponentsBuilder.fromUriString(oAuth2Properties.getRedirectUri())
				.queryParam(register, isRegister)
				.queryParam(idToken, token.getToken())
				.queryParam(refreshToken, token.getRefreshToken())
				.build()
				.toUriString();
	}

}
