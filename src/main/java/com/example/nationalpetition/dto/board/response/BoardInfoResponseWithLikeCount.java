package com.example.nationalpetition.dto.board.response;

import com.example.nationalpetition.domain.board.Board;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class BoardInfoResponseWithLikeCount {

    private Long boardId;
    private Long memberId;
    private String petitionTitle;
    private String title;
    private String petitionContent;
    private String content;
    private String petitionUrl;
    private String petitionsCount;
    private String category;
    private String petitionCreatedAt;
    private String petitionFinishedAt;
    private int viewCounts;
    private int boardCommentCounts;
    private long boardLikeCounts;
    private long boardUnLikeCounts;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdDate;

    @Builder
    public BoardInfoResponseWithLikeCount(Long boardId, Long memberId, String petitionTitle, String title, String petitionContent,
                                          String content, String petitionUrl, String petitionsCount, String category, String petitionCreatedAt,
                                          String petitionFinishedAt, int viewCounts, int boardCommentCounts, long boardLikeCounts, long boardUnLikeCounts, LocalDateTime createdDate) {
        this.boardId = boardId;
        this.memberId = memberId;
        this.petitionTitle = petitionTitle;
        this.title = title;
        this.petitionContent = petitionContent;
        this.content = content;
        this.petitionUrl = petitionUrl;
        this.petitionsCount = petitionsCount;
        this.category = category;
        this.petitionCreatedAt = petitionCreatedAt;
        this.petitionFinishedAt = petitionFinishedAt;
        this.viewCounts = viewCounts;
        this.boardCommentCounts = boardCommentCounts;
        this.boardLikeCounts = boardLikeCounts;
        this.boardUnLikeCounts = boardUnLikeCounts;
        this.createdDate = createdDate;
    }

    public static BoardInfoResponseWithLikeCount of(Board board, BoardLikeAndUnLikeCounts boardLikeAndUnLikeCounts) {
        return BoardInfoResponseWithLikeCount.builder()
                .boardId(board.getId())
                .memberId(board.getMemberId())
                .petitionTitle(board.getPetitionTitle())
                .title(board.getTitle())
                .petitionContent(board.getPetitionContent())
                .content(board.getContent())
                .petitionUrl(board.getPetitionUrl())
                .petitionsCount(board.getPetitionsCount())
                .category(board.getCategory())
                .petitionCreatedAt(board.getPetitionCreatedAt())
                .petitionFinishedAt(board.getPetitionFinishedAt())
                .viewCounts(board.getViewCounts())
                .boardCommentCounts(board.getRootCommentsCount())
                .boardLikeCounts(boardLikeAndUnLikeCounts.getBoardLikeCounts())
                .boardUnLikeCounts(boardLikeAndUnLikeCounts.getBoardUnLikeCounts())
                .createdDate(board.getCreatedDate())
                .build();
    }

}
