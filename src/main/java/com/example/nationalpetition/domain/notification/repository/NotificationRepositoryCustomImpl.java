package com.example.nationalpetition.domain.notification.repository;

import com.example.nationalpetition.domain.notification.Notification;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

import static com.example.nationalpetition.domain.notification.QNotification.notification;

@RequiredArgsConstructor
public class NotificationRepositoryCustomImpl implements NotificationRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Notification> findByNotificationMemberId(Long memberId, String nickname) {
        return queryFactory.selectFrom(notification)
                .where(
                        notification.notificationMemberId.eq(memberId),
                        notification.commentMemberNickname.ne(nickname)
                )
                .fetch();
    }

    @Override
    public Optional<Notification> findNotificationById(Long notificationId, Long memberId) {
        return Optional.ofNullable(queryFactory.selectFrom(notification)
                .where(
                        notification.id.eq(notificationId),
                        notification.notificationMemberId.eq(memberId)
                )
                .fetchOne()
        );
    }

}
