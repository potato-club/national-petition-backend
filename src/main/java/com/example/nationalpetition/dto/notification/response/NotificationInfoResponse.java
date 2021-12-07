package com.example.nationalpetition.dto.notification.response;

import com.example.nationalpetition.domain.notification.Notification;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class NotificationInfoResponse {

    private Long id;
    private Long boardId;
    private String content;
    private Boolean isRead;

    public NotificationInfoResponse(Long id, Long boardId, String content, Boolean isRead) {
        this.id = id;
        this.boardId = boardId;
        this.content = content;
        this.isRead = isRead;
    }

    public static NotificationInfoResponse of(Notification notification) {
        String content = String.format(notification.getNotificationTemplate().getTemplate(), notification.getCommentMemberNickname());
        return new NotificationInfoResponse(notification.getId(), notification.getBoardId(), content, notification.isRead());
    }

}
