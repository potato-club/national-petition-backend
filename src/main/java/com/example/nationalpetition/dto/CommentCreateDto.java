package com.example.nationalpetition.dto;

import com.example.nationalpetition.domain.comment.Comment;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CommentCreateDto {

    private Long memberId;

    private String content;

    @Builder
    public CommentCreateDto(String content, Long memberId) {
        this.content = content;
        this.memberId = memberId;
    }

    public Comment toEntity(Long boardId) {
        Comment comment = Comment.builder()
                .memberId(memberId)
                .boardId(boardId)
                .content(content)
                .build();
        return comment;
    }

}
