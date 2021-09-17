package com.example.nationalpetition.domain.member.entity;

import com.example.nationalpetition.domain.BaseTimeEntity;
import lombok.*;
import org.springframework.security.oauth2.core.user.OAuth2User;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Map;
import java.util.UUID;

@ToString
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

    @Builder
    public Member(String name, String email, String picture) {
        this.name = name;
        this.email = email;
        this.picture = picture;
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
