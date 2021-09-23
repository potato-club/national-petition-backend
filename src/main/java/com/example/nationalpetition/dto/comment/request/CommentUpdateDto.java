package com.example.nationalpetition.dto.comment.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CommentUpdateDto {

    private Long id;

    private Long memberId;

    private String content;

    @Builder
    public CommentUpdateDto(Long id, Long memberId, String content) {
        this.id = id;
        this.memberId = memberId;
        this.content = content;
    }

}
