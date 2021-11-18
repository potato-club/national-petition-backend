package com.example.nationalpetition.domain.comment;

import java.util.List;

public interface LikeCommentRepositoryCustom {
    LikeComment findByIdAndMemberIdAndLikeCommentStatus(Comment comment, Long memberId, LikeCommentStatus status);
    LikeComment findByIdAndMemberId(Comment comment, Long memberId);
    List<LikeComment> findByCommentIdAndLikeCommentStatus(Comment comment, LikeCommentStatus likeCommentStatus);
}
