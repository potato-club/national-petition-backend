package com.example.nationalpetition.dto.comment.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CommentRetrieveRequestDto {

    private Long commentId;

    @Builder
    public CommentRetrieveRequestDto(Long commentId) {
        this.commentId = commentId;
    }
}
