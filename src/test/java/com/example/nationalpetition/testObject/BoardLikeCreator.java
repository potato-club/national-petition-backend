package com.example.nationalpetition.testObject;

import com.example.nationalpetition.domain.board.BoardLike;
import com.example.nationalpetition.domain.board.BoardState;

public class BoardLikeCreator {

    public static BoardLike create(Long boardId, Long memberId, BoardState boardState) {
        return new BoardLike(boardId, memberId, boardState);
    }

}
