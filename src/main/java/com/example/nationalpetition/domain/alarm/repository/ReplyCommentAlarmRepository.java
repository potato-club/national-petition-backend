package com.example.nationalpetition.domain.alarm.repository;

import com.example.nationalpetition.domain.alarm.entity.ReplyCommentAlarm;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReplyCommentAlarmRepository extends JpaRepository<ReplyCommentAlarm, Long> {
}
