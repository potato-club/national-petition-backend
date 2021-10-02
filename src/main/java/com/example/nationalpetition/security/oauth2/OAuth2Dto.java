package com.example.nationalpetition.security.oauth2;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class OAuth2Dto {

    private String register;
    private String idToken;
    private String refreshToken;

    public OAuth2Dto(String register, String idToken, String refreshToken) {
        this.register = register;
        this.idToken = idToken;
        this.refreshToken = refreshToken;
    }

    public static OAuth2Dto of(String register, String idToken, String refreshToken) {
        return new OAuth2Dto(register, idToken, refreshToken);
    }
}
