package com.example.nationalpetition.service.alarm;

import com.example.nationalpetition.controller.member.MemberServiceUtils;
import com.example.nationalpetition.domain.alarm.entity.CommentAlarm;
import com.example.nationalpetition.domain.alarm.entity.ReplyCommentAlarm;
import com.example.nationalpetition.domain.alarm.repository.CommentAlarmRepository;
import com.example.nationalpetition.domain.alarm.repository.ReplyCommentAlarmRepository;
import com.example.nationalpetition.domain.board.Board;
import com.example.nationalpetition.domain.board.BoardCategory;
import com.example.nationalpetition.domain.board.repository.BoardRepository;
import com.example.nationalpetition.domain.comment.Comment;
import com.example.nationalpetition.domain.comment.CommentRepository;
import com.example.nationalpetition.domain.member.entity.Member;
import com.example.nationalpetition.domain.member.repository.MemberRepository;
import com.example.nationalpetition.dto.alarm.AlarmListResponse;
import com.example.nationalpetition.dto.alarm.AlarmResponse;
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
    ReplyCommentAlarmRepository replyCommentAlarmRepository;
    @Autowired
    CommentService commentService;
    @Autowired
    AlarmService alarmService;


    private Member boardWriter;
    private Member commentWriter;
    private Member replyCommentWriter;

    @BeforeEach
    public void setUp() {
        boardWriter = memberRepository.save(Member.of("게시글작성자", "aaa@bbb.cc", "a.jpg"));
        commentWriter = memberRepository.save(Member.of("댓글작성자", "ddd@eee.ff", "b.jpg"));
        commentWriter.addNickName("감자");
        replyCommentWriter = memberRepository.save(Member.of("대댓글작성자", "ddd@eee.ff", "b.jpg"));
        replyCommentWriter.addNickName("고구마");

    }

    @AfterEach
    void clear() {
        replyCommentAlarmRepository.deleteAll();
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
        assertThat(commentAlarm.getMessage()).isEqualTo(commentWriter.getNickName() + "님이 댓글을 작성하였습니다. : " + board.getTitle());
        assertThat(commentAlarm.getIsRead()).isFalse();
    }

    @Test
    @Transactional
    @DisplayName("댓글 알람 설정 시 해당 게시글 알람 OFF 시켰을 경우 저장 X")
    void createCommentAlarm_notSave() {
        //given
        final Board board = boardRepository.save(BoardCreator.create(boardWriter.getId(), "title2", "content2"));
        board.updateCommentAlarm(false);
        final Comment comment = commentRepository.save(Comment.newRootComment(commentWriter, board.getId(), "저는 반대입니다."));
        //when
        final CommentAlarm commentAlarm = alarmService.createCommentAlarm(board, commentWriter.getNickName(), comment.getId());
        //then
        assertThat(commentAlarm.getBoardId()).isEqualTo(0L);
        assertThat(commentAlarm.getMemberId()).isEqualTo(0L);
        assertThat(commentAlarm.getMessage()).isNull();
    }

