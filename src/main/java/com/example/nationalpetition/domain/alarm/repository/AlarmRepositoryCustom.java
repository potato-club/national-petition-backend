package com.example.nationalpetition.domain.alarm.repository;

import com.example.nationalpetition.domain.alarm.Alarm;

import java.util.List;

public interface AlarmRepositoryCustom {

	List<Alarm> findNotDeletedAlarmsByMemberId(Long memberId);

	Alarm findByIdAndMemberId(Long alarmId, Long memberId);

}
