package com.example.nationalpetition.dto.notification;

import com.example.nationalpetition.domain.notification.Notification;
import com.example.nationalpetition.domain.notification.NotificationTemplate;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class NotificationEvent {

    private Long notificationMemberId;

    private Long boardId;

    private NotificationTemplate notificationTemplate;

    private String commentMemberNickname;

    public NotificationEvent(Long notificationMemberId, Long boardId, NotificationTemplate notificationTemplate, String  commentMemberNickname) {
        this.notificationMemberId = notificationMemberId;
        this.boardId = boardId;
        this.notificationTemplate = notificationTemplate;
        this.commentMemberNickname = commentMemberNickname;
    }

    public static NotificationEvent of(Long notificationMemberId, Long boardId, NotificationTemplate notificationTemplate, String commentMemberNickname) {
        return new NotificationEvent(notificationMemberId, boardId, notificationTemplate, commentMemberNickname);
    }

    public Notification toEntity() {
        return Notification.of(notificationMemberId, boardId, notificationTemplate, commentMemberNickname);
    }

}
