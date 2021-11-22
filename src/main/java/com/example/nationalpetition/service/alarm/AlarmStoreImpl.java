package com.example.nationalpetition.service.alarm;

import com.example.nationalpetition.domain.alarm.entity.Alarm;
import com.example.nationalpetition.domain.alarm.entity.AlarmEventType;
import com.example.nationalpetition.domain.alarm.repository.AlarmRepository;
import com.example.nationalpetition.domain.board.Board;
import com.example.nationalpetition.domain.board.repository.BoardRepository;
import com.example.nationalpetition.domain.comment.Comment;
import com.example.nationalpetition.domain.comment.CommentRepository;
import com.example.nationalpetition.dto.alarm.AlarmResponse;
import com.example.nationalpetition.service.board.BoardServiceUtils;
import com.example.nationalpetition.service.comment.CommentServiceUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.example.nationalpetition.domain.alarm.entity.AlarmEventType.*;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class AlarmStoreImpl implements AlarmStore {

    private final AlarmRepository alarmRepository;
    private final CommentRepository commentRepository;
    private final BoardRepository boardRepository;

    @Override
    public AlarmResponse createAlarm(AlarmEventType AlarmEventType, Long commentId) {
        final Comment comment = CommentServiceUtils.findCommentById(commentRepository, commentId);
        final Board board = BoardServiceUtils.findBoardById(boardRepository, comment.getBoardId());

        if (isBoardWriterEqualsCommentWriter(AlarmEventType, comment, board)) {
            return null;
        }

        if (isReCommentWriterEqualsCommentWriter(AlarmEventType, comment)) {
            return null;
        }

        final AlarmMessage alarmMessage = AlarmMessage.of(board.getId(), board.getTitle(), comment.getMember().getNickName());
        return AlarmResponse.of(alarmRepository.save(alarmMessage.toEntity(board.getMemberId(), AlarmEventType)));
    }


    @Override
    public AlarmResponse checkAlarm(Long alarmId, Long memberId) {
        final Alarm alarm = AlarmServiceUtils.findAlarmByIdAndMemberId(alarmRepository, alarmId, memberId);
        alarm.check();
        return AlarmResponse.of(alarm);
    }

    @Override
    public void deleteAlarm(Long alarmId, Long memberId) {
        final Alarm alarm = AlarmServiceUtils.findAlarmByIdAndMemberId(alarmRepository, alarmId, memberId);
        alarm.delete();
    }


    private boolean isReCommentWriterEqualsCommentWriter(AlarmEventType AlarmEventType, Comment comment) {
        return AlarmEventType.equals(RE_COMMENT_CREATED) && comment.getParentId().equals(comment.getMember().getId());
    }

    private boolean isBoardWriterEqualsCommentWriter(AlarmEventType AlarmEventType, Comment comment, Board board) {
        return AlarmEventType.equals(COMMENT_CREATED) && board.isCreator(comment.getMember().getId());
    }
}
