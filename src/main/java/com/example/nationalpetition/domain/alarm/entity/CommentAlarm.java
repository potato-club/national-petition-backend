package com.example.nationalpetition.domain.alarm.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CommentAlarm extends Alarm {

    private long commentId;
    private String content;

}
