package com.example.nationalpetition.domain.alarm.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@Inheritance(strategy = InheritanceType.JOINED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class Alarm {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private Boolean isRead;

}
