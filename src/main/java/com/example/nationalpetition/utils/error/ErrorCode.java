package com.example.nationalpetition.utils.error;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public enum ErrorCode {

    // Bad Gateway
    BAD_GATEWAY_EXCEPTION("B001", "외부 API 호출중에 애러가 발생했습니다."),

    // Not Found
    NOT_FOUND_EXCEPTION_USER("N001", "해당 하는 유저를 찾을 수 없습니다."),
    NOT_FOUND_EXCEPTION_BOARD("N002", "해당 하는 게시글을 찾을 수 없습니다."),
    NOT_FOUND_EXCEPTION_COMMENT("N003", "해당 하는 댓글을 찾을 수 없습니다."),

    // UnAuthorized
    JWT_TOKEN_EXCEPTION_INVALID("U001", "잘못된 JWT 서명입니다."),
    JWT_TOKEN_EXCEPTION_EXPIRED("U002", "JWT 토큰이 만료되었습니다."),
    JWT_TOKEN_EXCEPTION_NOTHING("U003", "JWT 토큰이 없습니다."),

    // Bad Request
    VALIDATION_EXCEPTION("B001", "유효하지 않은 값입니다."),

    // Conflict
    CONFLICT_EXCEPTION("C001", "이미 존재하는 값입니다."),
    DUPLICATE_EXCEPTION_NICKNAME("C002","닉네임이 중복됩니다."),
    ALREADY_EXIST_EXCEPTION_ADD_NICKNAME("C003","이미 닉네임을 등록하셨습니다.");

    private String code;
    private String message;

    ErrorCode(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
