package com.example.nationalpetition.service.alarm;

import com.example.nationalpetition.domain.alarm.entity.AlarmEventType;
import com.example.nationalpetition.dto.alarm.AlarmResponse;

public interface AlarmStore {

    AlarmResponse createAlarm(AlarmEventType AlarmEventType, Long commentId);

    AlarmResponse checkAlarm(Long alarmId, Long memberId);

    void deleteAlarm(Long alarmId, Long memberId);



}
