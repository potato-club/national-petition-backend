package com.example.nationalpetition.service.alarm;

import com.example.nationalpetition.domain.board.Board;

public interface AlarmService {

    void createCommentAlarm(Board board, String nickName, Long commentId);

    void createReplyCommentAlarm(Board board, String nickName, Long parentId, Long commentId);
}
