package com.example.nationalpetition.domain.alarm.repository;

import com.example.nationalpetition.domain.alarm.entity.CommentAlarm;

import java.util.List;
import java.util.Optional;

public interface CommentAlarmRepositoryCustom {

   List<CommentAlarm>  findByMemberIdAndIsReadIsFalseAndOrderByIdDesc(Long memberId);
}
