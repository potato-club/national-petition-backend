package com.example.nationalpetition.service.member;

import com.example.nationalpetition.domain.member.entity.DeleteMember;
import com.example.nationalpetition.domain.member.entity.Member;
import com.example.nationalpetition.domain.member.repository.DeleteMemberRepository;
import com.example.nationalpetition.domain.member.repository.MemberRepository;
import com.example.nationalpetition.dto.member.request.NickNameRequest;
import com.example.nationalpetition.dto.member.response.DeleteMemberResponse;
import com.example.nationalpetition.dto.member.response.MemberResponse;
import com.example.nationalpetition.utils.error.exception.NotFoundException;
import com.example.nationalpetition.utils.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final DeleteMemberRepository deleteMemberRepository;

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
    public MemberResponse addNickName(Long memberId, NickNameRequest request) {
        MemberServiceUtils.duplicateNickName(memberRepository, request.getNickName());
        final Member member = MemberServiceUtils.isAlreadyExistNickName(memberRepository.findById(memberId).orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND_EXCEPTION_USER)));
        member.addNickName(request.getNickName());
        return MemberResponse.of(memberRepository.save(member));
    }

    @Transactional
    @Override
    public DeleteMemberResponse deleteMember(Long memberId) {
        final Member member = memberRepository.findById(memberId).orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND_EXCEPTION_USER));
        final DeleteMember deleteMember = DeleteMember.of(member);
        deleteMemberRepository.save(deleteMember);
        memberRepository.delete(member);
        return DeleteMemberResponse.of(deleteMember);
    }
}
