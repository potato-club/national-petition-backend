package com.example.nationalpetition.dto.comment;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CommentCreateDto {

    private String content;

    private Long parentId;

    @Builder
    public CommentCreateDto(String content, Long memberId, Long parentId) {
        this.content = content;
        this.parentId = parentId;
    }

}
