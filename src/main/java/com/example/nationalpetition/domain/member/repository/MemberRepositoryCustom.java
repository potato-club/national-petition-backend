package com.example.nationalpetition.domain.member.repository;

import com.example.nationalpetition.domain.member.entity.Member;

import java.util.Optional;

public interface MemberRepositoryCustom {

    boolean duplicateNickName(String nickName);

}
