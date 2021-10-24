package com.example.nationalpetition.dto.member.response;

import com.example.nationalpetition.domain.member.entity.Member;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberResponse {

    private Long memberId;
    private String name;
    private String email;
    private String picture;
    private String nickName;

    public MemberResponse(Long memberId, String name, String email, String picture, String nickName) {
        this.memberId  = memberId;
        this.name = name;
        this.email = email;
        this.picture = picture;
        this.nickName = nickName;
    }

    public static MemberResponse of(Member member) {
        return new MemberResponse(member.getId(), member.getName(), member.getEmail(), member.getPicture(), member.getNickName());
    }
}
