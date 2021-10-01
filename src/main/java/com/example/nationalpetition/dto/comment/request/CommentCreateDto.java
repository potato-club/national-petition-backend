package com.example.nationalpetition.dto.comment.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor
public class CommentCreateDto {

    @NotBlank(message = "내용을 입력해주세요.")
    private String content;

    private Long parentId;

    @Builder
    public CommentCreateDto(String content, Long memberId, Long parentId) {
        this.content = content;
        this.parentId = parentId;
    }

}
