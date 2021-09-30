package com.example.nationalpetition.utils.error;

import com.example.nationalpetition.utils.error.exception.ValidationException;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public enum ErrorCode {

    BAD_GATEWAY_EXCEPTION("400", "외부 API 호출중에 애러가 발생했습니다."),

    NOT_FOUND_EXCEPTION_USER("400", "해당 하는 유저를 찾을 수 없습니다."),
    NOT_FOUND_EXCEPTION_BOARD("400", "해당 하는 게시글을 찾을 수 없습니다."),
    NOT_FOUND_EXCEPTION_COMMENT("400", "해당 하는 댓글을 찾을 수 없습니다."),

    JWT_TOKEN_EXCEPTION_INVALID("400", "유효한 JWT 토큰이 없습니다"),
    JWT_TOKEN_EXCEPTION_EXPIRED("400", "JWT 토큰이 만료되었습니다."),

    VALIDATION_EXCEPTION("400", "유효하지 않은 값입니다."),

    CONFLICT_EXCEPTION("400", "이미 존재하는 값입니다."),

    DUPLICATE_EXCEPTION_NICKNAME("400","닉네임이 중복됩니다."),

    ALREADY_EXIST_EXCEPTION_ADD_NICKNAME("400","이미 닉네임을 등록하셨습니다.");

    private String code;
    private String message;

    ErrorCode(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
