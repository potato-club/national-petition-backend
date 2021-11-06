package com.example.nationalpetition.dto.notification;

import com.example.nationalpetition.domain.notification.Notification;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class NotificationEvent {

    private String content;

    private boolean isRead;

    private Long writeMemberId;

    private Long notificationMemberId;

    private Long boardId;

    public NotificationEvent(String content, boolean isRead, Long writeMemberId, Long notificationMemberId, Long boardId) {
        this.content = content;
        this.isRead = isRead;
        this.writeMemberId = writeMemberId;
        this.notificationMemberId = notificationMemberId;
        this.boardId = boardId;
    }

    public static NotificationEvent of(String content, boolean isRead, Long writeMemberId, Long notificationMemberId, Long boardId) {
        return new NotificationEvent(content, isRead, writeMemberId, notificationMemberId, boardId);
    }

    public Notification toEntity() {
        return Notification.of(content, isRead, writeMemberId, notificationMemberId, boardId);
    }

}
