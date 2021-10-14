package com.example.nationalpetition.dto.member.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.validation.constraints.Min;

@Getter
@Setter
@NoArgsConstructor
public class MemberPageRequest {

    @Min(1)
    private int page;
    @Min(1)
    private int size;

    public MemberPageRequest(@Min(1) int page, @Min(1) int size) {
        this.page = page;
        this.size = size;
    }
}
