package com.example.nationalpetition.dto.board.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UpdateBoardRequest {

    private Long boardId;

    private String title;

    private String content;

    public UpdateBoardRequest(Long boardId, String title, String content) {
        this.boardId = boardId;
        this.title = title;
        this.content = content;
    }

    public static UpdateBoardRequest testInstance(Long boardId, String title, String content) {
        return new UpdateBoardRequest(boardId, title, content);
    }

}
