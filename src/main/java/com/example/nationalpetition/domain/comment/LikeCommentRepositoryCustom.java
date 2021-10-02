package com.example.nationalpetition.domain.comment;

public interface LikeCommentRepositoryCustom {
    LikeComment findByIdAndMemberIdAndLikeCommentStatus(Long id, Long memberId, LikeCommentStatus status);
}
