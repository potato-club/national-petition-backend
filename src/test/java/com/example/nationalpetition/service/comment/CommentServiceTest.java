package com.example.nationalpetition.service.comment;

import com.example.nationalpetition.domain.board.Board;
import com.example.nationalpetition.domain.board.repository.BoardRepository;
import com.example.nationalpetition.domain.comment.*;
import com.example.nationalpetition.dto.comment.request.CommentCreateDto;
import com.example.nationalpetition.dto.comment.request.CommentUpdateDto;
import com.example.nationalpetition.dto.comment.request.LikeCommentRequestDto;
import com.example.nationalpetition.utils.error.exception.NotFoundException;
import org.junit.jupiter.api.AfterEach;
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

    @AfterEach
    void cleanUp() {
        commentRepository.deleteAll();
        likeCommentRepository.deleteAll();
        boardRepository.deleteAll();
    }

    @Test
    void 댓글을_저장한다() {
        // given
        String content = "감자는 뛰어납니다.";
        Long memberId = 1L;
        int depth = 1;
        String title = "안녕하세요.";
        String petitionUrl = "www1.national-petition.co.kr";
        String petitionsCounts = "1";
        String category = "인권";
        String petitionCreatedAt = "2021-06-06";
        String petitionFinishedAt = "2021-06-06";

        Board board = new Board(memberId, title, title, content, content, petitionUrl, petitionsCounts, category, petitionCreatedAt, petitionFinishedAt);
        boardRepository.save(board);

        CommentCreateDto dto = CommentCreateDto.builder()
                .content(content)
                .memberId(memberId)
                .build();

        // when
        commentService.addComment(dto, board.getId(), memberId);

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
        assertThat(comments.get(0).getMemberId()).isEqualTo(memberId);
    }

    @Test
    void 대댓글을_저장한다() {
        // given
        String content = "동의합니다.";
        Long memberId = 1L;
        String petitionUrl = "www1.national-petition.co.kr";
        String petitionTitle = "초코가 너무 귀여워요...";
        String title = "국민청원";
        String petitionContent = "초코는 목걸이를 하고 있어요";
        String category = "건강/인권";
        String petitionCount = "10000000000";
        String petitionCreatedAt = "2021-06-06";
        String petitionFinishedAt = "2021-06-06";

        Board board = new Board(memberId, petitionTitle, title, petitionContent, content, petitionUrl, petitionCount, category, petitionCreatedAt, petitionFinishedAt);

        boardRepository.save(board);

        Comment parent = commentRepository.save(Comment.newRootComment(memberId, board.getId(), content));

        CommentCreateDto createDto = CommentCreateDto.builder()
                .parentId(parent.getId())
                .content(content)
                .build();

        // when
        commentService.addComment(createDto, board.getId(), memberId);

        // then
        List<Comment> comments = commentRepository.findAll();
        assertThat(comments).hasSize(2);

        assertThat(comments.get(1).getDepth()).isEqualTo(2);
        assertThat(comments.get(1).getParentId()).isEqualTo(parent.getId());
        assertThat(comments.get(1).getMemberId()).isEqualTo(memberId);
        assertThat(comments.get(1).getContent()).isEqualTo(content);
        assertThat(comments.get(1).getBoardId()).isEqualTo(board.getId());

    }

    @Test
    void parentId에_해당하는_댓글이_없을때() {
        // given
        Long parentId = 99L;
        Long memberId = 1L;
        Long boardId = 1L;
        String content = "뛰어난 감자";

        CommentCreateDto dto = CommentCreateDto.builder()
                .parentId(parentId)
                .content(content)
                .build();

        // when & then
        assertThatThrownBy(() -> commentService.addComment(dto, boardId, memberId))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    void 해당하는_id가_없을때() {
        // given
        String originalContent = "감자는 소금과 먹나요?";
        String updatedContent = "감자는 설탕과 먹어요";
        Long memberId = 2L;
        Long boardId = 1L;


        commentRepository.save(Comment.newRootComment(memberId, boardId, originalContent));
        CommentUpdateDto dto = CommentUpdateDto.builder()
                .commentId(999L)
                .content(updatedContent)
                .build();

        // when & then
        assertThatThrownBy(() -> commentService.updateComment(memberId, dto))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    void 댓글을_수정한다() {
        // given
        String originalContent = "감자는 맛이 없어요 :(";
        String newContent = "사실은 맛있어요 :)";
        Long memberId = 1L;

        Comment originalComment = commentRepository.save(Comment.newRootComment(memberId, 1L, originalContent));
        CommentUpdateDto updateDto = CommentUpdateDto.builder()
                .commentId(originalComment.getId())
                .content(newContent)
                .build();

        // when
        commentService.updateComment(memberId, updateDto);

        // then
        List<Comment> comment = commentRepository.findAll();
        assertThat(comment).hasSize(1);
        assertThat(comment.get(0).getContent()).isEqualTo(updateDto.getContent());
        assertThat(comment.get(0).getMemberId()).isEqualTo(memberId);

    }

    @Test
    void 댓글을_삭제한다() {
        // given
        Long memberId = 1L;
        String petitionUrl = "www1.national-petition.co.kr";
        String petitionTitle = "초코가 너무 귀여워요...";
        String title = "국민청원";
        String petitionContent = "초코는 목걸이를 하고 있어요";
        String content = "초코바봉";
        String category = "건강/인권";
        String petitionCount = "10000000000";
        String petitionCreatedAt = "2021-06-06";
        String petitionFinishedAt = "2021-06-06";

        Board board = new Board(memberId, petitionTitle, title, petitionContent, content, petitionUrl, petitionCount, category, petitionCreatedAt, petitionFinishedAt);boardRepository.save(board);

        Comment comment = commentRepository.save(Comment.newRootComment(memberId, board.getId(), content));

        // when
        commentService.deleteComment(memberId, comment.getId());

        // then
        List<Comment> deletedComment = commentRepository.findAll();
        List<Board> boards = boardRepository.findAll();
        assertThat(boards).hasSize(1);
        assertThat(boards.get(0).getRootCommentsCount()).isEqualTo(0);
        assertThat(deletedComment).hasSize(1);
        assertThat(deletedComment.get(0).isDeleted()).isEqualTo(true);
        assertThat(deletedComment.get(0).getMemberId()).isEqualTo(memberId);
    }

    @Test
    void 댓글_좋아요를_누른다() {
        // given
        Long memberId = 1L;
        Long boardId = 1L;
        String content = "저는 감자보다 고구마가 더 좋은데요? ㅡ,.ㅡ";
        LikeCommentStatus likeStatus = LikeCommentStatus.LIKE;

        Comment comment = commentRepository.save(Comment.newRootComment(memberId, boardId, content));

        LikeCommentRequestDto dto = LikeCommentRequestDto.builder()
                .commentId(comment.getId())
                .status(likeStatus)
                .build();

        // when
        commentService.addStatus(memberId, dto);

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
        Comment comment = Comment.newRootComment(memberId, boardId, content);

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
        Long memberId = 1L;
        Long boardId = 1L;
        String content = "감자가 더 좋아요";
        LikeCommentStatus likeStatus = LikeCommentStatus.LIKE;

        Comment comment = commentRepository.save(Comment.newRootComment(memberId, boardId, content));

        likeCommentRepository.save(LikeComment.of(comment.getId(), likeStatus, memberId));

        LikeCommentRequestDto requestDto = LikeCommentRequestDto.builder()
                .commentId(comment.getId())
                .status(likeStatus)
                .build();

        // when
        commentService.deleteStatus(comment.getMemberId(), requestDto);

        // then
        List<LikeComment> comments = likeCommentRepository.findAll();
        assertThat(comments).isEmpty();
    }

    @Test
    void 댓골_좋아요가_표시되었을때_좋아요를_누른다() {
        // given
        Long memberId = 1L;
        Long boardId = 1L;
        String content = "감자쨩";
        LikeCommentStatus likeCommentStatus = LikeCommentStatus.LIKE;
        LikeCommentStatus unLikeCommentStatus = LikeCommentStatus.UNLIKE;

        Comment comment = commentRepository.save(Comment.newRootComment(memberId, boardId, content));

        likeCommentRepository.save(LikeComment.of(comment.getId(), unLikeCommentStatus, memberId));

        LikeCommentRequestDto requestDto = LikeCommentRequestDto.builder()
                .commentId(comment.getId())
                .status(likeCommentStatus)
                .build();

        // when & then
        assertThatThrownBy(() -> commentService.deleteStatus(comment.getMemberId(), requestDto))
                .isInstanceOf(NotFoundException.class);
    }

}