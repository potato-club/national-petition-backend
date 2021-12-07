package com.example.nationalpetition.dto.notification.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
public class UpdateMemberNotificationRequest {

    @NotNull
    private Boolean state;

    public UpdateMemberNotificationRequest(Boolean state) {
        this.state = state;
    }

}
