package com.example.nationalpetition.domain.board.repository;

import com.example.nationalpetition.domain.board.BoardLike;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import static com.example.nationalpetition.domain.board.QBoardLike.boardLike;

@RequiredArgsConstructor
public class BoardLikeRepositoryCustomImpl implements BoardLikeRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public BoardLike findByBoardIdAndMemberId(Long boardId, Long memberId) {
        return queryFactory.selectFrom(boardLike)
                .where(
                        boardLike.memberId.eq(memberId),
                        boardLike.boardId.eq(boardId)
                )
                .fetchOne();

    }
}
