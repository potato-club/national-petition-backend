package com.example.nationalpetition.utils.error.errorCode;

import lombok.Getter;

@Getter
public enum ErrorCode {

    UNAUTHORIZED_EXCEPTION("401"),
    FORBIDDEN_EXCEPTION("403"),
    NOTFOUND_EXCEPTION("404"),

    VALIDATION_EXCEPTION("409");

    private final String code;

    ErrorCode(String code) {
        this.code = code;
    }

}
