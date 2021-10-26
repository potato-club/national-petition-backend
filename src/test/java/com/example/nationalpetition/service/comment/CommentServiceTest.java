package com.example.nationalpetition.service.comment;

import com.example.nationalpetition.domain.board.Board;
import com.example.nationalpetition.domain.board.repository.BoardRepository;
import com.example.nationalpetition.domain.comment.*;
import com.example.nationalpetition.domain.member.entity.Member;
import com.example.nationalpetition.domain.member.repository.MemberRepository;
import com.example.nationalpetition.dto.comment.request.CommentCreateDto;
import com.example.nationalpetition.dto.comment.request.CommentUpdateDto;
import com.example.nationalpetition.dto.comment.request.LikeCommentRequestDto;
import com.example.nationalpetition.utils.error.exception.ForbiddenException;
import com.example.nationalpetition.utils.error.exception.NotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
public class CommentServiceTest {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private CommentService commentService;

    @Autowired
    private LikeCommentRepository likeCommentRepository;

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private MemberRepository memberRepository;

    @AfterEach
    void cleanUp() {
        commentRepository.deleteAll();
        likeCommentRepository.deleteAll();
        boardRepository.deleteAll();
        memberRepository.deleteAll();
    }

    private Member member;

    @BeforeEach
    void setUpMember() {
        member = memberRepository.save(Member.of("악동뮤지션", "akmu@yg.com", "nakha.jpg"));
    }

    @Test
    void 댓글을_저장한다() {
        // given
        String content = "감자는 뛰어납니다.";
        int depth = 1;

        String title = "안녕하세요.";
        String petitionUrl = "www1.national-petition.co.kr";
        String petitionsCounts = "1";
        String category = "인권";
        String petitionCreatedAt = "2021-06-06";
        String petitionFinishedAt = "2021-06-06";

        Board board = new Board(member.getId(), title, title, content, content, petitionUrl, petitionsCounts, category, petitionCreatedAt, petitionFinishedAt);
        boardRepository.save(board);

        CommentCreateDto dto = CommentCreateDto.builder()
                .content(content)
                .build();

        // when
        commentService.addComment(dto, board.getId(), member.getId());

        // then
        List<Comment> comments = commentRepository.findAll();
        List<Board> boards = boardRepository.findAll();
        assertThat(boards).hasSize(1);
        assertThat(boards.get(0).getRootCommentsCount()).isEqualTo(1);

        assertThat(comments).hasSize(1);
        assertThat(comments.get(0).getParentId()).isEqualTo(null);
        assertThat(comments.get(0).getDepth()).isEqualTo(depth);
        assertThat(comments.get(0).getContent()).isEqualTo(content);
        assertThat(comments.get(0).getBoardId()).isEqualTo(board.getId());
        assertThat(comments.get(0).getMember().getId()).isEqualTo(member.getId());
    }

    @Test
    void 대댓글을_저장한다() {
        // given
        String content = "동의합니다.";
        String petitionUrl = "www1.national-petition.co.kr";
        String petitionTitle = "초코가 너무 귀여워요...";
        String title = "국민청원";
        String petitionContent = "초코는 목걸이를 하고 있어요";
        String category = "건강/인권";
        String petitionCount = "10000000000";
        String petitionCreatedAt = "2021-06-06";
        String petitionFinishedAt = "2021-06-06";

        Board board = new Board(member.getId(), petitionTitle, title, petitionContent, content, petitionUrl, petitionCount, category, petitionCreatedAt, petitionFinishedAt);

        boardRepository.save(board);

        Comment parent = commentRepository.save(Comment.newRootComment(member, board.getId(), content));

        CommentCreateDto createDto = CommentCreateDto.builder()
                .parentId(parent.getId())
                .content(content)
                .build();

        // when
        commentService.addComment(createDto, board.getId(), member.getId());

        // then
        List<Comment> comments = commentRepository.findAll();
        assertThat(comments).hasSize(2);
        assertThat(comments.get(1).getDepth()).isEqualTo(2);
        assertThat(comments.get(1).getParentId()).isEqualTo(parent.getId());
        assertThat(comments.get(1).getMember().getId()).isEqualTo(member.getId());
        assertThat(comments.get(1).getContent()).isEqualTo(content);
        assertThat(comments.get(1).getBoardId()).isEqualTo(board.getId());
        assertThat(comments.get(0).getChildCommentsCount()).isEqualTo(1);

    }

