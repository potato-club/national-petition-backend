package com.example.nationalpetition.service.member;

import com.example.nationalpetition.domain.board.repository.BoardLikeRepository;
import com.example.nationalpetition.domain.board.repository.BoardRepository;
import com.example.nationalpetition.domain.comment.CommentRepository;
import com.example.nationalpetition.domain.member.entity.DeleteMember;
import com.example.nationalpetition.domain.member.entity.Member;
import com.example.nationalpetition.domain.member.repository.DeleteMemberRepository;
import com.example.nationalpetition.domain.member.repository.MemberRepository;
import com.example.nationalpetition.domain.notification.Notification;
import com.example.nationalpetition.domain.notification.repository.NotificationRepository;
import com.example.nationalpetition.dto.board.response.BoardInfoResponseInMyPage;
import com.example.nationalpetition.dto.board.response.BoardLikeAndUnLikeCounts;
import com.example.nationalpetition.dto.member.request.MemberPageRequest;
import com.example.nationalpetition.dto.member.request.NickNameRequest;
import com.example.nationalpetition.dto.member.response.MemberResponse;
import com.example.nationalpetition.dto.member.response.MyPageBoardListResponse;
import com.example.nationalpetition.dto.notification.response.NotificationInfoResponse;
import com.example.nationalpetition.utils.error.exception.ConflictException;
import com.example.nationalpetition.utils.message.MessageType;
import com.example.nationalpetition.utils.error.exception.NotFoundException;
import com.example.nationalpetition.utils.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.data.domain.Sort.Direction.DESC;

@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final BoardRepository boardRepository;
    private final BoardLikeRepository boardLikeRepository;
    private final DeleteMemberRepository deleteMemberRepository;
    private final CommentRepository commentRepository;
    private final NotificationRepository notificationRepository;

    @Override
    public MemberResponse findById(Long memberId) {
        return MemberResponse.of(memberRepository.findById(memberId).
                orElseThrow(() -> new NotFoundException(String.format("해당하는 멤버 (%s)는 존재하지 않습니다", memberId), ErrorCode.NOT_FOUND_EXCEPTION_USER)));
    }

    @Override
    public Member findByEmail(String email) {
        return memberRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException(String.format("해당하는 이메일을 가진 (%s) 멤버는 존재하지 않습니다", email), ErrorCode.NOT_FOUND_EXCEPTION_USER));
    }

    @Transactional
    @Override
    public String addNickName(Long memberId, NickNameRequest request) {
        if (memberRepository.duplicateNickName(request.getNickName())) {
            return MessageType.NICKNAME_DUPLICATE.getMessage();
        }
        final Member member = MemberServiceUtils.isAlreadyExistNickName(memberRepository.findById(memberId)
                .orElseThrow(() -> new ConflictException(String.format("이미 존재하는 닉네임 (%s) 입니다", request.getNickName()), ErrorCode.CONFLICT_EXCEPTION)));
        member.addNickName(request.getNickName());
        return MessageType.NICKNAME_SUCCESS.getMessage();
    }

    // TODO : 순조가 댓글개수 가져오는 기능 추가하면 commentRepository 에서 조회수 찾아오는거 수정하기
    @Override
    public MyPageBoardListResponse getMyBoardList(Long memberId, MemberPageRequest request) {
        final PageRequest pageable = PageRequest.of(request.getPage() - 1, request.getSize(), Sort.by(DESC, "id"));
        return MyPageBoardListResponse.of(boardRepository.findByMemberIdAndIsDeletedIsFalse(memberId, pageable)
                .stream()
                .map(b -> BoardInfoResponseInMyPage.of(b, boardLikeRepository.countLikeByBoardId(b.getId()).orElse(BoardLikeAndUnLikeCounts.of(0, 0)),
                        commentRepository.findCommentCountByBoardIdAndIsDeletedIsFalse(b.getId())))
                .collect(Collectors.toList()), boardRepository.findBoardCountsWithMemberId(memberId));

    }

    @Transactional
    @Override
    public String deleteMember(Long memberId) {
        final Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new NotFoundException(String.format("해당하는 멤버 (%s)는 존재하지 않습니다", memberId), ErrorCode.NOT_FOUND_EXCEPTION_USER));
        final DeleteMember deleteMember = DeleteMember.of(member);
        deleteMemberRepository.save(deleteMember);
        memberRepository.delete(member);
        return MessageType.DELETE_MEMBER.getMessage();
    }

    @Override
    public List<NotificationInfoResponse> retrieveNotification(Long memberId) {
        return notificationRepository.findByNotificationMemberId(memberId).stream()
                .map(NotificationInfoResponse::of).collect(Collectors.toList());
    }

}
