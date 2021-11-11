package com.example.nationalpetition.domain.notification;

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
public class Notification extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;

    private boolean isRead;

    // 댓글들의 알람
    private boolean isCommentNotification;

    // 댓글 작성자 id
    private Long commentMemberId;

    // 게시글 작성자 id
    private Long boardMemberId;

    private Long commentId;

    private Long commentParentId;

    private Long boardId;

    public Notification(String content, boolean isRead, Long commentMemberId, Long boardMemberId, Long commentId, Long commentParentId, Long boardId) {
        this.content = content;
        this.isRead = isRead;
        this.isCommentNotification = true;
        this.commentMemberId = commentMemberId;
        this.boardMemberId = boardMemberId;
        this.commentId = commentId;
        this.commentParentId = commentParentId;
        this.boardId = boardId;
    }

    public static Notification of(String content, boolean isRead, Long commentMemberId, Long boardMemberId, Long commentId, Long commentParentId, Long boardId) {
        return new Notification(content, isRead, commentMemberId, boardMemberId, commentId, commentParentId, boardId);
    }

}
