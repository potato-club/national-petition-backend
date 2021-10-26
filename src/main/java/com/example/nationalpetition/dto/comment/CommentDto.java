package com.example.nationalpetition.dto.comment;

import com.example.nationalpetition.domain.comment.Comment;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class CommentDto {

    private static final String DELETED_MESSAGE = "삭제된 메시지 입니다";

    private Long commentId;

    private Long memberId;

    private String content;

    private int depth;

    private long childrenCounts;

    private LocalDateTime createdAt;

    private String nickName;

    @Builder
    private CommentDto(Long commentId, Long memberId, String content, int depth, long childrenCounts, LocalDateTime createdAt, String nickName) {
        this.commentId = commentId;
        this.memberId = memberId;
        this.content = content;
        this.depth = depth;
        this.childrenCounts = childrenCounts;
        this.createdAt = createdAt;
        this.nickName = nickName;

    }

    public static CommentDto of(Comment comment) {
        if (comment.isDeleted()) {
            return deletedComment(comment);
        }
        return CommentDto.builder()
                .commentId(comment.getId())
                .memberId(comment.getMember().getId())
                .nickName(comment.getMember().getNickName())
                .content(comment.getContent())
                .depth(comment.getDepth())
                .childrenCounts(comment.getChildCommentsCount())
                .createdAt(comment.getCreatedDate())
                .build();
    }

    private static CommentDto deletedComment(Comment comment) {
        return CommentDto.builder()
                .commentId(comment.getId())
                .memberId(null)
                .content(DELETED_MESSAGE)
                .depth(comment.getDepth())
                .childrenCounts(comment.getChildCommentsCount())
                .createdAt(comment.getCreatedDate())
                .build();
    }

}
