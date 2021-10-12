package com.example.nationalpetition.dto.board.response;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class BoardListResponse {

    private List<BoardInfoResponseWithLikeCount> boardList;
    private long boardCounts;

    public BoardListResponse(List<BoardInfoResponseWithLikeCount> boardList, long boardCounts) {
        this.boardList = boardList;
        this.boardCounts = boardCounts;
    }

    public static BoardListResponse of(List<BoardInfoResponseWithLikeCount> boardList, long boardCounts) {
        return new BoardListResponse(boardList, boardCounts);
    }

}
