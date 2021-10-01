package com.example.nationalpetition.domain.comment;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.example.nationalpetition.domain.comment.QComment.comment;

@RequiredArgsConstructor
public class CommentRepositoryCustomImpl implements CommentRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    @Override
    public Comment findByIdAndMemberIdAndIsDeletedIsFalse(Long id, Long memberId) {
        return queryFactory.selectFrom(comment)
                .where(
                        comment.id.eq(id),
                        comment.memberId.eq(memberId),
                        comment.isDeleted.isFalse()
                ).fetchOne();
    }

    public List<Comment> findByBoardIdAndIsDeletedIsFalse(Long boardId) {
        return queryFactory.selectFrom(comment)
                .where(
                        comment.boardId.eq(boardId),
                        comment.isDeleted.isFalse()
                ).fetch();
    }

    @Override
    public Long findCommentCountByBoardIdAndIsDeletedIsFalse(Long boardId) {
        return queryFactory
                .selectFrom(comment)
                .where(comment.boardId.eq(boardId),
                        comment.isDeleted.isFalse())
                .fetchCount();
    }
}
