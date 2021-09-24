package com.example.nationalpetition.dto.board.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
public class DeleteBoardLikeRequest {

    @NotNull
    private Long boardId;

    public DeleteBoardLikeRequest(Long boardId) {
        this.boardId = boardId;
    }

}
