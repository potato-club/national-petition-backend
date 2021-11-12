package com.example.nationalpetition.domain.alarm.entity;

import com.example.nationalpetition.domain.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CommentAlarm extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private long boardId;
    private long memberId;
    private long commentId;
    private String message;
    private Boolean isRead;

    @Builder
    public CommentAlarm(long boardId, long memberId, long commentId, String message) {
        this.boardId = boardId;
        this.memberId = memberId;
        this.commentId = commentId;
        this.message = message;
        this.isRead = false;
    }

    public static CommentAlarm of(long boardId, long memberId, long commentId, String message) {
        return new CommentAlarm(boardId, memberId, commentId, message);
    }

    public static CommentAlarm Return() {
        return new CommentAlarm();
    }


}
