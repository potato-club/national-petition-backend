package com.example.nationalpetition.domain.comment;

public interface LikeCommentRepositoryCustom {
    LikeComment findByIdAndMemberIdAndLikeCommentStatus(Comment comment, Long memberId, LikeCommentStatus status);
    LikeComment findByIdAndMemberId(Comment comment, Long memberId);
}
