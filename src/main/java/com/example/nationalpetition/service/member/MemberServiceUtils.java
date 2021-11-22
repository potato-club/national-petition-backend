package com.example.nationalpetition.service.member;

import com.example.nationalpetition.domain.member.entity.Member;
import com.example.nationalpetition.domain.member.repository.MemberRepository;
import com.example.nationalpetition.utils.error.ErrorCode;
import com.example.nationalpetition.utils.error.exception.ConflictException;
import com.example.nationalpetition.utils.error.exception.NotFoundException;

public class MemberServiceUtils {

	public static Member isAlreadyExistNickName(Member member) {
		if (member.getNickName() != null) {
			throw new ConflictException(String.format("이미 닉네임(%s) 등록을 한 계정(%s)입니다.",
					member.getNickName(), member.getId()), ErrorCode.ALREADY_EXIST_EXCEPTION_ADD_NICKNAME);
		}
		return member;
	}

	public static Member findMemberById(MemberRepository memberRepository, Long memberId) {
		return memberRepository.findById(memberId).orElseThrow(() -> new NotFoundException("멤버를 찾을 수 없어요", ErrorCode.NOT_FOUND_EXCEPTION_USER));
	}
}
