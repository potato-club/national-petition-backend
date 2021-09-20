package com.example.nationalpetition.domain;

import com.example.nationalpetition.domain.comment.Comment;
import com.example.nationalpetition.domain.comment.CommentRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class CommentTest {
    @Autowired
    private CommentRepository commentRepository;

    @Test
    void 댓글이_수정됐는지_확인하는_테스트() {
        // given
        String originalContent = "사실 감자는 똑똑해요";
        String newContent = "맞아요 동의해요";

        Comment comment = commentRepository.save(Comment.newRootComment(1L, 1L, originalContent));

        // when
        comment.update(newContent);

        assertThat(comment.getContent()).isEqualTo(newContent);
    }

}
