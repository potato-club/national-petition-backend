package com.example.nationalpetition.domain.alarm.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;

@Entity
@Getter
@NoArgsConstructor
public class BoardAlarm extends Alarm{

    private long boardId;
    private String title;
}
