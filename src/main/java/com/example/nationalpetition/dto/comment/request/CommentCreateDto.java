package com.example.nationalpetition.dto.comment.request;

import com.example.nationalpetition.config.validator.BadWord;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor
public class CommentCreateDto {

    @NotBlank(message = "내용을 입력해주세요.")
    @BadWord
    private String content;

    private Long parentId;

    @Builder
    public CommentCreateDto(String content, Long parentId) {
        this.content = content;
        this.parentId = parentId;
    }

}
