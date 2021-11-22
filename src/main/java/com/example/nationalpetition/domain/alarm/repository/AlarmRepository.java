package com.example.nationalpetition.domain.alarm.repository;

import com.example.nationalpetition.domain.alarm.entity.Alarm;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AlarmRepository extends JpaRepository<Alarm, Long>, AlarmRepositoryCustom{

    Optional<Alarm> findByIdAndMemberId(Long alarmId, Long memberId);

    Optional<Alarm> findByMemberIdAndBoardId(Long memberId, Long boardId);
}
