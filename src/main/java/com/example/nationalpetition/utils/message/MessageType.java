package com.example.nationalpetition.utils.message;

import lombok.Getter;

@Getter
public enum MessageType {

    DELETE_MEMBER("회원 탈퇴가 정상적으로 처리되었습니다."),
    NICKNAME_DUPLICATE("duplicate"),
    NICKNAME_SUCCESS("success");

    private final String message;

    MessageType(String message) {
        this.message = message;
    }
}
