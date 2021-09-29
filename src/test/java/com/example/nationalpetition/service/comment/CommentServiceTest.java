package com.example.nationalpetition.service.comment;

import com.example.nationalpetition.domain.comment.*;
import com.example.nationalpetition.dto.comment.CommentCreateDto;
import com.example.nationalpetition.dto.comment.request.CommentUpdateDto;
import com.example.nationalpetition.dto.comment.request.LikeCommentRequestDto;
import com.example.nationalpetition.dto.comment.response.CommentRetrieveResponseDto;
import com.example.nationalpetition.utils.error.exception.NotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.stream.Collectors;

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

    @AfterEach
    void cleanUp() {
        commentRepository.deleteAll();
    }

    @Test
    void 댓글을_저장한다() {
        // given
        String content = "감자는 뛰어납니다.";
        Long boardId = 1L;
        Long memberId = 1L;
        int depth = 1;

        CommentCreateDto dto = CommentCreateDto.builder()
                .content(content)
                .memberId(memberId)
                .build();

        // when
        commentService.addComment(dto, boardId, memberId);

        // then
        List<Comment> comments = commentRepository.findAll();

        assertThat(comments).hasSize(1);

        assertThat(comments.get(0).getParentId()).isEqualTo(null);
        assertThat(comments.get(0).getDepth()).isEqualTo(depth);
        assertThat(comments.get(0).getContent()).isEqualTo(content);
        assertThat(comments.get(0).getBoardId()).isEqualTo(boardId);
        assertThat(comments.get(0).getMemberId()).isEqualTo(memberId);
    }

    @Test
    void 대댓글을_저장한다() {
        // given
        String content = "동의합니다.";
        Long boardId = 1L;
        Long memberId = 1L;

        Comment parent = commentRepository.save(Comment.newRootComment(memberId, boardId, content));

        CommentCreateDto createDto = CommentCreateDto.builder()
                .parentId(parent.getId())
                .content(content)
                .build();

        // when
        commentService.addComment(createDto, boardId, memberId);

        // then
        List<Comment> comments = commentRepository.findAll();
        assertThat(comments).hasSize(2);

        assertThat(comments.get(1).getDepth()).isEqualTo(2);
        assertThat(comments.get(1).getParentId()).isEqualTo(parent.getId());
        assertThat(comments.get(1).getMemberId()).isEqualTo(memberId);
        assertThat(comments.get(1).getContent()).isEqualTo(content);
        assertThat(comments.get(1).getBoardId()).isEqualTo(boardId);
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
        Comment comment = commentRepository.save(Comment.newRootComment(1L, 1L, "치킨이 더 맛있어요"));
        Long memberId = 1L;
        Long commentId = comment.getId();

        // when
        commentService.deleteComment(memberId, commentId);

        // then
        List<Comment> deletedComment = commentRepository.findAll();
        assertThat(deletedComment).hasSize(1);
        assertThat(deletedComment.get(0).isDeleted()).isEqualTo(true);
        assertThat(deletedComment.get(0).getMemberId()).isEqualTo(memberId);
    }

    @Test
    void 댓글을_불러온다() {
        // given
        Long memberId = 1L;
        Long boardId = 1L;
        String content = "첫 번째 게시물 입니다.";

        commentRepository.save(Comment.newRootComment(memberId, boardId, content));

        CommentRetrieveResponseDto responseDto = CommentRetrieveResponseDto.builder()
                .commentId(1L)
                .boardId(boardId)
                .content(content)
                .memberId(memberId)
                .build();

        // when
        commentService.retrieveComments(boardId);

        // then
        List<Comment> dto = commentRepository.findAll().stream().collect(Collectors.toList());
        assertThat(dto).hasSize(1);
        assertThat(responseDto.getMemberId()).isEqualTo(memberId);
        assertThat(responseDto.getContent()).isEqualTo(content);

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
    void 댓글_좋아요를_누른상태에서_싫어요를_누른다() {
        // given
        Long memberId = 1L;
        Long boardId = 1L;
        String content = "저는 감자보다 고구마가 더 좋은데요? ㅡ,.ㅡ";
        LikeCommentStatus likeStatus = LikeCommentStatus.LIKE;
        LikeCommentStatus unLikeStatus = LikeCommentStatus.UNLIKE;

        Comment comment = commentRepository.save(Comment.newRootComment(memberId, boardId, content));

        LikeComment likeComment = likeCommentRepository.save(LikeComment.of(comment.getId(), likeStatus, memberId));

        LikeCommentRequestDto requestDto = LikeCommentRequestDto.builder()
                .commentId(comment.getId())
                .status(unLikeStatus)
                .build();

        // when
        commentService.addStatus(likeComment.getMemberId(), requestDto);

        // then
        assertThat(requestDto.getLikeCommentStatus()).isEqualTo(unLikeStatus);

    }

}