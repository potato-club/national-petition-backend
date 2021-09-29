package com.example.nationalpetition.domain.member.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;


@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class DeleteMember {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 20, nullable = false)
    private String name;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String picture;

    @Column(length = 20, nullable = false)
    private String nickName;

    @Builder
    public DeleteMember(String name, String email, String picture, String nickName) {
        this.name = name;
        this.email = email;
        this.picture = picture;
        this.nickName = nickName;
    }

    public static DeleteMember of(Member member) {
        return new DeleteMember().builder()
                .name(member.getName())
                .email(member.getEmail())
                .picture(member.getPicture())
                .nickName(member.getNickName())
                .build();
    }
}
