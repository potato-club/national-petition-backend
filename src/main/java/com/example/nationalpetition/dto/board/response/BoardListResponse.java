package com.example.nationalpetition.dto.board.response;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class BoardListResponse {

    private List<BoardInfoResponseWithLikeCount> boardList;
    private long boardCounts;
    private long boardPages;

    public BoardListResponse(List<BoardInfoResponseWithLikeCount> boardList, long boardCounts, long boardPages) {
        this.boardList = boardList;
        this.boardCounts = boardCounts;
        this.boardPages = boardPages;
    }

    public static BoardListResponse of(List<BoardInfoResponseWithLikeCount> boardList, long boardCounts, long boardPages) {
        return new BoardListResponse(boardList, boardCounts, boardPages);
    }

}
