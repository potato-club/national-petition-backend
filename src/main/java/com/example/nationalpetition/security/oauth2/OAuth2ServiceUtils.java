package com.example.nationalpetition.security.oauth2;

import com.example.nationalpetition.exception.JwtTokenException;
import com.example.nationalpetition.security.jwt.Token;
import com.example.nationalpetition.security.jwt.TokenService;
import com.example.nationalpetition.utils.error.ErrorCode;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletResponse;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class OAuth2ServiceUtils {

    public static HttpServletResponse writeTokenResponse(HttpServletResponse response, Token token) {
        response.setContentType("text/html;charset=UTF-8");
        response.addHeader("Authorization", token.getToken());
        response.addHeader("Refresh", token.getRefreshToken());
        response.setContentType("application/json;charset=UTF-8");

        return response;
    }

    public static void validateRefreshToken(TokenService tokenService, String refreshToken) {
        if (!StringUtils.hasText(refreshToken) || ! tokenService.validateToken(refreshToken)) {
            throw new JwtTokenException(ErrorCode.JWT_TOKEN_EXCEPTION_INVALID);
        }
    }
}

