package com.example.nationalpetition.domain.alarm.repository;

import com.example.nationalpetition.domain.alarm.entity.CommentAlarm;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CommentAlarmRepository extends JpaRepository<CommentAlarm, Long> {

    List<CommentAlarm> findByMemberIdAndIsReadIsFalseAndOrderByIdDesc(Long memberId);
}
