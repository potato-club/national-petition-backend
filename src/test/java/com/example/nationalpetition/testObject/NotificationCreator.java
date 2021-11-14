package com.example.nationalpetition.testObject;

import com.example.nationalpetition.domain.notification.Notification;
import com.example.nationalpetition.domain.notification.NotificationTemplate;

public class NotificationCreator {

    public static Notification create(Long notificationMemberId ,Long boardId, NotificationTemplate notificationTemplate, String commentMemberNickname) {
        return Notification.of(notificationMemberId, boardId, notificationTemplate, commentMemberNickname);
    }

}
