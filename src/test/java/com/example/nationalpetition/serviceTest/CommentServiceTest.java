package com.example.nationalpetition.serviceTest;

import com.example.nationalpetition.domain.comment.Comment;
import com.example.nationalpetition.domain.comment.CommentRepository;
import com.example.nationalpetition.dto.CommentCreateDto;
import com.example.nationalpetition.service.comment.CommentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class CommentServiceTest {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private CommentService service;

    @Test
    void 댓글을_저장한다() {
        // given
        String content = "감자는 뛰어납니다.";
        Long memberId = 1L;
        Long boardId = 1L;
        CommentCreateDto dto = CommentCreateDto.builder()
                .content(content)
                .memberId(memberId)
                .build();

        // when
        service.save(dto, boardId);

        // then
        Comment comment = commentRepository.findAll().get(0);
        assertThat(comment.getContent()).isEqualTo(dto.getContent());
        assertThat(comment.getBoardId()).isEqualTo(boardId);
        assertThat(comment.getMemberId()).isEqualTo(dto.getMemberId());

    }

}
