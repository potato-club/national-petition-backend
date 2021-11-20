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

    public UpdateBoardNotificationRequest(Long boardId, Boolean state) {
        this.boardId = boardId;
        this.state = state;
    }

    public static UpdateBoardNotificationRequest testInstance(Long boardId, Boolean state) {
        return new UpdateBoardNotificationRequest(boardId, state);
    }

}
