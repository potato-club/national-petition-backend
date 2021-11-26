package com.example.nationalpetition.domain.alarm;

import com.example.nationalpetition.domain.alarm.repository.AlarmRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlarmRepository extends JpaRepository<Alarm, Long>, AlarmRepositoryCustom {

}
