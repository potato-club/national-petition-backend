package com.example.nationalpetition.domain.notification.repository;

import com.example.nationalpetition.domain.notification.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, Long>, NotificationRepositoryCustom {
}
