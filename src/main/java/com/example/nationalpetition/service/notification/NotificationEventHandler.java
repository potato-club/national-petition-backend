package com.example.nationalpetition.service.notification;

import com.example.nationalpetition.dto.notification.NotificationEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NotificationEventHandler {

    private final NotificationEventService notificationEventService;

    @EventListener
    public void addNotification(NotificationEvent event) {
        notificationEventService.addNotification(event);
    }

}
