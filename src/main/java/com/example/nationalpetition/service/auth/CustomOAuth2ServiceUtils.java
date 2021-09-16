package com.example.nationalpetition.service.auth;

import com.example.nationalpetition.domain.auth.OAuthAttributes;
import com.example.nationalpetition.domain.user.entity.Member;
import com.example.nationalpetition.domain.user.repository.MemberRepository;

public class CustomOAuth2ServiceUtils {


    public static Member saveOrUpdate(MemberRepository memberRepository, OAuthAttributes authAttributes) {
        final Member member = memberRepository.findByEmail(authAttributes.getEmail())
                .map(u -> u.update(authAttributes.getName(), authAttributes.getPicture()))
                .orElse(authAttributes.toEntity());
        return memberRepository.save(member);
    }
}
