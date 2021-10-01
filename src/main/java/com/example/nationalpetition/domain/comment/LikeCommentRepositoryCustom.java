package com.example.nationalpetition.domain.comment;

public interface LikeCommentRepositoryCustom {
    LikeComment findByIdAndLikeCommentStatus(Long id, LikeCommentStatus status);
}
