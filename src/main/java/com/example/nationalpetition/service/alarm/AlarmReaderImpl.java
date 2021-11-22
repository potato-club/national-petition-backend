package com.example.nationalpetition.service.alarm;

import com.example.nationalpetition.domain.alarm.entity.Alarm;
import com.example.nationalpetition.domain.alarm.repository.AlarmRepository;
import com.example.nationalpetition.domain.comment.Comment;
import com.example.nationalpetition.domain.comment.CommentRepository;
import com.example.nationalpetition.dto.alarm.AlarmListResponse;
import com.example.nationalpetition.dto.alarm.AlarmResponse;
import com.example.nationalpetition.service.comment.CommentServiceUtils;
import com.example.nationalpetition.utils.error.ErrorCode;
import com.example.nationalpetition.utils.error.exception.NotFoundException;
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

    @Override
    public Alarm findByMemberIdAndCommentId(Long memberId, Long commentId) {
        final Comment comment = CommentServiceUtils.findCommentById(commentRepository, commentId);
        return alarmRepository.findByMemberIdAndBoardId(memberId, comment.getBoardId()).orElseThrow(
                () -> new NotFoundException("알람을 찾을 수 없어요", ErrorCode.NOT_FOUND_EXCEPTION_ALARM));
    }
}
