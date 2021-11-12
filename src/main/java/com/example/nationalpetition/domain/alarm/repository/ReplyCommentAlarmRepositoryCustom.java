package com.example.nationalpetition.domain.alarm.repository;

import com.example.nationalpetition.domain.alarm.entity.ReplyCommentAlarm;

import java.util.List;
import java.util.Optional;

public interface ReplyCommentAlarmRepositoryCustom {

   List<ReplyCommentAlarm> findByMemberIdAndIsReadIsFalseAndOrderByIdDesc(Long memberId);

}
