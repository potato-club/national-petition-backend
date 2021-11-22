package com.example.nationalpetition.service.alarm;

import com.example.nationalpetition.domain.alarm.entity.Alarm;
import com.example.nationalpetition.domain.alarm.repository.AlarmRepository;
import com.example.nationalpetition.utils.error.ErrorCode;
import com.example.nationalpetition.utils.error.exception.NotFoundException;

public class AlarmServiceUtils {

    public static Alarm findAlarmByIdAndMemberId(AlarmRepository alarmRepository, Long alarmId, Long memberId) {
        return alarmRepository.findByIdAndMemberId(alarmId, memberId).orElseThrow(
                () -> new NotFoundException(String.format("멤버 (%s)에게 해당하는 알람 (%s)은 존재하지 않습니다", memberId, alarmId), ErrorCode.NOT_FOUND_EXCEPTION_COMMENT));
    }
}
