package com.example.nationalpetition.domain.comment;

import com.example.nationalpetition.domain.BaseTimeEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Getter
@NoArgsConstructor
@Entity
public class LikeComment extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long commentId;

    private Long memberId;

    private LikeCommentStatus likeCommentStatus;

    @Builder
    public LikeComment(Long commentId, LikeCommentStatus likeCommentStatus, Long memberId) {
        this.commentId = commentId;
        this.likeCommentStatus = likeCommentStatus;
        this.memberId = memberId;
    }

    public static LikeComment of(Long commentId, LikeCommentStatus status, Long memberId) {
        return LikeComment.builder()
                .commentId(commentId)
                .memberId(memberId)
                .likeCommentStatus(status)
                .build();
    }

    public void update(LikeCommentStatus status) {
        this.likeCommentStatus = status;
    }
}
