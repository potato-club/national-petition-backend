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
    public CommentCreateDto(Long memberId, String content) {
        this.memberId = memberId;
        this.content = content;
    }

    public Comment toEntity(Long memberId, Long boardId) {
        Comment comment = Comment.builder()
                .memberId(memberId)
                .boardId(boardId)
                .content(content)
                .build();
        return comment;
    }

}
