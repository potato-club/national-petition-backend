package com.example.nationalpetition.domain.alarm.entity;

import com.example.nationalpetition.domain.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Alarm extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private long memberId;

    private long boardId;

    private String message;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private AlarmEventType eventType;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private AlarmState state;


    public Alarm(long memberId, long boardId,  String message, AlarmEventType eventType, AlarmState state) {
        this.memberId = memberId;
        this.boardId = boardId;
        this.message = message;
        this.eventType = eventType;
        this.state = state;
    }

    public static Alarm of(Long memberId, long boardId, String message, AlarmEventType eventType) {
        return new Alarm(memberId, boardId, message, eventType, AlarmState.UNCHECKED);
    }

    public void check() {
        this.state = AlarmState.CHECKED;
    }

    public void delete() {
        this.state = AlarmState.DELETED;
    }
}
