package com.example.nationalpetition.service.notification;

import com.example.nationalpetition.domain.board.Board;
import com.example.nationalpetition.domain.board.repository.BoardRepository;
import com.example.nationalpetition.domain.comment.Comment;
import com.example.nationalpetition.domain.comment.CommentRepository;
import com.example.nationalpetition.domain.member.entity.Member;
import com.example.nationalpetition.domain.member.repository.MemberRepository;
import com.example.nationalpetition.domain.notification.Notification;
import com.example.nationalpetition.domain.notification.NotificationTemplate;
import com.example.nationalpetition.domain.notification.repository.NotificationRepository;
import com.example.nationalpetition.dto.comment.request.CommentCreateDto;
import com.example.nationalpetition.dto.notification.request.UpdateBoardNotificationRequest;
import com.example.nationalpetition.dto.notification.request.UpdateCommentNotificationRequest;
import com.example.nationalpetition.dto.notification.response.NotificationInfoResponse;
import com.example.nationalpetition.service.comment.CommentService;
import com.example.nationalpetition.testObject.BoardCreator;
import com.example.nationalpetition.testObject.CommentCreator;
import com.example.nationalpetition.testObject.MemberCreator;
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
    private NotificationService notificationService;

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
    private Member boardMember;

    @BeforeEach
    void setUpMember() {
        member1 = memberRepository.save(MemberCreator.create("nickname1"));
        member2 = memberRepository.save(MemberCreator.create("nickname2"));
        member3 = memberRepository.save(MemberCreator.create("nickname3"));
        boardMember = memberRepository.save(MemberCreator.create("boardMember"));
    }

    @Test
    void 댓글을_달면_알림이_데이터베이스에_저장된다() {
        // given
        String content = "감자는 뛰어납니다.";

        Board board = BoardCreator.create(boardMember.getId(), "title", "content");
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
        assertThat(notificationList.get(0).getNotificationTemplate()).isEqualTo(NotificationTemplate.CREATE_COMMENT);
    }

    @Test
    void 대댓글을_달면_알림이_데이터베이스에_저장된다() {
        // given
        String content = "감자는 뛰어납니다.";

        Board board = BoardCreator.create(boardMember.getId(), "title", "content");
        boardRepository.save(board);

        Comment comment = CommentCreator.create("comment1", board.getId(), null, member1, 0);
        commentRepository.save(comment);

        CommentCreateDto dto = CommentCreateDto.builder()
                .content(content)
                .parentId(comment.getId())
                .build();

        // when
        commentService.addComment(dto, board.getId(), member1.getId());

        // then
        List<Comment> commentList = commentRepository.findAll();
        List<Board> boardList = boardRepository.findAll();
        List<Notification> notificationList = notificationRepository.findAll();
        assertThat(boardList).hasSize(1);
        assertThat(commentList).hasSize(2);
        assertThat(notificationList).hasSize(1);
        assertThat(notificationList.get(0).getNotificationTemplate()).isEqualTo(NotificationTemplate.CREATE_RE_COMMENT);
    }

    @DisplayName("999번 유저가 게시글을 작성 - 3개 그냥 댓글, 나머지 1개는 대댓글 - 그냥 댓글에만 알림이 온다")
    @Test
    void 알림_리스트1() {
        // given
        Board board = BoardCreator.create(boardMember.getId(), "title", "content");
        boardRepository.save(board);

        Notification notification11 = NotificationCreator.create(boardMember.getId(), board.getId(), NotificationTemplate.CREATE_COMMENT, member1.getNickName());
        Notification notification12 = NotificationCreator.create(boardMember.getId(), board.getId(), NotificationTemplate.CREATE_COMMENT, member2.getNickName());
        Notification notification21 = NotificationCreator.create(boardMember.getId(), board.getId(), NotificationTemplate.CREATE_COMMENT, member3.getNickName());
        Notification notification31 = NotificationCreator.create(member3.getId(), board.getId(), NotificationTemplate.CREATE_RE_COMMENT, member2.getNickName());
        notificationRepository.saveAll(Arrays.asList(notification11, notification12, notification21, notification31));

        List<NotificationInfoResponse> notificationInfoResponses = notificationService.retrieveNotification(boardMember.getId());

        // then
        assertThat(notificationInfoResponses).hasSize(3);
    }

    @DisplayName("999번 유저가 게시글을 작성 - 1개 그냥 댓글, 나머지 1개는 대댓글 - 그냥 댓글에만 알림이 온다")
    @Test
    void 알림_리스트2() {
        // given
        Board board = BoardCreator.create(boardMember.getId(), "title", "content");
        boardRepository.save(board);

        // 1번이 댓글 남김
        Notification notification11 = NotificationCreator.create(board.getMemberId(), board.getId(), NotificationTemplate.CREATE_COMMENT, member1.getNickName());
        // 1번의 댓글에 2번이 대댓글 남김
        Notification notification12 = NotificationCreator.create(member1.getId(), board.getId(), NotificationTemplate.CREATE_RE_COMMENT, member2.getNickName());
        // 1번의 댓글에 3번이 대댓글 남김
        Notification notification21 = NotificationCreator.create(member1.getId(), board.getId(), NotificationTemplate.CREATE_RE_COMMENT, member3.getNickName());
        // 1번의 댓글에 1번이 댓글 남김
        Notification notification31 = NotificationCreator.create(member1.getId(), board.getId(), NotificationTemplate.CREATE_RE_COMMENT, member1.getNickName());
        notificationRepository.saveAll(Arrays.asList(notification11, notification12, notification21, notification31));

        List<NotificationInfoResponse> notificationInfoResponses = notificationService.retrieveNotification(member1.getId());

        // then
        assertThat(notificationInfoResponses).hasSize(2);
    }

    @DisplayName("전체 알림이 off이면 개별 알람이 on이여도 알람 db에 저장이 되지 않는다")
    @Test
    void 전체알람이_오프일_경우1() {
        // given
        Member member = MemberCreator.create("sunjo");
        member.updateMemberNotification(false);
        memberRepository.save(member);
        Board board = BoardCreator.create(boardMember.getId(), "title1", "content1");
        boardRepository.save(board);

        CommentCreateDto dto = CommentCreateDto.builder()
                .content("와 ~ 댓글이다")
                .build();

        // when
        commentService.addComment(dto, board.getId(), member.getId());

        // then
        List<Notification> notificationList = notificationRepository.findAll();
        assertThat(notificationList).isEmpty();
    }

    @DisplayName("전체 알림이 off이면 개별 알람이 on이여도 알람 db에 저장이 되지 않는다")
    @Test
    void 전체알람이_오프일_경우2() {
        // given
        Member member = MemberCreator.create("sunjo");
        member.updateMemberNotification(false);
        memberRepository.save(member);
        Board board = BoardCreator.create(boardMember.getId(), "title1", "content1");
        boardRepository.save(board);
        Comment comment = CommentCreator.create("와 부모댓글이다.", board.getId(), null, member1, 0);
        commentRepository.save(comment);

        CommentCreateDto dto = CommentCreateDto.builder()
                .content("와 ~ 댓글이다")
                .parentId(comment.getId())
                .build();

        // when
        commentService.addComment(dto, board.getId(), member.getId());

        // then
        List<Notification> notificationList = notificationRepository.findAll();
        assertThat(notificationList).isEmpty();
    }

    @DisplayName("전체 알람의 상태를 변경한다")
    @Test
    void 전체알람의_상태를_변경한다() {
        // given

        // when
        notificationService.updateMemberNotification(false, member1.getId());

        // then
        List<Member> memberList = memberRepository.findAll();
        assertThat(memberList).hasSize(4);
        assertThat(memberList.get(0).isNotification()).isFalse();
    }

    @DisplayName("전체알림은 on 내가 쓴 게시글 알림 상태를 off로 했을 경우")
    @Test
    void 내가_쓴_게시글_알림을_껐을_경우1() {
        // given
        Member member = MemberCreator.create("sunjo");
        memberRepository.save(member);
        Board board = BoardCreator.create(member.getId(), "title1", "content1");
        board.updateBoardNotification(false);
        boardRepository.save(board);

        CommentCreateDto dto = CommentCreateDto.builder()
                .content("와 ~ 댓글이다")
                .build();

        // when
        commentService.addComment(dto, board.getId(), member1.getId());

        // then
        List<Notification> notificationList = notificationRepository.findAll();
        assertThat(notificationList).isEmpty();
    }

    @DisplayName("전체알림은 on 내가 쓴 게시글 알림 상태를 off로 했을 경우 대댓글을 달면 알림이 저장된다")
    @Test
    void 내가_쓴_게시글_알림을_껐을_경우2() {
        // given
        Member member = MemberCreator.create("sunjo");
        memberRepository.save(member);
        Board board = BoardCreator.create(member.getId(), "title1", "content1");
        board.updateBoardNotification(false);
        boardRepository.save(board);
        Comment comment = CommentCreator.create("와 부모댓글이다.", board.getId(), null, member1, 0);
        commentRepository.save(comment);

        CommentCreateDto dto = CommentCreateDto.builder()
                .content("와 ~ 댓글이다")
                .parentId(comment.getId())
                .build();

        // when
        commentService.addComment(dto, board.getId(), member1.getId());

        // then
        List<Notification> notificationList = notificationRepository.findAll();
        assertThat(notificationList).hasSize(1);
    }

    @DisplayName("전체알림은 on 내가 쓴 게시글 알림 상태를 on, 댓글 알림을 off 일 경우 일반 댓글을 저장")
    @Test
    void 댓글의_알림을_껐을_경우1() {
        // given
        Member member = MemberCreator.create("sunjo");
        memberRepository.save(member);
        Board board = BoardCreator.create(member.getId(), "title1", "content1");
        boardRepository.save(board);
        Comment comment = CommentCreator.create("와 부모댓글이다.", board.getId(), null, member1, 0);
        comment.updateCommentNotification(false);
        commentRepository.save(comment);

        CommentCreateDto dto = CommentCreateDto.builder()
                .content("와 ~ 댓글이다")
                .build();

        // when
        commentService.addComment(dto, board.getId(), member1.getId());

        // then
        List<Notification> notificationList = notificationRepository.findAll();
        assertThat(notificationList).hasSize(1);
    }

    @DisplayName("전체알림은 on 내가 쓴 게시글 알림 상태를 on, 댓글 알림을 off 일 경우 일반 댓글을 저장")
    @Test
    void 댓글의_알림을_껐을_경우2() {
        // given
        Member member = MemberCreator.create("sunjo");
        memberRepository.save(member);
        Board board = BoardCreator.create(member.getId(), "title1", "content1");
        boardRepository.save(board);
        Comment comment = CommentCreator.create("와 부모댓글이다.", board.getId(), null, member1, 0);
        comment.updateCommentNotification(false);
        commentRepository.save(comment);

        CommentCreateDto dto = CommentCreateDto.builder()
                .content("와 ~ 댓글이다")
                .parentId(comment.getId())
                .build();

        // when
        commentService.addComment(dto, board.getId(), member1.getId());

        // then
        List<Notification> notificationList = notificationRepository.findAll();
        assertThat(notificationList).isEmpty();
    }

    @Test
    void 게시글_알림_상태_변경() {
        // given
        Member member = MemberCreator.create("sunjo");
        memberRepository.save(member);
        Board board = BoardCreator.create(member.getId(), "title1", "content1");
        boardRepository.save(board);

        UpdateBoardNotificationRequest request = UpdateBoardNotificationRequest.testInstance(board.getId(), false);

        // when
        notificationService.updateBoardNotification(request, member.getId());

        // then
        List<Board> boardList = boardRepository.findAll();
        assertThat(boardList).hasSize(1);
        assertThat(boardList.get(0).isBoardNotification()).isFalse();
    }

    @Test
    void 댓글_알림_상태_변경() {
        // given
        Member member = MemberCreator.create("sunjo");
        memberRepository.save(member);
        Board board = BoardCreator.create(member.getId(), "title1", "content1");
        boardRepository.save(board);
        Comment comment = CommentCreator.create("와 부모댓글이다.", board.getId(), null, member1, 0);
        commentRepository.save(comment);

        UpdateCommentNotificationRequest request = UpdateCommentNotificationRequest.testInstance(comment.getId(), false);
        // when
        notificationService.updateCommentNotification(request, member1.getId());

        // then
        List<Comment> commentList = commentRepository.findAll();
        assertThat(commentList).hasSize(1);
        assertThat(commentList.get(0).isCommentNotification()).isFalse();
    }

//    @DisplayName("알림 상태를 변경한다")
//    @Test
//    void 알림_상태를_변경() {
//        // given
//        Board board = BoardCreator.create(boardMember.getId(), "title", "content");
//        boardRepository.save(board);
//
//        // 1번이 댓글 남김
//        Notification notification11 = NotificationCreator.create(board.getMemberId(), board.getId(), NotificationTemplate.CREATE_COMMENT, member1.getNickName());
//        notificationRepository.save(notification11);
//
//        notificationService.notificationIsRead(notification11.getId(), board.getMemberId());
//
//        // then
//        List<Notification> notificationList = notificationRepository.findAll();
//        for (Notification notification : notificationList) {
//            System.out.println("뭐야" + notification.getId());
//        }
//        assertThat(notificationList.get(0).isRead()).isEqualTo(true);
//        assertThat(notificationList.get(1).isRead()).isEqualTo(true);
//        assertThat(notificationList.get(2).isRead()).isEqualTo(true);
//    }

}
