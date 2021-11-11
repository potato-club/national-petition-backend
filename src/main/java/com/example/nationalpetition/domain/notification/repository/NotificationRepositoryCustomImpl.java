package com.example.nationalpetition.domain.notification.repository;

import com.example.nationalpetition.domain.notification.Notification;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.example.nationalpetition.domain.notification.QNotification.notification;

@RequiredArgsConstructor
public class NotificationRepositoryCustomImpl implements NotificationRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Notification> findByBoardMemberId(Long memberId) {
        return queryFactory.selectFrom(notification)
                .where(
                        notification.boardMemberId.eq(memberId)
                )
                .fetch();
    }

    @Override
    public List<Notification> findByCommentMemberId(Long memberId) {
        return queryFactory.select(notification)
                .from(notification)
                .where(
                        notification.commentMemberId.eq(memberId)
                )
                .fetch();
    }

    @Override
    public List<Notification> findByBoardIdList(List<Long> boardIdList, Long memberId) {
        return queryFactory.selectFrom(notification)
                .where(
                        notification.boardId.in(boardIdList),
                        notification.commentMemberId.ne(memberId)
                )
                .fetch();
    }

    @Override
    public List<Notification> findByBoardId(Long boardId, Long commentId) {
        return queryFactory.selectFrom(notification)
                .where(
                        notification.boardId.eq(boardId),
                        notification.commentId.gt(commentId)
                )
                .fetch();
    }

}
