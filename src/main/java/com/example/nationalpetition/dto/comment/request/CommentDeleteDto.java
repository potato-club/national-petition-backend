package com.example.nationalpetition.dto.comment.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CommentDeleteDto {

    private Long commentId;

    private Long memberId;

    public CommentDeleteDto(Long commentId, Long memberId) {
        this.commentId = commentId;
        this.memberId = memberId;
    }

}
