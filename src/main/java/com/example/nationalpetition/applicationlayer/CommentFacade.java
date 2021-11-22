package com.example.nationalpetition.applicationlayer;

import com.example.nationalpetition.domain.alarm.entity.AlarmEventType;
import com.example.nationalpetition.dto.comment.request.CommentCreateDto;
import com.example.nationalpetition.service.alarm.AlarmReader;
import com.example.nationalpetition.service.alarm.AlarmStore;
import com.example.nationalpetition.service.alarm.AlarmValidator;
import com.example.nationalpetition.service.comment.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CommentFacade {

    private final CommentService commentService;
    private final AlarmStore alarmStore;
    private final AlarmValidator alarmValidator;
    private final AlarmReader alarmReader;

    public Long addComment(CommentCreateDto dto, Long boardId, Long memberId) {
        // 댓글 or 대댓글 쓰기
        final Long commentId = commentService.addComment(dto, boardId, memberId);
        // 댓글인지 대댓글인지 체크
        final AlarmEventType alarmEventType = alarmValidator.validateAlarm(commentId);
        // 알람 저장
        alarmStore.createAlarm(alarmEventType, commentId);
        return commentId;
    }


}
