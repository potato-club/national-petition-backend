package com.example.nationalpetition.service.alarm.dto.request;

import com.example.nationalpetition.domain.alarm.Alarm;

public interface AlarmMessage {

	Alarm toEntity(Long memberId);

}
