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

    // 알림을 받을 멤버
    private Long notificationMemberId;

    private Long boardId;

    private NotificationTemplate notificationTemplate;

    private String commentMemberNickname;

    private boolean isRead;

    public Notification(Long notificationMemberId, Long boardId, NotificationTemplate notificationTemplate, String commentMemberNickname) {
        this.notificationMemberId = notificationMemberId;
        this.boardId = boardId;
        this.notificationTemplate = notificationTemplate;
        this.commentMemberNickname = commentMemberNickname;
        this.isRead = false;
    }

    public static Notification of(Long notificationMemberId, Long boardId, NotificationTemplate notificationTemplate, String commentMemberNickname) {
        return new Notification(notificationMemberId, boardId, notificationTemplate, commentMemberNickname);
    }

    public void updateIsRead() {
        this.isRead = true;
    }

}
