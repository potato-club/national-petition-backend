package com.example.nationalpetition.dto.notification.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
public class UpdateCommentNotificationRequest {

    @NotNull
    private Long commentId;

    @NotNull
    private Boolean state;

    public UpdateCommentNotificationRequest(Long commentId, Boolean state) {
        this.commentId = commentId;
        this.state = state;
    }

    public static UpdateCommentNotificationRequest testInstance(Long commentId, Boolean state) {
        return new UpdateCommentNotificationRequest(commentId, state);
    }

}
