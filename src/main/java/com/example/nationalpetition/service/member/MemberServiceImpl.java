package com.example.nationalpetition.service.member;

import com.example.nationalpetition.domain.board.repository.BoardLikeRepository;
import com.example.nationalpetition.domain.board.repository.BoardRepository;
import com.example.nationalpetition.domain.comment.CommentRepository;
import com.example.nationalpetition.domain.member.entity.DeleteMember;
import com.example.nationalpetition.domain.member.entity.Member;
import com.example.nationalpetition.domain.member.repository.DeleteMemberRepository;
import com.example.nationalpetition.domain.member.repository.MemberRepository;
import com.example.nationalpetition.dto.board.response.BoardInfoResponseInMyPage;
import com.example.nationalpetition.dto.board.response.BoardLikeAndUnLikeCounts;
import com.example.nationalpetition.dto.member.MessageConst;
import com.example.nationalpetition.dto.member.request.NickNameRequest;
import com.example.nationalpetition.dto.member.response.MemberResponse;
import com.example.nationalpetition.utils.error.exception.NotFoundException;
import com.example.nationalpetition.utils.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

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


    @Override
    public MemberResponse findById(Long memberId) {
        return MemberResponse.of(memberRepository.findById(memberId).orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND_EXCEPTION_USER)));
    }

    @Override
    public Member findByEmail(String email) {
        return memberRepository.findByEmail(email).orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND_EXCEPTION_USER));
    }

    @Transactional
    @Override
    public String addNickName(Long memberId, NickNameRequest request) {
        if (memberRepository.duplicateNickName(request.getNickName())) {
            return MessageConst.DUPLICATE;
        }
        final Member member = MemberServiceUtils.isAlreadyExistNickName(memberRepository.findById(memberId).orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND_EXCEPTION_USER)));
        member.addNickName(request.getNickName());
        return MessageConst.SUCCESS;
    }

    // TODO : 순조가 댓글개수 가져오는 기능 추가하면 commentRepository 에서 조회수 찾아오는거 수정하기
    @Override
    public List<BoardInfoResponseInMyPage> getMyBoardList(Long memberId, Pageable pageable) {
        return boardRepository.findByMemberIdAndIsDeletedIsFalse(memberId, pageable)
                .stream()
                .map(b -> BoardInfoResponseInMyPage.of(b, boardLikeRepository.countLikeByBoardId(b.getId()).orElse(BoardLikeAndUnLikeCounts.of(0, 0)),
                        commentRepository.findCommentCountByBoardIdAndIsDeletedIsFalse(b.getId())))
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public String deleteMember(Long memberId) {
        final Member member = memberRepository.findById(memberId).orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND_EXCEPTION_USER));
        final DeleteMember deleteMember = DeleteMember.of(member);
        deleteMemberRepository.save(deleteMember);
        memberRepository.delete(member);
        return MessageConst.MESSAGE;
    }

}
