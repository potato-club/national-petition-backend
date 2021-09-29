package com.example.nationalpetition.domain.board.repository;

import com.example.nationalpetition.domain.board.BoardLike;
import com.example.nationalpetition.domain.board.BoardState;
import com.example.nationalpetition.dto.board.response.BoardLikeAndUnLikeCounts;
import com.example.nationalpetition.dto.board.response.QBoardLikeAndUnLikeCounts;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

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

    @Override
    public Optional<BoardLikeAndUnLikeCounts> countLikeByBoardId(Long boardId) {
        return Optional.ofNullable(queryFactory.select(new QBoardLikeAndUnLikeCounts(
                        new CaseBuilder().when(boardLike.boardState.eq(BoardState.LIKE)).then(1).otherwise(Expressions.nullExpression()).count().as("boardLikeCounts"),
                        new CaseBuilder().when(boardLike.boardState.eq(BoardState.UNLIKE)).then(1).otherwise(Expressions.nullExpression()).count().as("boardUnLikeCounts")
                ))
                .from(boardLike)
                .where(
                        boardLike.boardId.eq(boardId)
                )
                .fetchOne());
    }

}
