package com.example.nationalpetition.service.alarm;

import com.example.nationalpetition.domain.alarm.entity.AlarmEventType;
import com.example.nationalpetition.domain.comment.Comment;
import com.example.nationalpetition.domain.comment.CommentRepository;
import com.example.nationalpetition.service.comment.CommentServiceUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AlarmValidator {

    private final CommentRepository commentRepository;

    public AlarmEventType validateAlarm(Long commentId) {
        final Comment comment = CommentServiceUtils.findCommentById(commentRepository, commentId);

        if (comment.getDepth() == 1) {
            return AlarmEventType.COMMENT_CREATED;
        }

        return AlarmEventType.RE_COMMENT_CREATED;
    }
}
