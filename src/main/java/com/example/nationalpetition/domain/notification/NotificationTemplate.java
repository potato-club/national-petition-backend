package com.example.nationalpetition.domain.notification;

import lombok.Getter;

@Getter
public enum NotificationTemplate {

    CREATE_COMMENT("댓글 생성", "%s 님이 댓글을 달았습니다."),
    CREATE_RE_COMMENT("대댓글 생성", "%s 님이 대댓글을 달았습니다."),
    ;

    private final String description;
    private final String template;

    NotificationTemplate(String description, String template) {
        this.description = description;
        this.template = template;
    }

}
