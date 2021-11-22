package com.example.nationalpetition.applicationlayer;

import com.example.nationalpetition.domain.alarm.entity.AlarmEventType;
import com.example.nationalpetition.dto.comment.request.CommentCreateDto;
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

    public Long createComment(CommentCreateDto dto, Long boardId, Long memberId) {
        final Long commentId = commentService.addComment(dto, boardId, memberId);
        final AlarmEventType alarmEventType = alarmValidator.validateAlarm(commentId);
        alarmStore.createAlarm(alarmEventType, commentId);
        return commentId;
    }

}
