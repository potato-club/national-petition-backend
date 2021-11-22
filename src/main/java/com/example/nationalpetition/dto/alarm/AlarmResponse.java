package com.example.nationalpetition.dto.alarm;

import com.example.nationalpetition.domain.alarm.entity.Alarm;
import com.example.nationalpetition.domain.alarm.entity.AlarmEventType;
import com.example.nationalpetition.domain.alarm.entity.AlarmState;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class AlarmResponse {

    private final Long alarmId;

    private final AlarmEventType eventType;

    private final AlarmState alarmState;

    private final String message;

    private final Long boardId;

    public static AlarmResponse of(Alarm alarm) {
        return new AlarmResponse(alarm.getId(), alarm.getEventType(), alarm.getState(), alarm.getMessage(), alarm.getBoardId());
    }

}