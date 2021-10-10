package com.example.nationalpetition.domain.comment;

public interface LikeCommentRepositoryCustom {
    LikeComment findByIdAndMemberIdAndLikeCommentStatus(Long id, Long memberId, LikeCommentStatus status);
    LikeComment findByIdAndMemberId(Long id, Long memberId);
}
