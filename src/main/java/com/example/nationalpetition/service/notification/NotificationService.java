package com.example.nationalpetition.service.notification;

import com.example.nationalpetition.domain.notification.repository.NotificationRepository;
import com.example.nationalpetition.dto.notification.NotificationEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;

    @Transactional
    public void addNotification(NotificationEvent event) {
        notificationRepository.save(event.toEntity());
    }

}
