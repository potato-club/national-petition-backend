package com.example.nationalpetition.service.alarm;

import com.example.nationalpetition.domain.alarm.entity.CommentAlarm;
import com.example.nationalpetition.domain.alarm.entity.ReplyCommentAlarm;
import com.example.nationalpetition.domain.board.Board;
import com.example.nationalpetition.dto.alarm.AlarmListResponse;

public interface AlarmService {

    CommentAlarm createCommentAlarm(Board board, String nickName, Long commentId);

    ReplyCommentAlarm createReplyCommentAlarm(Board board, String nickName, Long parentId, Long commentId);

//    AlarmListResponse getAlarmList(Long memberId);
}
