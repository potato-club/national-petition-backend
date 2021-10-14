package com.example.nationalpetition.dto.comment.request;

import com.example.nationalpetition.domain.comment.LikeCommentStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
public class LikeCommentRequestDto {

    @NotNull(message = "commentId를 입력해주세요.")
    private Long commentId;

    @Enumerated(EnumType.STRING)
    private LikeCommentStatus likeCommentStatus;

    @Builder
    public LikeCommentRequestDto(Long commentId, LikeCommentStatus status) {
        this.commentId = commentId;
        this.likeCommentStatus = status;
    }

}
