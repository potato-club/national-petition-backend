package com.example.nationalpetition.service.alarm.dto.response;

import com.example.nationalpetition.domain.alarm.Alarm;
import com.example.nationalpetition.domain.alarm.AlarmEventType;
import com.example.nationalpetition.domain.alarm.AlarmState;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

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
