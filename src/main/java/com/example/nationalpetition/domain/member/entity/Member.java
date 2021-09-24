package com.example.nationalpetition.domain.member.entity;

import com.example.nationalpetition.domain.BaseTimeEntity;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@ToString
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseTimeEntity {

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private String email;

    @Column(length = 20, nullable = false)
    private String name;

    @Column(nullable = false)
    private String picture;

    @Column(length = 20)
    private String nickName;

    @Builder
    public Member(String name, String email, String picture) {
        this.name = name;
        this.email = email;
        this.picture = picture;
    }

    public void addNickName(String nickName) {
        this.nickName = nickName;
    }

    public static Member of(String name, String email, String picture) {
        return new Member(name, email, picture);
    }

    public Member update(String name, String picture) {
        this.name = name;
        this.picture = picture;

        return this;
    }

}
