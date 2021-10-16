package com.example.nationalpetition.dto.board.response;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class BoardLikeCountsDto {

    private Long boardId;
    private long boardCount;

    public BoardLikeCountsDto(Long boardId, long boardCount) {
        this.boardId = boardId;
        this.boardCount = boardCount;
    }

}
