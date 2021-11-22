package com.example.nationalpetition.service.alarm;

import com.example.nationalpetition.domain.alarm.entity.Alarm;
import com.example.nationalpetition.dto.alarm.AlarmListResponse;


public interface AlarmReader {

    AlarmListResponse getAlarmList(Long memberId);

    Alarm findByMemberIdAndCommentId(Long memberId, Long commentId);
}
