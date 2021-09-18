package com.example.nationalpetition.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CommentCreateDto {

    private Long memberId;

    private String content;

    private Long parentId;

    @Builder
    public CommentCreateDto(String content, Long memberId, Long parentId) {
        this.content = content;
        this.memberId = memberId;
        this.parentId = parentId;
    }

}
