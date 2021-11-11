package com.example.nationalpetition.dto.notification;

import com.example.nationalpetition.domain.notification.Notification;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class NotificationEvent {

    private String content;

    private boolean isRead;

    private Long commentMemberId;

    private Long boardMemberId;

    private Long commentId;

    private Long boardId;

    public NotificationEvent(String content, boolean isRead, Long commentMemberId, Long boardMemberId, Long commentId, Long boardId) {
        this.content = content;
        this.isRead = isRead;
        this.commentMemberId = commentMemberId;
        this.boardMemberId = boardMemberId;
        this.commentId = commentId;
        this.boardId = boardId;
    }

    public static NotificationEvent of(String content, boolean isRead, Long commentMemberId, Long boardMemberId, Long commentId, Long boardId) {
        return new NotificationEvent(content, isRead, commentMemberId, boardMemberId, commentId, boardId);
    }

    public Notification toEntity() {
        return Notification.of(content, isRead, commentMemberId, boardMemberId, commentId, boardId);
    }

}
