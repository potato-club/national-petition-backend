package com.example.nationalpetition.dto.comment;

import com.example.nationalpetition.domain.comment.Comment;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class CommentDto {
    private Long id;

    private Long memberId;

    private Long parentId;

    private String content;

    private int depth;

    private boolean isDeleted;

    private LocalDateTime localDateTime;

    private CommentDto(Comment comment) {
        this.memberId = comment.getMemberId();
        this.parentId = comment.getParentId();
        this.content = comment.getContent();
        this.depth = comment.getDepth();
        this.isDeleted = comment.isDeleted();
        this.localDateTime = comment.getCreatedDate();
    }

    public static CommentDto of(Comment comment) {
        return new CommentDto(comment);
    }

}
