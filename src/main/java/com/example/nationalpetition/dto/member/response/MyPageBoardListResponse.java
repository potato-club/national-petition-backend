package com.example.nationalpetition.dto.member.response;

import com.example.nationalpetition.dto.board.response.BoardInfoResponseInMyPage;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class MyPageBoardListResponse {

    private List<BoardInfoResponseInMyPage> myBoardList;

    public MyPageBoardListResponse(List<BoardInfoResponseInMyPage> myBoardList) {
        this.myBoardList = myBoardList;
    }

    public static MyPageBoardListResponse of(List<BoardInfoResponseInMyPage> myBoardList) {
        return new MyPageBoardListResponse(myBoardList);
    }
}
