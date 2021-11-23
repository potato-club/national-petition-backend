package com.example.nationalpetition.service.alarm;

import com.example.nationalpetition.domain.alarm.repository.AlarmRepository;
import com.example.nationalpetition.domain.comment.CommentRepository;
import com.example.nationalpetition.dto.alarm.AlarmListResponse;
import com.example.nationalpetition.dto.alarm.AlarmResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AlarmReaderImpl implements AlarmReader{

    private final AlarmRepository alarmRepository;
    private final CommentRepository commentRepository;

    @Override
    public AlarmListResponse getAlarmList(Long memberId) {
        return AlarmListResponse.of(alarmRepository.findAlarmsByMemberIdWithNotDeleted(memberId)
                .stream()
                .map(AlarmResponse::of)
                .collect(Collectors.toList()));
    }
}
