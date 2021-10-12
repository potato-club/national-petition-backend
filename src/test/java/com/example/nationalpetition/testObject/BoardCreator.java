package com.example.nationalpetition.testObject;

import com.example.nationalpetition.domain.board.Board;

public class BoardCreator {

    public static Board create(Long memberId, String title, String content) {
        return Board.builder()
                .memberId(memberId)
                .category("카테고리")
                .content(content)
                .petitionContent("petitionContent")
                .petitionTitle("petitionTitle")
                .petitionUrl("url")
                .title(title)
                .petitionsCount("10000")
                .category("기타")
                .petitionCreatedAt("2021-06-21")
                .petitionFinishedAt("2021-06-22")
                .build();
    }

}
