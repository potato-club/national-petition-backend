package com.example.nationalpetition.security.oauth2;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class OAuth2Dto {

    private String register;
    private String token;
    private String refreshToken;

    public OAuth2Dto(String register, String token, String refreshToken) {
        this.register = register;
        this.token = token;
        this.refreshToken = refreshToken;
    }

    public static OAuth2Dto of(String register, String token, String refreshToken) {
        return new OAuth2Dto(register, token, refreshToken);
    }
}
