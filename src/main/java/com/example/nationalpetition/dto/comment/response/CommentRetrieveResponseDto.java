package com.example.nationalpetition.dto.comment.response;

import com.example.nationalpetition.domain.comment.Comment;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class CommentRetrieveResponseDto {

    private Long commentId;

    private Long parentId;

    private Long boardId;

    private Long memberId;

    private String content;

    private LocalDateTime createdTime;

    @Builder
    public CommentRetrieveResponseDto(Long commentId, Long boardId, Long parentId, Long memberId, String content, LocalDateTime createdTime) {
        this.commentId = commentId;
        this.parentId = parentId;
        this.boardId = boardId;
        this.memberId = memberId;
        this.content = content;
        this.createdTime = createdTime;
    }


    public static CommentRetrieveResponseDto of(Comment comment) {
        return CommentRetrieveResponseDto.builder()
                .commentId(comment.getId())
                .boardId(comment.getBoardId())
                .memberId(comment.getMemberId())
                .parentId(comment.getParentId())
                .content(comment.getContent())
                .createdTime(comment.getCreatedDate())
                .build();
    }

}
