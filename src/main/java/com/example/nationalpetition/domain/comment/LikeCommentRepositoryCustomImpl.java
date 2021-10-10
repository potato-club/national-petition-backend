package com.example.nationalpetition.domain.comment;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import static com.example.nationalpetition.domain.comment.QLikeComment.likeComment;

@RequiredArgsConstructor
public class LikeCommentRepositoryCustomImpl implements LikeCommentRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    @Override
    public LikeComment findByIdAndMemberIdAndLikeCommentStatus(Long id, Long memberId, LikeCommentStatus status) {
        return queryFactory.selectFrom(likeComment)
                .where(
                        likeComment.commentId.eq(id),
                        likeComment.memberId.eq(memberId),
                        likeComment.likeCommentStatus.eq(status)
                ).fetchOne();
    }

    @Override
    public LikeComment findByIdAndMemberId(Long id, Long memberId) {
        return queryFactory.selectFrom(likeComment)
                .where(
                        likeComment.commentId.eq(id),
                        likeComment.memberId.eq(memberId)
                ).fetchOne();
    }

}
