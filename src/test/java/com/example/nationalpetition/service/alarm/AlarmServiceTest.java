package com.example.nationalpetition.service.alarm;

import com.example.nationalpetition.controller.member.MemberServiceUtils;
import com.example.nationalpetition.domain.alarm.entity.CommentAlarm;
import com.example.nationalpetition.domain.alarm.repository.CommentAlarmRepository;
import com.example.nationalpetition.domain.board.Board;
import com.example.nationalpetition.domain.board.BoardCategory;
import com.example.nationalpetition.domain.board.repository.BoardRepository;
import com.example.nationalpetition.domain.comment.Comment;
import com.example.nationalpetition.domain.comment.CommentRepository;
import com.example.nationalpetition.domain.member.entity.Member;
import com.example.nationalpetition.domain.member.repository.MemberRepository;
import com.example.nationalpetition.dto.comment.request.CommentCreateDto;
import com.example.nationalpetition.service.comment.CommentService;
import com.example.nationalpetition.testObject.BoardCreator;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AlarmServiceTest {

    @Autowired
    MemberRepository memberRepository;
    @Autowired
    BoardRepository boardRepository;
    @Autowired
    CommentRepository commentRepository;
    @Autowired
    CommentAlarmRepository commentAlarmRepository;
    @Autowired
    CommentService commentService;
    @Autowired
    AlarmService alarmService;


    private Member boardWriter;
    private Member commentWriter;

    @BeforeEach
    public void setUp() {
        boardWriter = memberRepository.save(Member.of("게시글작성자", "aaa@bbb.cc", "a.jpg"));
        commentWriter = memberRepository.save(Member.of("댓글작성자", "ddd@eee.ff", "b.jpg"));
        commentWriter.addNickName("감자");

    }

    @AfterEach
    void clear() {
        commentAlarmRepository.deleteAll();
        commentRepository.deleteAll();
        memberRepository.deleteAll();
        boardRepository.deleteAll();
    }


    @Test
    @DisplayName("댓글 알람 설정 시 해당 게시글에 댓글 달릴 시 저장")
    void createCommentAlarm() {
        //given
        final Board board = boardRepository.save(BoardCreator.create(boardWriter.getId(), "title", "content"));
        final Comment comment = commentRepository.save(Comment.newRootComment(commentWriter, board.getId(), "저는 반대입니다."));

        //when
        final CommentAlarm commentAlarm = alarmService.createCommentAlarm(board, commentWriter.getNickName(), comment.getId());
        //then
        assertThat(commentAlarm.getBoardId()).isEqualTo(board.getId());
        assertThat(commentAlarm.getMemberId()).isEqualTo(boardWriter.getId());
        assertThat(commentAlarm.getCommentId()).isEqualTo(comment.getId());
        assertThat(commentAlarm.getMessage()).isEqualTo(commentWriter.getNickName() + "님이 댓글을 작성하였습니다. : " + BoardCreator.create(boardWriter.getId(), "title", "content").getTitle());
        assertThat(commentAlarm.getIsRead()).isFalse();
    }

    @Test
    @DisplayName("댓글 알람 설정 시 해당 게시글에 본인이 댓글 달았을 경우 저장 X")
    void createCommentAlarm_notSave() {
        //given
        final Board board = boardRepository.save(BoardCreator.create(boardWriter.getId(), "title2", "content2"));

        final Comment comment = commentRepository.save(Comment.newRootComment(boardWriter, board.getId(), "저는 반대입니다."));
        //when
        final CommentAlarm commentAlarm = alarmService.createCommentAlarm(board, boardWriter.getNickName(), comment.getId());
        //then
        assertThat(commentAlarm.getBoardId()).isEqualTo(0L);
        assertThat(commentAlarm.getMemberId()).isEqualTo(0L);
        assertThat(commentAlarm.getMessage()).isNull();
    }

}