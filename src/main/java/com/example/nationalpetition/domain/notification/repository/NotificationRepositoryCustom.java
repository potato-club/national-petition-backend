package com.example.nationalpetition.domain.notification.repository;

import com.example.nationalpetition.domain.notification.Notification;

import java.util.List;

public interface NotificationRepositoryCustom {

    List<Notification> findByNotificationNotificationMemberId(Long memberId);

    List<Long> findByWriteMemberId(Long memberId);

    List<Notification> findByBoardIdList(List<Long> boardIdList, Long memberId);

}
