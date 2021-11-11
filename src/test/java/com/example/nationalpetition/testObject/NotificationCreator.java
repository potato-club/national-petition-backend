package com.example.nationalpetition.testObject;

import com.example.nationalpetition.domain.notification.Notification;

public class NotificationCreator {

    public static Notification create(String content, Long writeMemberId, Long notificationMemberId, Long boardId) {
        return Notification.of(content, false, writeMemberId, notificationMemberId, boardId);
    }

}
