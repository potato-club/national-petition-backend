package com.example.nationalpetition.utils.message;

import lombok.Getter;

@Getter
public enum MessageType {

    DELETE_MEMBER("회원 탈퇴가 정상적으로 처리되었습니다."),
    NICKNAME_DUPLICATE("duplicate"),
    NICKNAME_SUCCESS("success"),

    CODE("code"),
    MESSAGE("message"),
    ERROR("error"),

    TOKEN_NOT_REQUIRED("token 유효성 검사가 필요없는 url 입니다. uri: "),
    TOKEN_NOTHING("JWT 토큰이 없습니다, uri: "),
    TOKEN_INVALID("잘못된 JWT 서명입니다. ");

    private final String message;

    MessageType(String message) {
        this.message = message;
    }
}
