package com.example.nationalpetition.domain.notification.repository;

import com.example.nationalpetition.domain.notification.Notification;

import java.util.List;

public interface NotificationRepositoryCustom {

    List<Notification> findByBoardMemberId(Long memberId);

    List<Notification> findByCommentMemberId(Long memberId);

    List<Notification> findByBoardIdList(List<Long> boardIdList, Long memberId);

    List<Notification> findByBoardId(Long boardId, Long commentId);

    List<Notification> findByParentId(Long commentMemberId, Long commentId);

}
