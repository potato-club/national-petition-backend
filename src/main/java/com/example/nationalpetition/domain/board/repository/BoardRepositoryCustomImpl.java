package com.example.nationalpetition.domain.board.repository;

import com.example.nationalpetition.domain.board.Board;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

import static com.example.nationalpetition.domain.board.QBoard.board;

@RequiredArgsConstructor
public class BoardRepositoryCustomImpl implements BoardRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<Board> findByIdAndMemberId(Long boardId, Long memberId) {
        return Optional.ofNullable(
                queryFactory.selectFrom(board)
                        .where(
                                board.id.eq(boardId),
                                board.memberId.eq(memberId),
                                board.isDeleted.eq(Boolean.FALSE)
                        )
                        .fetchOne()
        );
    }

    @Override
    public long findBoardCounts(String search) {
        return queryFactory.selectFrom(board)
                .where(
                        board.title.contains(search)
                                .or(board.petitionTitle.contains(search))
                ).fetchCount();
    }

}