    @Test
    void parentId에_해당하는_댓글이_없을때() {
        // given
        Long parentId = 99L;
        Long boardId = 1L;
        String content = "뛰어난 감자";

        CommentCreateDto dto = CommentCreateDto.builder()
                .parentId(parentId)
                .content(content)
                .build();

        // when & then
        assertThatThrownBy(() -> commentService.addComment(dto, boardId, member.getId()))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    void 해당하는_id가_없을때() {
        // given
        String originalContent = "감자는 소금과 먹나요?";
        String updatedContent = "감자는 설탕과 먹어요";
        Long boardId = 1L;

        commentRepository.save(Comment.newRootComment(member, boardId, originalContent));
        CommentUpdateDto dto = CommentUpdateDto.builder()
                .commentId(999L)
                .content(updatedContent)
                .build();

        // when & then
        assertThatThrownBy(() -> commentService.updateComment(member.getId(), dto))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    void 댓글을_수정한다() {
        // given
        String originalContent = "감자는 맛이 없어요 :(";
        String newContent = "사실은 맛있어요 :)";

        Comment originalComment = commentRepository.save(Comment.newRootComment(member, 1L, originalContent));
        CommentUpdateDto updateDto = CommentUpdateDto.builder()
                .commentId(originalComment.getId())
                .content(newContent)
                .build();

        // when
        commentService.updateComment(member.getId(), updateDto);

        // then
        List<Comment> comment = commentRepository.findAll();
        assertThat(comment).hasSize(1);
        assertThat(comment.get(0).getContent()).isEqualTo(updateDto.getContent());
        assertThat(comment.get(0).getMember().getId()).isEqualTo(member.getId());

    }

    @Test
    void 댓글을_삭제한다() {
        // given
        String petitionUrl = "www1.national-petition.co.kr";
        String petitionTitle = "초코가 너무 귀여워요...";
        String title = "국민청원";
        String petitionContent = "초코는 목걸이를 하고 있어요";
        String content = "초코바봉";
        String category = "건강/인권";
        String petitionCount = "10000000000";
        String petitionCreatedAt = "2021-06-06";
        String petitionFinishedAt = "2021-06-06";

        Board board = new Board(member.getId(), petitionTitle, title, petitionContent, content, petitionUrl, petitionCount, category, petitionCreatedAt, petitionFinishedAt);
        boardRepository.save(board);

        Comment comment = commentRepository.save(Comment.newRootComment(member, board.getId(), content));

        // when
        commentService.deleteComment(member.getId(), comment.getId());

        // then
        List<Comment> deletedComment = commentRepository.findAll();
        List<Board> boards = boardRepository.findAll();
        assertThat(boards).hasSize(1);
        assertThat(boards.get(0).getRootCommentsCount()).isEqualTo(0);
        assertThat(deletedComment).hasSize(1);
        assertThat(deletedComment.get(0).isDeleted()).isEqualTo(true);
        assertThat(deletedComment.get(0).getMember().getId()).isEqualTo(member.getId());
    }

    @Test
    void 댓글_좋아요를_누른다() {
        // given
        Long boardId = 1L;
        String content = "저는 감자보다 고구마가 더 좋은데요? ㅡ,.ㅡ";
        LikeCommentStatus likeStatus = LikeCommentStatus.LIKE;

        Comment comment = commentRepository.save(Comment.newRootComment(member, boardId, content));

        LikeCommentRequestDto dto = LikeCommentRequestDto.builder()
                .commentId(comment.getId())
                .status(likeStatus)
                .build();

        // when
        commentService.addStatus(member.getId(), dto);

        // then
        List<LikeComment> comments = likeCommentRepository.findAll();
        assertThat(comments.get(0).getLikeCommentStatus()).isEqualTo(likeStatus);

    }


    @Test
    void 좋아요_상태에서_싫어요() {
        // given
        Long memberId = 1L;
        Long boardId = 2L;
        String content = "댓글입니다.";
        Comment comment = Comment.newRootComment(member, boardId, content);

        commentRepository.save(comment);

        LikeCommentRequestDto likeRequestDto = LikeCommentRequestDto.builder()
                .commentId(comment.getId())
                .status(LikeCommentStatus.LIKE)
                .build();

        LikeCommentRequestDto unLikeRequestDto = LikeCommentRequestDto.builder()
                .commentId(comment.getId())
                .status(LikeCommentStatus.UNLIKE)
                .build();

        commentService.addStatus(memberId, likeRequestDto);

        // when
        commentService.addStatus(memberId, unLikeRequestDto);

        // then
        List<LikeComment> comments = likeCommentRepository.findAll();
        assertThat(comments).hasSize(1);
        assertThat(LikeCommentStatus.UNLIKE).isEqualTo(comments.get(0).getLikeCommentStatus());

    }

    @Test
    void 댓글_좋아요를_취소할때() {
        // given
        Long boardId = 1L;
        String content = "감자가 더 좋아요";
        LikeCommentStatus likeStatus = LikeCommentStatus.LIKE;

        Comment comment = commentRepository.save(Comment.newRootComment(member, boardId, content));

        likeCommentRepository.save(LikeComment.of(comment.getId(), likeStatus, member.getId()));

        LikeCommentRequestDto requestDto = LikeCommentRequestDto.builder()
                .commentId(comment.getId())
                .status(likeStatus)
                .build();

        // when
        commentService.deleteStatus(comment.getMember().getId(), requestDto);

        // then
        List<LikeComment> comments = likeCommentRepository.findAll();
        assertThat(comments).isEmpty();
    }

    @Test
    void 댓골_좋아요가_표시되었을때_좋아요를_누른다() {
        // given
        Long boardId = 1L;
        String content = "감자쨩";
        LikeCommentStatus likeCommentStatus = LikeCommentStatus.LIKE;
        LikeCommentStatus unLikeCommentStatus = LikeCommentStatus.UNLIKE;

        Comment comment = commentRepository.save(Comment.newRootComment(member, boardId, content));

        likeCommentRepository.save(LikeComment.of(comment.getId(), unLikeCommentStatus, member.getId()));

        LikeCommentRequestDto requestDto = LikeCommentRequestDto.builder()
                .commentId(comment.getId())
                .status(likeCommentStatus)
                .build();

        // when & then
        assertThatThrownBy(() -> commentService.deleteStatus(comment.getMember().getId(), requestDto))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    void 댓글뎁스제한하기() {
        // given
        String content = "234234";
        String title = "안녕하세요.";
        String petitionUrl = "www1.national-petition.co.kr";
        String petitionsCounts = "1";
        String category = "인권";
        String petitionCreatedAt = "2021-06-06";
        String petitionFinishedAt = "2021-06-06";

        Board board = new Board(member.getId(), title, title, content, content, petitionUrl, petitionsCounts, category, petitionCreatedAt, petitionFinishedAt);
        boardRepository.save(board);

        Comment comment = commentRepository.save(Comment.newRootComment(member, board.getId(), content));

        Comment bigComment = Comment.newChildComment(comment.getParentId(), member, comment.getBoardId(), comment.getDepth() + 1, comment.getContent());

        commentRepository.save(bigComment);

        CommentCreateDto dto = CommentCreateDto.builder().parentId(bigComment.getId())
                .content(content)
                .build();

        // when & then
        assertThatThrownBy(() -> commentService.addComment(dto, comment.getBoardId(), member.getId()))
                .isInstanceOf(ForbiddenException.class);

    }

}