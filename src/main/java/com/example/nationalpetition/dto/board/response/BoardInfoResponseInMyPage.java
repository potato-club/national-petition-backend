package com.example.nationalpetition.dto.board.response;

import com.example.nationalpetition.domain.board.Board;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


@Getter
@NoArgsConstructor
public class BoardInfoResponseInMyPage {

    private Long boardId;
    private Long memberId;
    private String petitionTitle;
    private String title;
    private String content;
    private String category;
    private long boardLikeCounts;
    private long boardUnLikeCounts;
    private int viewCounts;
    private long boardCommentCounts;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdDate;


    @Builder
    public BoardInfoResponseInMyPage(Long boardId, Long memberId, String petitionTitle, String title, String content, String category, long boardLikeCounts,
                                     long boardUnLikeCounts, int viewCounts, long boardCommentCounts, LocalDateTime createdDate) {
        this.boardId = boardId;
        this.memberId = memberId;
        this.petitionTitle = petitionTitle;
        this.title = title;
        this.content = content;
        this.category = category;
        this.boardLikeCounts = boardLikeCounts;
        this.boardUnLikeCounts = boardUnLikeCounts;
        this.viewCounts = viewCounts;
        this.boardCommentCounts = boardCommentCounts;
        this.createdDate = createdDate;
    }

    public static BoardInfoResponseInMyPage of(Board board, BoardLikeAndUnLikeCounts boardLikeAndUnLikeCounts, long boardCommentCounts) {
        return new BoardInfoResponseInMyPage().builder()
                .boardId(board.getId())
                .memberId(board.getMemberId())
                .petitionTitle(board.getPetitionTitle())
                .title(board.getTitle())
                .content(board.getContent())
                .category(board.getCategory())
                .boardLikeCounts(boardLikeAndUnLikeCounts.getBoardLikeCounts())
                .boardUnLikeCounts(boardLikeAndUnLikeCounts.getBoardUnLikeCounts())
                .viewCounts(board.getViewCounts())
                .boardCommentCounts(boardCommentCounts)
                .createdDate(board.getCreatedDate())
                .build();
    }
}
