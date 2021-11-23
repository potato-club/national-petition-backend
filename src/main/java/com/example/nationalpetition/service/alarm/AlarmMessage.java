package com.example.nationalpetition.service.alarm;

import com.example.nationalpetition.domain.alarm.entity.Alarm;
import com.example.nationalpetition.domain.alarm.entity.AlarmEventType;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class AlarmMessage{

    private final Long boardId;
    private final String boardTitle;
    private final String nickName;

    public static AlarmMessage of(Long boardId, String boardTitle, String nickName) {
        return new AlarmMessage(boardId, boardTitle, nickName);
    }

    public Alarm toEntity(Long memberId, AlarmEventType alarmEventType) {
        return Alarm.of(memberId, boardId, String.format(alarmEventType.getMessageFormat(), nickName, boardTitle), alarmEventType);
    }
}
