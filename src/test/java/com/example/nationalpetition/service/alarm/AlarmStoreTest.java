package com.example.nationalpetition.service.alarm;


import com.example.nationalpetition.domain.alarm.entity.Alarm;
import com.example.nationalpetition.domain.alarm.entity.AlarmEventType;
import com.example.nationalpetition.domain.alarm.repository.AlarmRepository;
import com.example.nationalpetition.domain.board.Board;
import com.example.nationalpetition.domain.board.repository.BoardRepository;
import com.example.nationalpetition.domain.comment.Comment;
import com.example.nationalpetition.domain.comment.CommentRepository;
import com.example.nationalpetition.domain.member.entity.Member;
import com.example.nationalpetition.domain.member.repository.MemberRepository;
import com.example.nationalpetition.dto.alarm.AlarmResponse;
import com.example.nationalpetition.testObject.BoardCreator;
import com.example.nationalpetition.testObject.MemberCreator;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.example.nationalpetition.domain.alarm.entity.AlarmEventType.*;
import static com.example.nationalpetition.domain.alarm.entity.AlarmState.*;
import static org.assertj.core.api.Assertions.*;


@SpringBootTest
class AlarmStoreTest {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private AlarmRepository alarmRepository;

    @Autowired
    private AlarmStore alarmStore;

    @Autowired
    private AlarmValidator alarmValidator;



    @AfterEach
    void clear() {
        alarmRepository.deleteAll();
        boardRepository.deleteAll();
        commentRepository.deleteAll();
        memberRepository.deleteAll();
    }

    @Test
    @DisplayName("댓글 알람 추가 하기")
    void createAlarm() {
        //given
        Member boardCreator = MemberCreator.create("aa@bb.cc", "게시글 작성자");
        Member commentWriter = MemberCreator.create("dd@ee.ff", "댓글 작성자");
        memberRepository.saveAll(List.of(boardCreator, commentWriter));

        Board board = BoardCreator.create(boardCreator.getId(), "게시글 제목", "게시글 내용");
        boardRepository.save(board);

        Comment comment = Comment.newRootComment(commentWriter, board.getId(), "댓글 내용");
        commentRepository.save(comment);

        final AlarmEventType alarmEventType = alarmValidator.checkCommentOrReComment(comment.getId());
        //when
        final AlarmResponse alarm = alarmStore.createAlarm(alarmEventType, comment.getId());
        //then
        assertThat(alarm.getEventType()).isEqualTo(COMMENT_CREATED);
        assertThat(alarm.getAlarmState()).isEqualTo(UNCHECKED);
        assertThat(alarm.getBoardId()).isEqualTo(board.getId());
        assertThat(alarm.getMessage()).isEqualTo(String.format(alarmEventType.getMessageFormat(), commentWriter.getNickName(), board.getTitle()));
    }



    @Test
    @DisplayName("알람 확인 시 알람 상태를 읽음으로 바꾸기")
    void checkAlarm() {
        // given
        Long memberId = 1L;
        Long boardId = 100L;

        final Alarm alarm = Alarm.of(memberId, boardId, "알람", COMMENT_CREATED);
        alarmRepository.save(alarm);

        // when
        final AlarmResponse alarmResponse = alarmStore.checkAlarm(alarm.getId(), memberId);

        // then
        assertThat(alarmResponse.getAlarmState()).isEqualTo(CHECKED);
    }

    @Test
    @Transactional
    @DisplayName("알람 삭제하기")
    void deleteAlarm() {
        // given
        Long memberId = 1L;
        Long boardId = 100L;

        Alarm alarm = Alarm.of(memberId, boardId, "알람", COMMENT_CREATED);
        alarmRepository.save(alarm);

        // when
        alarmStore.deleteAlarm(alarm.getId(), memberId);

        // then
        assertThat(alarm.getState()).isEqualTo(DELETED);
    }

}