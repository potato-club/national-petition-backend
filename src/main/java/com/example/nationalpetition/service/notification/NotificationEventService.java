package com.example.nationalpetition.service.notification;

import com.example.nationalpetition.domain.notification.Notification;
import com.example.nationalpetition.domain.notification.repository.NotificationRepository;
import com.example.nationalpetition.dto.notification.NotificationEvent;
import com.example.nationalpetition.dto.notification.response.NotificationInfoResponse;
import com.example.nationalpetition.service.redis.RedisService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationEventService {

    private final RedisService redisService;
    private final NotificationRepository notificationRepository;

    public void addNotification(NotificationEvent event) {
        Notification notification = notificationRepository.save(event.toEntity());
        redisService.publish(NotificationInfoResponse.of(notification));
    }

}
