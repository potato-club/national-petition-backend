package com.example.nationalpetition.dto.notification.response;

import com.example.nationalpetition.domain.notification.Notification;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class NotificationInfoResponse {

    private Long id;
    private Long boardId;
    private Long notificationMemberId;
    private String content;
    private Boolean isRead;
    private String createdDate;

    public NotificationInfoResponse(Long id, Long boardId, Long notificationMemberId, String content, Boolean isRead, LocalDateTime createdDate) {
        this.id = id;
        this.boardId = boardId;
        this.notificationMemberId = notificationMemberId;
        this.content = content;
        this.isRead = isRead;
        this.createdDate = createdDate.toString();
    }

    public static NotificationInfoResponse of(Notification notification) {
        String content = String.format(notification.getNotificationTemplate().getTemplate(), notification.getCommentMemberNickname());
        return new NotificationInfoResponse(notification.getId(), notification.getBoardId(), notification.getNotificationMemberId(), content, notification.isRead(), notification.getCreatedDate());
    }

}
