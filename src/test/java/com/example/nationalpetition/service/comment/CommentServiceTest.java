package com.example.nationalpetition.service.comment;

import com.example.nationalpetition.domain.comment.Comment;
import com.example.nationalpetition.domain.comment.CommentRepository;
import com.example.nationalpetition.dto.comment.CommentCreateDto;
import com.example.nationalpetition.dto.comment.request.CommentUpdateDto;
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
        commentService.addComment(dto, boardId);

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
                .memberId(memberId)
                .build();

        // when
        commentService.addComment(createDto, boardId);

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
                .memberId(memberId)
                .build();

        // when & then
        assertThatThrownBy(() -> commentService.addComment(dto, boardId))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    void 해당하는_id가_없을때() {
        // given
        String originalContent = "감자는 소금과 먹나요?";
        String updatedContent = "감자는 설탕과 먹어요";

        Comment savedComment = commentRepository.save(Comment.newRootComment(1L, 1L, originalContent));
        CommentUpdateDto dto = CommentUpdateDto.builder()
                .id(savedComment.getId())
                .memberId(2L)
                .content(updatedContent)
                .build();

        // when & then
        assertThatThrownBy(() -> commentService.updateComment(dto))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    void 댓글을_수정한다() {
        // given
        String originalContent = "감자는 맛이 없어요 :(";
        String newContent = "사실은 맛있어요 :)";

        Comment originalComment = commentRepository.save(Comment.newRootComment(1L, 1L, originalContent));
        CommentUpdateDto updateDto = CommentUpdateDto.builder()
                .id(originalComment.getId())
                .memberId(originalComment.getMemberId())
                .content(newContent)
                .build();

        // when
        commentService.updateComment(updateDto);

        // then
        List<Comment> comment = commentRepository.findAll();
        assertThat(comment).hasSize(1);
        assertThat(comment.get(0).getContent()).isEqualTo(updateDto.getContent());

    }

}
