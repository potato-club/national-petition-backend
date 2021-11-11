package com.example.nationalpetition.testObject;

import com.example.nationalpetition.domain.notification.Notification;

public class NotificationCreator {

    public static Notification create(String content, Long commentMemberId, Long boardMemberId, Long commentId, Long boardId) {
        return Notification.of(content, false, commentMemberId, boardMemberId, commentId, boardId);
    }

}
