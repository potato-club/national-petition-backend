package com.example.nationalpetition.service.notification;

import com.example.nationalpetition.domain.board.Board;
import com.example.nationalpetition.domain.board.repository.BoardRepository;
import com.example.nationalpetition.domain.comment.Comment;
import com.example.nationalpetition.domain.comment.CommentRepository;
import com.example.nationalpetition.domain.comment.LikeCommentRepository;
import com.example.nationalpetition.domain.member.entity.Member;
import com.example.nationalpetition.domain.member.repository.MemberRepository;
import com.example.nationalpetition.domain.notification.Notification;
import com.example.nationalpetition.domain.notification.repository.NotificationRepository;
import com.example.nationalpetition.dto.comment.request.CommentCreateDto;
import com.example.nationalpetition.dto.notification.response.NotificationInfoResponse;
import com.example.nationalpetition.service.comment.CommentService;
import com.example.nationalpetition.service.member.MemberService;
import com.example.nationalpetition.testObject.BoardCreator;
import com.example.nationalpetition.testObject.CommentCreator;
import com.example.nationalpetition.testObject.NotificationCreator;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class NotificationServiceTest {

    @Autowired
    private CommentService commentService;

    @Autowired
    private MemberService memberService;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private NotificationRepository notificationRepository;

    @AfterEach
    void cleanUp() {
        commentRepository.deleteAll();
        boardRepository.deleteAll();
        memberRepository.deleteAll();
        notificationRepository.deleteAll();
    }

    private Member member1;
    private Member member2;
    private Member member3;

    @BeforeEach
    void setUpMember() {
        member1 = memberRepository.save(Member.of("악동뮤지션1", "akmu@yg.com", "nakha.jpg"));
        member2 = memberRepository.save(Member.of("악동뮤지션2", "akmu@yg.com", "nakha.jpg"));
        member3 = memberRepository.save(Member.of("악동뮤지션3", "akmu@yg.com", "nakha.jpg"));
    }

    @Test
    void 댓글을_달면_알림이_데이터베이스에_저장된다() {
        // given
        String content = "감자는 뛰어납니다.";

        Board board = BoardCreator.create(999L, "title", "content");
        boardRepository.save(board);

        CommentCreateDto dto = CommentCreateDto.builder()
                .content(content)
                .build();

        // when
        commentService.addComment(dto, board.getId(), member1.getId());

        // then
        List<Comment> commentList = commentRepository.findAll();
        List<Board> boardList = boardRepository.findAll();
        List<Notification> notificationList = notificationRepository.findAll();
        assertThat(boardList).hasSize(1);
        assertThat(commentList).hasSize(1);
        assertThat(notificationList).hasSize(1);
    }

    @DisplayName("999번 유저가 게시글을 작성 - 1번이 2개 댓글, 2번이 1개 3번이 1개일 경우")
    @Test
    void 알림_리스트1() {
        // given
        Board board = BoardCreator.create(999L, "title", "content");
        boardRepository.save(board);
        Notification notification11 = NotificationCreator.create("content1 - 1", member1.getId(), 999L, board.getId());
        Notification notification12 = NotificationCreator.create("content1 - 2", member1.getId(), 999L, board.getId());
        Notification notification21 = NotificationCreator.create("content2 - 1", member2.getId(), 999L, board.getId());
        Notification notification31 = NotificationCreator.create("content3 - 1", member3.getId(), 999L, board.getId());
        notificationRepository.saveAll(Arrays.asList(notification11, notification12, notification21, notification31));

        List<NotificationInfoResponse> notificationInfoResponses = memberService.retrieveNotification(999L);

        // then
        assertThat(notificationInfoResponses).hasSize(4);
    }

    @DisplayName("999번 유저가 게시글을 작성 - 1번이 2개 댓글, 2번이 1개 3번이 1개일 경우")
    @Test
    void 알림_리스트2() {
        // given
        Board board = BoardCreator.create(999L, "title", "content");
        boardRepository.save(board);
        Notification notification11 = NotificationCreator.create("content1 - 1", member1.getId(), 999L, board.getId());
        Notification notification12 = NotificationCreator.create("content1 - 2", member1.getId(), 999L, board.getId());
        Notification notification21 = NotificationCreator.create("content2 - 1", member2.getId(), 999L, board.getId());
        Notification notification31 = NotificationCreator.create("content3 - 1", member3.getId(), 999L, board.getId());
        notificationRepository.saveAll(Arrays.asList(notification11, notification12, notification21, notification31));

        List<NotificationInfoResponse> notificationInfoResponses = memberService.retrieveNotification(member1.getId());

        // then
        assertThat(notificationInfoResponses).hasSize(2);
    }

    @DisplayName("999번 유저가 게시글을 작성 - 1번이 2개 댓글, 2번이 1개 3번이 1개일 경우")
    @Test
    void 알림_리스트3() {
        // given
        Board board = BoardCreator.create(999L, "title", "content");
        boardRepository.save(board);
        Notification notification11 = NotificationCreator.create("content1 - 1", member1.getId(), 999L, board.getId());
        Notification notification12 = NotificationCreator.create("content1 - 2", member1.getId(), 999L, board.getId());
        Notification notification21 = NotificationCreator.create("content2 - 1", member2.getId(), 999L, board.getId());
        Notification notification31 = NotificationCreator.create("content3 - 1", member3.getId(), 999L, board.getId());
        notificationRepository.saveAll(Arrays.asList(notification11, notification12, notification21, notification31));

        List<NotificationInfoResponse> notificationInfoResponses = memberService.retrieveNotification(member2.getId());

        // then
        assertThat(notificationInfoResponses).hasSize(3);
    }

}
