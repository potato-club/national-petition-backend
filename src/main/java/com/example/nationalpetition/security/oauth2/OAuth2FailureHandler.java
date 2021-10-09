package com.example.nationalpetition.security.oauth2;

import com.example.nationalpetition.utils.message.MessageType;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RequiredArgsConstructor
@Component
public class OAuth2FailureHandler extends SimpleUrlAuthenticationFailureHandler {

    private final OAuth2Properties oAuth2Properties;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        getRedirectStrategy().sendRedirect(request, response, createRedirectUri());
    }

    private String createRedirectUri() {
        // TODO : BAC-42 머지하면 메세지들 변수화하기 (충돌 방지로 일단 이렇게했어요)
        return UriComponentsBuilder.fromUriString(oAuth2Properties.getRedirectUri())
                .queryParam("message", "error")
                .build()
                .toUriString();
    }
}
