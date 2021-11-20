package com.example.nationalpetition.domain.member.entity;

import com.example.nationalpetition.domain.BaseTimeEntity;
import lombok.*;

import javax.persistence.*;


@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String email;

    @Column(length = 20, nullable = false)
    private String name;

    @Column(nullable = false)
    private String picture;

    @Column(length = 20)
    private String nickName;

    private String refreshToken;

    private boolean isNotification;

    @Builder
    public Member(String name, String email, String picture) {
        this.name = name;
        this.email = email;
        this.picture = picture;
        this.isNotification = true;
    }

    public void addNickName(String nickName) {
        this.nickName = nickName;
    }

    public void updateRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public static Member of(String name, String email, String picture) {
        return new Member(name, email, picture);
    }

    public Member update(String name, String picture) {
        this.name = name;
        this.picture = picture;

        return this;
    }

    public void removeRefreshToken() {
        this.refreshToken = null;
    }

    public void updateMemberNotification(Boolean state) {
        this.isNotification = state;
    }

}
