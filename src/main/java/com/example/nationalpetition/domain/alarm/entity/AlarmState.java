package com.example.nationalpetition.domain.alarm.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AlarmState {

    CHECKED("확인된 알림"),
    UNCHECKED("미확인된 알림"),
    DELETED("삭제 처리된 알림")
    ;

    private final String description;

}