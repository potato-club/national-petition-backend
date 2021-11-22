package com.example.nationalpetition.domain.board.repository;

import com.example.nationalpetition.domain.board.Board;
import com.example.nationalpetition.domain.board.BoardCategory;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.List;
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
                                board.isDeleted.isFalse()
                        )
                        .fetchOne()
        );
    }

    @Override
    public long findBoardCountsWithMemberId(Long memberId) {
        return queryFactory.selectFrom(board)
                .where(
                        board.memberId.eq(memberId),
                        board.isDeleted.isFalse()
                ).fetchCount();
    }

    @Override
    public Page<Board> findBySearchingAndCategory(String search, BoardCategory category, Pageable pageable) {
        QueryResults<Board> results = queryFactory.selectFrom(board)
                .where(
                        board.title.contains(search)
                                .or(board.petitionTitle.contains(search)),
                        eqCategory(category),
                        board.isDeleted.isFalse()
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(
                        getOrderSpecifier(pageable.getSort()).toArray(OrderSpecifier[]::new)
                )
                .fetchResults();

        List<Board> content = results.getResults();
        long total = results.getTotal();
        return new PageImpl<>(content, pageable, total);
    }

    private List<OrderSpecifier> getOrderSpecifier(Sort sort) {
        List<OrderSpecifier> orders = new ArrayList<>();
        // Sort
        sort.stream().forEach(order -> {
            Order direction = order.isAscending() ? Order.ASC : Order.DESC;
            String prop = order.getProperty();
            PathBuilder orderByExpression = new PathBuilder(Board.class, "board");
            orders.add(new OrderSpecifier(direction, orderByExpression.get(prop)));
        });
        return orders;
    }

    private BooleanExpression eqCategory(BoardCategory category) {
        if (category == null) {
            return null;
        }
        return board.category.eq(category);
    }

}
