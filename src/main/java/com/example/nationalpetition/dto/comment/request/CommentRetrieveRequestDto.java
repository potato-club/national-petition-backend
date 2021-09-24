package com.example.nationalpetition.dto.comment.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CommentRetrieveRequestDto {
    private Long commentId;

    private Long boardId;

    @Builder
    public CommentRetrieveRequestDto(Long commentId, Long boardId, Long memberId) {
        this.commentId = commentId;
        this.boardId = boardId;
    }
}
