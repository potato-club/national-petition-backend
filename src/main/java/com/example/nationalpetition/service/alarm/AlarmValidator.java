package com.example.nationalpetition.service.alarm;

import com.example.nationalpetition.domain.alarm.entity.AlarmEventType;
import com.example.nationalpetition.domain.board.Board;
import com.example.nationalpetition.domain.board.repository.BoardRepository;
import com.example.nationalpetition.domain.comment.Comment;
import com.example.nationalpetition.domain.comment.CommentRepository;
import com.example.nationalpetition.service.board.BoardServiceUtils;
import com.example.nationalpetition.service.comment.CommentServiceUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static com.example.nationalpetition.domain.alarm.entity.AlarmEventType.*;

@Component
@RequiredArgsConstructor
public class AlarmValidator {

    private final CommentRepository commentRepository;
    private final BoardRepository boardRepository;

    public AlarmEventType checkCommentOrReComment(Long commentId) {
        final Comment comment = CommentServiceUtils.findCommentById(commentRepository, commentId);

        if (comment.getDepth() == 1) {
            return validateComment(comment);
        }

        return validateReComment(comment);
    }

    private AlarmEventType validateComment(Comment comment) {
        final Board board = BoardServiceUtils.findBoardById(boardRepository, comment.getBoardId());
        final Board boardWriter = BoardServiceUtils.findBoardById(boardRepository, board.getMemberId());

        if (!boardWriter.getIsAlarm()) {
            return null;
        }
        if (isBoardWriterEqualsCommentWriter(comment, board)) {
            return null;
        }
        return COMMENT_CREATED;
    }


    private AlarmEventType validateReComment(Comment reComment) {
        final Comment commentWriter = CommentServiceUtils.findCommentById(commentRepository, reComment.getParentId());

        if (!commentWriter.getIsAlarm()) {
            return null;
        }
        if (isReCommentWriterEqualsCommentWriter(reComment)) {
            return null;
        }
        return RE_COMMENT_CREATED;
    }


    private boolean isReCommentWriterEqualsCommentWriter(Comment comment) {
        return comment.getParentId().equals(comment.getMember().getId());
    }

    private boolean isBoardWriterEqualsCommentWriter( Comment comment, Board board) {
        return board.isCreator(comment.getMember().getId());
    }

}
