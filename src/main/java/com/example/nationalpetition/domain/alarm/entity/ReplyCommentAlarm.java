package com.example.nationalpetition.domain.alarm.entity;

import com.example.nationalpetition.domain.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReplyCommentAlarm extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private long boardId;
    private long memberId;
    private long parentId;
    private long commentId;
    private String message;
    private Boolean isRead;

    public ReplyCommentAlarm(long boardId, long memberId, long parentId, long commentId, String message) {
        this.boardId = boardId;
        this.memberId = memberId;
        this.parentId = parentId;
        this.commentId = commentId;
        this.message = message;
        this.isRead = false;
    }

    public static ReplyCommentAlarm of(long boardId, long memberId, long parentId, long commentId, String message) {
        return new ReplyCommentAlarm(boardId, memberId, parentId, commentId, message);
    }

    public static ReplyCommentAlarm Return() {
        return new ReplyCommentAlarm();
    }

}
