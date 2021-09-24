package com.example.nationalpetition.utils.error;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public enum ErrorCode {

    NOT_FOUND_EXCEPTION_USER("400", "해당 하는 유저를 찾을 수 없습니다."),
    NOT_FOUND_EXCEPTION_BOARD("400", "해당 하는 게시글을 찾을 수 없습니다."),
    NOT_FOUND_EXCEPTION_COMMENT("400", "해당 하는 댓글을 찾을 수 없습니다."),

    JWT_TOKEN_EXCEPTION_INVALID("400", "유효한 JWT 토큰이 없습니다"),
    JWT_TOKEN_EXCEPTION_EXPIRED("400", "JWT 토큰이 만료되었습니다.");

    private String code;
    private String message;

    ErrorCode(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
