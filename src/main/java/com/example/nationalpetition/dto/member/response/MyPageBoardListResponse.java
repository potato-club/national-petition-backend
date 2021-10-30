package com.example.nationalpetition.dto.member.response;

import com.example.nationalpetition.dto.board.response.BoardInfoResponseInMyPage;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class MyPageBoardListResponse {

    private List<BoardInfoResponseInMyPage> myBoardList;
    private long boardCounts;

    public MyPageBoardListResponse(List<BoardInfoResponseInMyPage> myBoardList, long boardCounts) {
        this.myBoardList = myBoardList;
        this.boardCounts = boardCounts;
    }

    public static MyPageBoardListResponse of(List<BoardInfoResponseInMyPage> myBoardList, long boardCounts) {
        return new MyPageBoardListResponse(myBoardList, boardCounts);
    }
}
