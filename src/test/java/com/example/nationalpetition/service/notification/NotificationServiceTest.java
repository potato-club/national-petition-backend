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
    void ?????????_??????_?????????_?????????????????????_????????????() {
        // given
        String content = "????????? ???????????????.";

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
    void ????????????_??????_?????????_?????????????????????_????????????() {
        // given
        String content = "????????? ???????????????.";

        Board board = BoardCreator.create(boardMember.getId(), "title", "content");
        boardRepository.save(board);

        Comment comment = CommentCreator.create("comment1", board.getId(), null, member1, 0);
        commentRepository.save(comment);

        CommentCreateDto dto = CommentCreateDto.builder()
                .content(content)
                .parentId(comment.getId())
                .build();

        // when
        commentService.addComment(dto, board.getId(), member2.getId());

        // then
        List<Comment> commentList = commentRepository.findAll();
        List<Board> boardList = boardRepository.findAll();
        List<Notification> notificationList = notificationRepository.findAll();
        assertThat(boardList).hasSize(1);
        assertThat(commentList).hasSize(2);
        assertThat(notificationList).hasSize(1);
        assertThat(notificationList.get(0).getNotificationTemplate()).isEqualTo(NotificationTemplate.CREATE_RE_COMMENT);
    }

    @DisplayName("999??? ????????? ???????????? ?????? - 3??? ?????? ??????, ????????? 1?????? ????????? - ?????? ???????????? ????????? ??????")
    @Test
    void ??????_?????????1() {
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

    @DisplayName("999??? ????????? ???????????? ?????? - 1??? ?????? ??????, ????????? 1?????? ????????? - ?????? ???????????? ????????? ??????")
    @Test
    void ??????_?????????2() {
        // given
        Board board = BoardCreator.create(boardMember.getId(), "title", "content");
        boardRepository.save(board);

        // 1?????? ?????? ??????
        Notification notification11 = NotificationCreator.create(board.getMemberId(), board.getId(), NotificationTemplate.CREATE_COMMENT, member1.getNickName());
        // 1?????? ????????? 2?????? ????????? ??????
        Notification notification12 = NotificationCreator.create(member1.getId(), board.getId(), NotificationTemplate.CREATE_RE_COMMENT, member2.getNickName());
        // 1?????? ????????? 3?????? ????????? ??????
        Notification notification21 = NotificationCreator.create(member1.getId(), board.getId(), NotificationTemplate.CREATE_RE_COMMENT, member3.getNickName());
        // 1?????? ????????? 1?????? ?????? ??????
        Notification notification31 = NotificationCreator.create(member1.getId(), board.getId(), NotificationTemplate.CREATE_RE_COMMENT, member1.getNickName());
        notificationRepository.saveAll(Arrays.asList(notification11, notification12, notification21, notification31));

        List<NotificationInfoResponse> notificationInfoResponses = notificationService.retrieveNotification(member1.getId());

        // then
        assertThat(notificationInfoResponses).hasSize(3);
    }

    @DisplayName("?????? ????????? off?????? ?????? ????????? on????????? ?????? db??? ????????? ?????? ?????????")
    @Test
    void ???????????????_?????????_??????1() {
        // given
        Member member = MemberCreator.create("sunjo");
        member.updateMemberNotification(false);
        memberRepository.save(member);
        Board board = BoardCreator.create(boardMember.getId(), "title1", "content1");
        boardRepository.save(board);

        CommentCreateDto dto = CommentCreateDto.builder()
                .content("??? ~ ????????????")
                .build();

        // when
        commentService.addComment(dto, board.getId(), member.getId());

        // then
        List<Notification> notificationList = notificationRepository.findAll();
        assertThat(notificationList).isEmpty();
    }

    @DisplayName("?????? ????????? off?????? ?????? ????????? on????????? ?????? db??? ????????? ?????? ?????????")
    @Test
    void ???????????????_?????????_??????2() {
        // given
        Member member = MemberCreator.create("sunjo");
        member.updateMemberNotification(false);
        memberRepository.save(member);
        Board board = BoardCreator.create(boardMember.getId(), "title1", "content1");
        boardRepository.save(board);
        Comment comment = CommentCreator.create("??? ??????????????????.", board.getId(), null, member1, 0);
        commentRepository.save(comment);

        CommentCreateDto dto = CommentCreateDto.builder()
                .content("??? ~ ????????????")
                .parentId(comment.getId())
                .build();

        // when
        commentService.addComment(dto, board.getId(), member.getId());

        // then
        List<Notification> notificationList = notificationRepository.findAll();
        assertThat(notificationList).isEmpty();
    }

    @DisplayName("?????? ????????? ????????? ????????????")
    @Test
    void ???????????????_?????????_????????????() {
        // given

        // when
        notificationService.updateMemberNotification(false, member1.getId());

        // then
        List<Member> memberList = memberRepository.findAll();
        assertThat(memberList).hasSize(4);
        assertThat(memberList.get(0).isNotification()).isFalse();
    }

//    @DisplayName("?????? ????????? ????????????")
//    @Test
//    void ??????_?????????_??????() {
//        // given
//        Board board = BoardCreator.create(boardMember.getId(), "title", "content");
//        boardRepository.save(board);
//
//        // 1?????? ?????? ??????
//        Notification notification11 = NotificationCreator.create(board.getMemberId(), board.getId(), NotificationTemplate.CREATE_COMMENT, member1.getNickName());
//        notificationRepository.save(notification11);
//
//        notificationService.notificationIsRead(notification11.getId(), board.getMemberId());
//
//        // then
//        List<Notification> notificationList = notificationRepository.findAll();
//        for (Notification notification : notificationList) {
//            System.out.println("??????" + notification.getId());
//        }
//        assertThat(notificationList.get(0).isRead()).isEqualTo(true);
//        assertThat(notificationList.get(1).isRead()).isEqualTo(true);
//        assertThat(notificationList.get(2).isRead()).isEqualTo(true);
//    }

}
