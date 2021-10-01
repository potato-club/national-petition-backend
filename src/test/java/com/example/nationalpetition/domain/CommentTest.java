package com.example.nationalpetition.domain;

import com.example.nationalpetition.domain.comment.Comment;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class CommentTest {

    @Test
    void 댓글이_수정됐는지_확인하는_테스트() {
        // given
        String originalContent = "사실 감자는 똑똑해요";
        String newContent = "맞아요 동의해요";

        Comment comment = Comment.newRootComment(1L, 1L, originalContent);

        // when
        comment.update(newContent);

        // then
        assertThat(comment.getContent()).isEqualTo(newContent);
    }

    @Test
    void 댓글이_삭제됐는지_확인하는_테스트() {
        // given
        Comment comment = Comment.newChildComment(1L, 1L, 1L, 1, "감자는 사실...");

        // when
        comment.delete();

        // then
        assertThat(comment.isDeleted()).isEqualTo(true);
    }


}
