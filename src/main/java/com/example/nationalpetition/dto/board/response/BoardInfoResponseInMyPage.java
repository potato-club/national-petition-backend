package com.example.nationalpetition.dto.board.response;

import com.example.nationalpetition.domain.board.Board;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


@Getter
@NoArgsConstructor
public class BoardInfoResponseInMyPage {

    private Long boardId;
    private String petitionTitle;
    private String title;
    private String content;
    private String category;
    private long boardLikeCounts;
    private long boardUnLikeCounts;
    private long commentCount;
    private String createdDate;


    // TODO : 나중에 댓글 총 개수 추가, 작성일 어떻게 나타낼건지 회의해서 수정

    @Builder
    public BoardInfoResponseInMyPage(Long boardId, String petitionTitle, String title, String content, String category, long boardLikeCounts,
                                     long boardUnLikeCounts, LocalDateTime createdDate, long commentCount) {
        this.boardId = boardId;
        this.petitionTitle = petitionTitle;
        this.title = title;
        this.content = content;
        this.category = category;
        this.boardLikeCounts = boardLikeCounts;
        this.boardUnLikeCounts = boardUnLikeCounts;
        this.createdDate = createdDate.format(DateTimeFormatter.ISO_DATE);
        this.commentCount = commentCount;
    }

    public static BoardInfoResponseInMyPage of(Board board, BoardLikeAndUnLikeCounts boardLikeAndUnLikeCounts, long commentCount) {
        return new BoardInfoResponseInMyPage().builder()
                .boardId(board.getId())
                .petitionTitle(board.getPetitionTitle())
                .title(board.getTitle())
                .content(board.getContent())
                .category(board.getCategory())
                .boardLikeCounts(boardLikeAndUnLikeCounts.getBoardLikeCounts())
                .boardUnLikeCounts(boardLikeAndUnLikeCounts.getBoardUnLikeCounts())
                .createdDate(board.getCreatedDate())
                .commentCount(commentCount)
                .build();
    }
}
