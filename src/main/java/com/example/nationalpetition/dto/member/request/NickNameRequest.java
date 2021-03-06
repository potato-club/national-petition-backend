package com.example.nationalpetition.dto.member.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Getter
@NoArgsConstructor
public class NickNameRequest {

    @NotEmpty
    @Size(min = 1, max = 6)
    private String nickName;

    public NickNameRequest(String nickName) {
        this.nickName = nickName;
    }
}
