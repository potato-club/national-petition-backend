package com.example.nationalpetition.service.member;

import com.example.nationalpetition.domain.member.entity.Member;
import com.example.nationalpetition.domain.member.repository.MemberRepository;
import com.example.nationalpetition.utils.error.ErrorCode;
import com.example.nationalpetition.utils.error.exception.AlreadyExistException;
import com.example.nationalpetition.utils.error.exception.DuplicateException;

public class MemberServiceUtils {

    public static void duplicateNickName(MemberRepository memberRepository, String nickName) {
        if (memberRepository.duplicateNickName(nickName)) {
            throw new DuplicateException(ErrorCode.DUPLICATE_EXCEPTION_NICKNAME);
        }
    }

    public static Member isAlreadyExistNickName(Member member) {
        if (member.getNickName() != null) {
            throw new AlreadyExistException(ErrorCode.ALREADY_EXIST_EXCEPTION_ADD_NICKNAME);
        }
        return member;
    }
}
