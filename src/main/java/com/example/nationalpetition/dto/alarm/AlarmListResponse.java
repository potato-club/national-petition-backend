package com.example.nationalpetition.dto.alarm;

import com.example.nationalpetition.domain.alarm.entity.CommentAlarm;
import com.example.nationalpetition.domain.alarm.entity.ReplyCommentAlarm;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class AlarmListResponse {

    List<CommentAlarm> commentAlarmList;
    List<ReplyCommentAlarm> replyCommentAlarmList;

    public AlarmListResponse(List<CommentAlarm> commentAlarmList, List<ReplyCommentAlarm> replyCommentAlarmList) {
        this.commentAlarmList = commentAlarmList;
        this.replyCommentAlarmList = replyCommentAlarmList;
    }

    public static AlarmListResponse of(List<CommentAlarm> commentAlarmList, List<ReplyCommentAlarm> replyCommentAlarmList) {
        return new AlarmListResponse(commentAlarmList, replyCommentAlarmList);
    }
}
