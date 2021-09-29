package com.example.nationalpetition.dto.comment.request;

import com.example.nationalpetition.domain.comment.LikeCommentStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class LikeCommentRequestDto {

    private Long commentId;

    private LikeCommentStatus likeCommentStatus;

    @Builder
    public LikeCommentRequestDto(Long commentId, LikeCommentStatus status) {
        this.commentId = commentId;
        this.likeCommentStatus = status;
    }

}
