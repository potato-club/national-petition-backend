package com.example.nationalpetition.dto.comment.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor
public class CommentUpdateDto {

    @NotBlank(message = "commentId를 입력해주세요.")
    private Long commentId;

    @NotBlank(message = "변경할 내용을 입력해주세요.")
    private String content;

    @Builder
    public CommentUpdateDto(Long commentId, String content) {
        this.commentId = commentId;
        this.content = content;
    }

}
