package com.example.nationalpetition.service.notification;

import com.example.nationalpetition.domain.member.entity.Member;
import com.example.nationalpetition.domain.member.repository.MemberRepository;
import com.example.nationalpetition.domain.notification.Notification;
import com.example.nationalpetition.domain.notification.repository.NotificationRepository;
import com.example.nationalpetition.dto.notification.NotificationEvent;
import com.example.nationalpetition.dto.notification.response.NotificationInfoResponse;
import com.example.nationalpetition.utils.error.ErrorCode;
import com.example.nationalpetition.utils.error.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final MemberRepository memberRepository;
    private final NotificationRepository notificationRepository;

    @Transactional
    public void addNotification(NotificationEvent event) {
        notificationRepository.save(event.toEntity());
    }

    @Transactional(readOnly = true)
    public List<NotificationInfoResponse> retrieveNotification(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new NotFoundException("멤버를 찾을 수 없어요", ErrorCode.NOT_FOUND_EXCEPTION_COMMENT));
        return notificationRepository.findByNotificationMemberId(memberId, member.getNickName()).stream()
                .map(NotificationInfoResponse::of).collect(Collectors.toList());
    }

    @Transactional
    public void notificationIsRead(Long notificationId, Long memberId) {
        Notification notification = notificationRepository.findNotificationById(notificationId, memberId)
                .orElseThrow(() -> new NotFoundException(String.format("%s는 존재하지 않는 %s의 알림입니다.", notificationId, memberId), ErrorCode.NOT_FOUND_EXCEPTION_NOTIFICATION));
        notification.updateIsRead();
    }

    @Transactional
    public void updateMemberNotification(Boolean state, Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new NotFoundException("멤버를 찾을 수 없어요", ErrorCode.NOT_FOUND_EXCEPTION_COMMENT));
        member.updateMemberNotification(state);
    }

//    @Transactional
//    public void updateBoardNotification(UpdateBoardNotificationRequest request, Long memberId) {
//        Board board = boardRepository.findByIdAndMemberId(request.getBoardId(), memberId)
//                .orElseThrow(() -> new NotFoundException(String.format("해당하는 멤버 (%s)에게 해당하는 게시글 (%s)은 존재하지 않습니다",
//                        memberId, request.getBoardId()), ErrorCode.NOT_FOUND_EXCEPTION_BOARD));
//        board.updateBoardNotification(request.getState());
//    }
//
//    @Transactional
//    public void updateCommentNotification(UpdateCommentNotificationRequest request, Long memberId) {
//        Comment comment = commentRepository.findByIdAndMemberIdAndIsDeletedIsFalse(request.getCommentId(), memberId);
//        if (comment == null) {
//            throw new NotFoundException(String.format("멤버(%s)에게 해당하는 댓글(%s)은 존재하지 않습니다", memberId, request.getCommentId()), ErrorCode.NOT_FOUND_EXCEPTION_COMMENT);
//        }
//        comment.updateCommentNotification(request.getState());
//    }

}
