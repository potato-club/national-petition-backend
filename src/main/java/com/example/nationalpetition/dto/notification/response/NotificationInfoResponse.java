package com.example.nationalpetition.dto.notification.response;

import com.example.nationalpetition.domain.notification.Notification;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class NotificationInfoResponse {

    private Long id;
    private String content;

    public NotificationInfoResponse(Long id, String content) {
        this.id = id;
        this.content = content;
    }

    public static NotificationInfoResponse of(Notification notification) {
        return new NotificationInfoResponse(notification.getId(), notification.getContent());
    }

}
