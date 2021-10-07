package com.example.nationalpetition.security.oauth2;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Component
public class OAuth2Properties {

    @Value("${google.redirect.uri}")
    private String redirectUri;
}
