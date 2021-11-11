package com.example.nationalpetition.domain.alarm.repository;

import com.example.nationalpetition.domain.alarm.entity.Alarm;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlarmRepository extends JpaRepository<Alarm, Long> {
}
