package com.example.nationalpetition.service.notification;

import com.example.nationalpetition.dto.notification.NotificationEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NotificationEventHandler {

    private final NotificationService notificationService;

    @EventListener
    public void addNotification(NotificationEvent event) {
        notificationService.addNotification(event);
    }

}
