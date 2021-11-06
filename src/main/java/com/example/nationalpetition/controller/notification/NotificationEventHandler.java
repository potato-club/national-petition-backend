package com.example.nationalpetition.controller.notification;

import com.example.nationalpetition.dto.notification.NotificationEvent;
import com.example.nationalpetition.service.notification.NotificationService;
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
