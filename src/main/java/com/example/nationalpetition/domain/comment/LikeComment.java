package com.example.nationalpetition.domain.comment;

import com.example.nationalpetition.domain.BaseTimeEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
public class LikeComment extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "comment_id")
    private Comment comment;

    private Long memberId;

    private LikeCommentStatus likeCommentStatus;

    @Builder
    public LikeComment(Comment comment, LikeCommentStatus likeCommentStatus, Long memberId) {
        this.comment = comment;
        this.likeCommentStatus = likeCommentStatus;
        this.memberId = memberId;
    }

    public static LikeComment of(Comment comment, LikeCommentStatus status, Long memberId) {
        return LikeComment.builder()
                .comment(comment)
                .memberId(memberId)
                .likeCommentStatus(status)
                .build();
    }

    public void update(LikeCommentStatus status) {
        this.likeCommentStatus = status;
    }
}