//    @Test
//    @Transactional
//    @DisplayName("게시글 알람 ON인데 마이페이지 알람 설정 안한 게시글 작성자면 저장 X")
//    void createCommentAlarm_notSave2() {
//        //given
//        boardWriter.changeAlarm(false);
//        final Board board = boardRepository.save(BoardCreator.create(boardWriter.getId(), "title2", "content2"));
//        final Comment comment = commentRepository.save(Comment.newRootComment(commentWriter, board.getId(), "저는 반대입니다."));
//        //when
//        final CommentAlarm commentAlarm = alarmService.createCommentAlarm(board, commentWriter.getNickName(), comment.getId());
//        //then
//        assertThat(commentAlarm.getBoardId()).isEqualTo(0L);
//        assertThat(commentAlarm.getMemberId()).isEqualTo(0L);
//        assertThat(commentAlarm.getMessage()).isNull();
//    }

    @Test
    @DisplayName("댓글 알람 설정 시 해당 게시글에 본인이 댓글 달았을 경우 저장 X")
    void createCommentAlarm_notSave3() {
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

    @Test
    @DisplayName("대댓글 알람 설정 시 해당 댓글에 대댓글이 달렸을 경우 저장")
    void createReplyCommentAlarm() {
        //given
        final Board board = boardRepository.save(BoardCreator.create(boardWriter.getId(), "title2", "content2"));

        final Comment comment = commentRepository.save(Comment.newRootComment(commentWriter, board.getId(), "저는 반대입니다."));

        final Comment replyComment = commentRepository.save(Comment.newChildComment(comment.getId(), replyCommentWriter, board.getId(), comment.getDepth() + 1, "저는 찬성이요"));
        //when
        final ReplyCommentAlarm replyCommentAlarm = alarmService.createReplyCommentAlarm(board, replyCommentWriter.getNickName(), comment.getId(), replyComment.getId());
        //then
        assertThat(replyCommentAlarm.getBoardId()).isEqualTo(board.getId());
        assertThat(replyCommentAlarm.getParentId()).isEqualTo(comment.getId());
        assertThat(replyCommentAlarm.getMemberId()).isEqualTo(boardWriter.getId());
        assertThat(replyCommentAlarm.getMessage()).isEqualTo(replyCommentWriter.getNickName() + "님이 대댓글을 작성하였습니다. : " + board.getTitle());
    }

    @Test
    @Transactional
    @DisplayName("대댓글 알람 설정 시 해당 게시글 알람 OFF 시켰을 경우 저장 X")
    void createReplyCommentAlarm_notSave() {
        //given
        final Board board = boardRepository.save(BoardCreator.create(boardWriter.getId(), "title2", "content2"));
        board.updateReplyCommentAlarm(false);

        final Comment comment = commentRepository.save(Comment.newRootComment(commentWriter, board.getId(), "저는 반대입니다."));

        final Comment replyComment = commentRepository.save(Comment.newChildComment(comment.getId(), replyCommentWriter, board.getId(), comment.getDepth() + 1, "저는 찬성이요"));
        //when
        final ReplyCommentAlarm replyCommentAlarm = alarmService.createReplyCommentAlarm(board, replyCommentWriter.getNickName(), comment.getId(), replyComment.getId());
        //then
        assertThat(replyCommentAlarm.getBoardId()).isEqualTo(0L);
        assertThat(replyCommentAlarm.getMemberId()).isEqualTo(0L);
        assertThat(replyCommentAlarm.getParentId()).isEqualTo(0L);
        assertThat(replyCommentAlarm.getCommentId()).isEqualTo(0L);
        assertThat(replyCommentAlarm.getMessage()).isNull();
    }

//    @Test
//    @Transactional
//    @DisplayName("게시글 알람 ON인데 마이페이지 알람 설정 안한 게시글 작성자면 저장 X")
//    void createReplyCommentAlarm_notSave2() {
//        //given
//        boardWriter.changeAlarm(false);
//
//        final Board board = boardRepository.save(BoardCreator.create(boardWriter.getId(), "title2", "content2"));
//        final Comment comment = commentRepository.save(Comment.newRootComment(commentWriter, board.getId(), "저는 반대입니다."));
//        final Comment replyComment = commentRepository.save(Comment.newChildComment(comment.getId(), replyCommentWriter, board.getId(), comment.getDepth() + 1, "저는 찬성이요"));
//
//        //when
//        final ReplyCommentAlarm replyCommentAlarm = alarmService.createReplyCommentAlarm(board, replyCommentWriter.getNickName(), comment.getId(), replyComment.getId());
//        //then
//        assertThat(replyCommentAlarm.getBoardId()).isEqualTo(0L);
//        assertThat(replyCommentAlarm.getMemberId()).isEqualTo(0L);
//        assertThat(replyCommentAlarm.getParentId()).isEqualTo(0L);
//        assertThat(replyCommentAlarm.getCommentId()).isEqualTo(0L);
//        assertThat(replyCommentAlarm.getMessage()).isNull();
//    }

    @Test
    @DisplayName("대댓글 알람 설정 시 해당 댓글에 본인이 대댓글 달았을 경우 저장 X")
    void createReplyCommentAlarm_notSave3() {
        //given
        final Board board = boardRepository.save(BoardCreator.create(boardWriter.getId(), "title2", "content2"));

        final Comment comment = commentRepository.save(Comment.newRootComment(commentWriter, board.getId(), "저는 반대입니다."));
        final Comment replyComment = commentRepository.save(Comment.newChildComment(comment.getId(), boardWriter, board.getId(), comment.getDepth() + 1, "저는 찬성이요"));
        //when
        final ReplyCommentAlarm replyCommentAlarm = alarmService.createReplyCommentAlarm(board, boardWriter.getNickName(), comment.getId(), replyComment.getId());
        //then
        assertThat(replyCommentAlarm.getBoardId()).isEqualTo(0L);
        assertThat(replyCommentAlarm.getMemberId()).isEqualTo(0L);
        assertThat(replyCommentAlarm.getParentId()).isEqualTo(0L);
        assertThat(replyCommentAlarm.getCommentId()).isEqualTo(0L);
        assertThat(replyCommentAlarm.getMessage()).isNull();
    }

    @Test
    @DisplayName("알람 정보 조회하기 댓글 + 대댓글 합쳐서")
    void getAlarmList() {
        //given
        // 게시글 2개에 댓글 대댓글 각각 5개씩 있을 때
        for (int i = 0; i < 2; i++) {
            final Board board = boardRepository.save(BoardCreator.create(boardWriter.getId(), "title" + i, "content" + i));
            for (int j = 0; j < 5; j++) {
                final Comment comment = commentRepository.save(Comment.newRootComment(commentWriter, board.getId(), "저는 반대입니다."));
                final Comment replyComment = commentRepository.save(Comment.newChildComment(comment.getId(), replyCommentWriter, board.getId(), comment.getDepth() + 1, "저는 찬성이요"));
                alarmService.createCommentAlarm(board, commentWriter.getNickName(), comment.getId());
                alarmService.createReplyCommentAlarm(board, replyCommentWriter.getNickName(), comment.getId(), replyComment.getId());
            }
        }
        //when
        final AlarmListResponse alarmList = alarmService.getAlarmList(boardWriter.getId());
        //then
        assertThat(alarmList.getAlarmResponseList().size()).isEqualTo(20);
    }

    @Test
    @DisplayName("알람 정보 조회하기 게시글 2개 전부 알람 설정 OFF 일때는 가져오는거 0개 ")
    void getAlarmList2() {
        //given
        for (int i = 0; i < 2; i++) {
            final Board board = boardRepository.save(BoardCreator.create(boardWriter.getId(), "title" + i, "content" + i));
            board.updateCommentAlarm(false);
            board.updateReplyCommentAlarm(false);
            for (int j = 0; j < 5; j++) {
                final Comment comment = commentRepository.save(Comment.newRootComment(commentWriter, board.getId(), "저는 반대입니다."));
                final Comment replyComment = commentRepository.save(Comment.newChildComment(comment.getId(), replyCommentWriter, board.getId(), comment.getDepth() + 1, "저는 찬성이요"));
                alarmService.createCommentAlarm(board, commentWriter.getNickName(), comment.getId());
                alarmService.createReplyCommentAlarm(board, replyCommentWriter.getNickName(), comment.getId(), replyComment.getId());
            }
        }
        //when
        final AlarmListResponse alarmList = alarmService.getAlarmList(boardWriter.getId());
        //then
        assertThat(alarmList.getAlarmResponseList().size()).isEqualTo(0);
    }

    @Test
    @Transactional
    @DisplayName("한 게시글에 댓글만 알람설정하고 대댓글은 알람설정 안했을 시 댓글 알람만 조회")
    void getAlarmList3() {
        //given
        final Board board = boardRepository.save(BoardCreator.create(boardWriter.getId(), "title", "content"));
        board.updateReplyCommentAlarm(false);
        for (int j = 0; j < 5; j++) {
            final Comment comment = commentRepository.save(Comment.newRootComment(commentWriter, board.getId(), "저는 반대입니다." + j));
            final Comment replyComment = commentRepository.save(Comment.newChildComment(comment.getId(), replyCommentWriter, board.getId(), comment.getDepth() + 1, "저는 찬성이요" + j));
            alarmService.createCommentAlarm(board, commentWriter.getNickName(), comment.getId());
            alarmService.createReplyCommentAlarm(board, replyCommentWriter.getNickName(), comment.getId(), replyComment.getId());
        }
        //when
        final AlarmListResponse alarmList = alarmService.getAlarmList(boardWriter.getId());
        //then
        assertThat(alarmList.getAlarmResponseList().size()).isEqualTo(5);
        }

    @Test
    @Transactional
    @DisplayName("한 게시글에 대댓글만 알람설정하고 댓글은 알람설정 안했을 시 댓글 알람만 조회")
    void getAlarmList4() {
        //given
        final Board board = boardRepository.save(BoardCreator.create(boardWriter.getId(), "title", "content"));
        board.updateCommentAlarm(false);
        for (int j = 0; j < 5; j++) {
            final Comment comment = commentRepository.save(Comment.newRootComment(commentWriter, board.getId(), "저는 반대입니다." + j));
            final Comment replyComment = commentRepository.save(Comment.newChildComment(comment.getId(), replyCommentWriter, board.getId(), comment.getDepth() + 1, "저는 찬성이요" + j));
            alarmService.createCommentAlarm(board, commentWriter.getNickName(), comment.getId());
            alarmService.createReplyCommentAlarm(board, replyCommentWriter.getNickName(), comment.getId(), replyComment.getId());
        }
        //when
        final AlarmListResponse alarmList = alarmService.getAlarmList(boardWriter.getId());
        //then
        assertThat(alarmList.getAlarmResponseList().size()).isEqualTo(5);
    }
}