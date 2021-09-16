package com.example.nationalpetition.domain.user.entity;

import com.example.nationalpetition.domain.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseTimeEntity {

    @Id
    @GeneratedValue
    private Long id;

    private String email;

    @Column(length = 20)
    private String name;

    @Column
    private String picture;

    public Member(String email, String name, String picture) {
        this.email = email;
        this.name = name;
        this.picture = picture;
    }

    public static Member of(String email, String nickName, String picture) {
        return new Member(email, nickName, picture);
    }

    public Member update(String name, String picture) {
        this.name = name;
        this.picture = picture;

        return this;
    }
}
