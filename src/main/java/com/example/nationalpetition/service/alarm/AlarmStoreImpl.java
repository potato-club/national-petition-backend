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

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class AlarmStoreImpl implements AlarmStore {

    private final AlarmRepository alarmRepository;
    private final CommentRepository commentRepository;
    private final BoardRepository boardRepository;

    @Transactional
    @Override
    public AlarmResponse createAlarm(AlarmEventType AlarmEventType, Long commentId) {
        final Comment comment = CommentServiceUtils.findCommentById(commentRepository, commentId);
        final Board board = BoardServiceUtils.findBoardById(boardRepository, comment.getBoardId());

        final AlarmMessage alarmMessage = AlarmMessage.of(board.getId(), board.getTitle(), comment.getMember().getNickName());
        return AlarmResponse.of(alarmRepository.save(alarmMessage.toEntity(board.getMemberId(), AlarmEventType)));
    }

    @Transactional
    @Override
    public AlarmResponse checkAlarm(Long alarmId, Long memberId) {
        final Alarm alarm = AlarmServiceUtils.findAlarmByIdAndMemberId(alarmRepository, alarmId, memberId);
        alarm.check();
        return AlarmResponse.of(alarm);
    }

    @Transactional
    public void deleteAlarm(Long alarmId, Long memberId) {
        Alarm alarm = AlarmServiceUtils.findAlarmByIdAndMemberId(alarmRepository, alarmId, memberId);
        alarm.delete();
    }

}
