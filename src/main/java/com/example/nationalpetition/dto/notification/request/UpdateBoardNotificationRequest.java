package com.example.nationalpetition.dto.notification.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
public class UpdateBoardNotificationRequest {

    @NotNull
    private Long boardId;

    @NotNull
    private Boolean state;

}
