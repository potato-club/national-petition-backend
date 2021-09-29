package com.example.nationalpetition.dto.board.response;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@Getter
@NoArgsConstructor
public class BoardLikeAndUnLikeCounts {

    private long boardLikeCounts;

    private long boardUnLikeCounts;

    @QueryProjection
    public BoardLikeAndUnLikeCounts(long boardLikeCounts, long boardUnLikeCounts) {
        this.boardLikeCounts = boardLikeCounts;
        this.boardUnLikeCounts = boardUnLikeCounts;
    }

    public static BoardLikeAndUnLikeCounts of(long boardLikeCounts, long boardUnLikeCounts) {
        return new BoardLikeAndUnLikeCounts(boardLikeCounts, boardUnLikeCounts);
    }




}
