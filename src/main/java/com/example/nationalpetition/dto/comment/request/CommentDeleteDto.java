package com.example.nationalpetition.dto.comment.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CommentDeleteDto {

    private Long commentId;


    public CommentDeleteDto(Long commentId) {
        this.commentId = commentId;
    }

}
