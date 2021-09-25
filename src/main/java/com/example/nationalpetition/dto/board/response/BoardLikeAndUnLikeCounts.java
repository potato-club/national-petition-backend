package com.example.nationalpetition.dto.board.response;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@Getter
@NoArgsConstructor
public class BoardLikeAndUnLikeCounts {

    private int boardLikeCounts;

    private int boardUnLikeCounts;

    @QueryProjection
    public BoardLikeAndUnLikeCounts(int boardLikeCounts, int boardUnLikeCounts) {
        this.boardLikeCounts = boardLikeCounts;
        this.boardUnLikeCounts = boardUnLikeCounts;
    }

    public static BoardLikeAndUnLikeCounts of(int boardLikeCounts, int boardUnLikeCounts) {
        return new BoardLikeAndUnLikeCounts(boardLikeCounts, boardUnLikeCounts);
    }




}
