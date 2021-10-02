package com.example.nationalpetition.security.oauth2;

import com.example.nationalpetition.domain.member.entity.Member;
import com.example.nationalpetition.domain.member.repository.MemberRepository;
import com.example.nationalpetition.utils.error.exception.NotFoundException;
import com.example.nationalpetition.security.jwt.Token;
import com.example.nationalpetition.security.jwt.TokenService;
import com.example.nationalpetition.utils.error.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@Slf4j
@Component
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

    @Value("${redirect.baseUri}")
    private String baseUri;

    @Value("${redirect.addNickNameUri")
    private String addNickNameUri;

    private final TokenService tokenService;
    private final MemberRepository memberRepository;


    public OAuth2SuccessHandler(TokenService tokenService, MemberRepository memberRepository) {
        this.tokenService = tokenService;
        this.memberRepository = memberRepository;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        final OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        final Member member = memberRepository.findByEmail((String) oAuth2User.getAttributes().get("email")).orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND_EXCEPTION_USER));
        final Token token = tokenService.generateToken(member.getId());

        if (!StringUtils.hasText(member.getNickName())) {

            // TODO : 나중에 환경변수 다시 설정할 예정 ㅜㅜ
            response.sendRedirect("http://localhost:3000/user/login" + "?register=false" + "&idToken=" + token.getToken() + "&refreshToken=" + token.getRefreshToken());
            return;
        }
        // TODO : 나중에 환경변수 다시 설정할 예정 ㅜㅜ
        response.sendRedirect("http://localhost:3000/user/login" + "?register=true" + "&idToken=" + token.getToken() + "&refreshToken=" + token.getRefreshToken());
    }

}
