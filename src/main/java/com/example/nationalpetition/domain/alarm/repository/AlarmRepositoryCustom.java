package com.example.nationalpetition.domain.alarm.repository;

import com.example.nationalpetition.domain.alarm.entity.Alarm;

import java.util.List;

public interface AlarmRepositoryCustom {

    List<Alarm> findAlarmsByMemberIdWithNotDeleted(Long memberId);
}
