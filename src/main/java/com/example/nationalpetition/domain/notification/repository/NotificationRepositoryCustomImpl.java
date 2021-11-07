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
    public List<Notification> findByNotificationNotificationMemberId(Long memberId) {
        return queryFactory.selectFrom(notification)
                .where(
                        notification.notificationMemberId.eq(memberId)
                )
                .fetch();
    }

    @Override
    public List<Long> findByWriteMemberId(Long memberId) {
        return queryFactory.select(notification.writeMemberId)
                .from(notification)
                .where(
                        notification.writeMemberId.eq(memberId)
                )
                .fetch();
    }

    @Override
    public List<Notification> findByBoardIdList(List<Long> boardIdList, Long memberId) {
        return queryFactory.selectFrom(notification)
                .where(
                        notification.boardId.in(boardIdList),
                        notification.writeMemberId.ne(memberId)
                )
                .fetch();
    }

}
