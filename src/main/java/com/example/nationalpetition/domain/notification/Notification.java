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

    //
    private Long writeMemberId;

    private Long notificationMemberId;

    // 게시글
    private Long boardId;

    public Notification(String content, boolean isRead, Long writeMemberId, Long notificationMemberId, Long boardId) {
        this.content = content;
        this.isRead = isRead;
        this.writeMemberId = writeMemberId;
        this.notificationMemberId = notificationMemberId;
        this.boardId = boardId;
    }

    public static Notification of(String content, boolean isRead, Long writeMemberId, Long notificationMemberId, Long boardId) {
        return new Notification(content, isRead, writeMemberId, notificationMemberId, boardId);
    }

}
