package com.example.nationalpetition.dto.board.request;

import com.example.nationalpetition.domain.board.BoardLike;
import com.example.nationalpetition.domain.board.BoardState;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
public class BoardLikeRequest {

    @NotNull
    private Long boardId;

    @NotNull
    private BoardState boardState;

    public BoardLikeRequest(Long boardId, BoardState boardState) {
        this.boardId = boardId;
        this.boardState = boardState;
    }

    public static BoardLikeRequest testInstance(Long boardId, BoardState boardState) {
        return new BoardLikeRequest(boardId, boardState);
    }

    public BoardLike toEntity(Long memberId) {
        return new BoardLike(boardId, memberId, boardState);
    }

}
