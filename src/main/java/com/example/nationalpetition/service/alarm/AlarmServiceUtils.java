package com.example.nationalpetition.service.alarm;

public class AlarmServiceUtils {

    public static String createCommentAlarmMessage(String memberNickName, String boardTitle) {
        return String.format("(%s)님이 댓글을 작성하였습니다. : (%s)", memberNickName, boardTitle);
    }

    public static String createReplyMessage(String memberNickName, String boardTitle) {
        return String.format("(%s)님이 대댓글을 작성하였습니다. : (%s)", memberNickName, boardTitle);

    }
}
