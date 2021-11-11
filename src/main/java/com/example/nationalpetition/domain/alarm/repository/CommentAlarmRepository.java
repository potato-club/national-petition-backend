package com.example.nationalpetition.domain.alarm.repository;

import com.example.nationalpetition.domain.alarm.entity.CommentAlarm;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentAlarmRepository extends JpaRepository<CommentAlarm, Long> {
}
