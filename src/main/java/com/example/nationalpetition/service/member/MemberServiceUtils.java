package com.example.nationalpetition.service.member;

import com.example.nationalpetition.domain.member.entity.Member;
import com.example.nationalpetition.utils.error.ErrorCode;
import com.example.nationalpetition.utils.error.exception.ConflictException;

public class MemberServiceUtils {

	public static Member isAlreadyExistNickName(Member member) {
		if (member.getNickName() != null) {
			throw new ConflictException(String.format("이미 닉네임(%s) 등록을 한 계정(%s)입니다.",
					member.getNickName(), member.getId()), ErrorCode.ALREADY_EXIST_EXCEPTION_ADD_NICKNAME);
		}
		return member;
	}
}
