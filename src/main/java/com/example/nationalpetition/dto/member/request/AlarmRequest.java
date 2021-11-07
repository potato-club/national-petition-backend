package com.example.nationalpetition.dto.member.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AlarmRequest {

    private Boolean isSubscribe;

    public AlarmRequest(Boolean isSubscribe) {
        this.isSubscribe = isSubscribe;
    }
}
