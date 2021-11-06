package com.example.nationalpetition.domain.notification.repository;

import com.example.nationalpetition.domain.notification.Notification;

import java.util.List;

public interface NotificationRepositoryCustom {

    List<Notification> findByNotificationMemberId(Long memberId);

}
