package com.example.nationalpetition.domain.notification.repository;

import com.example.nationalpetition.domain.notification.Notification;

import java.util.List;
import java.util.Optional;

public interface NotificationRepositoryCustom {

    List<Notification> findByNotificationMemberId(Long memberId);

    Optional<Notification> findNotificationById(Long notificationId, Long memberId);

}
