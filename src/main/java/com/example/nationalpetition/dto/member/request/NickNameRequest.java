package com.example.nationalpetition.dto.member.request;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Size;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class NickNameRequest {

    @Size(min = 2, max = 20)
    private String nickName;
}
