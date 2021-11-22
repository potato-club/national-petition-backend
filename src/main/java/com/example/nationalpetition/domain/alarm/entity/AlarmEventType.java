package com.example.nationalpetition.domain.alarm.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum AlarmEventType {

    COMMENT_CREATED("댓글 생성", "%s가 게시물에 댓글을 달았습니다.", true),
    RE_COMMENT_CREATED("답글 생성", "%가 댓글에 답글을 달았습니다.", true),
    ;

    private final String description;
    private final String messageFormat;
    private final boolean hasLinkId;

}