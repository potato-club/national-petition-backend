package com.example.nationalpetition.dto.board.response;

import com.example.nationalpetition.domain.board.Board;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
    private long boardLikeCounts;
    private long boardUnLikeCounts;

    @Builder
    public BoardInfoResponseWithLikeCount(Long boardId, Long memberId, String petitionTitle, String title, String petitionContent, String content, String petitionUrl, String petitionsCount, String category, long boardLikeCounts, long boardUnLikeCounts) {
        this.boardId = boardId;
        this.memberId = memberId;
        this.petitionTitle = petitionTitle;
        this.title = title;
        this.petitionContent = petitionContent;
        this.content = content;
        this.petitionUrl = petitionUrl;
        this.petitionsCount = petitionsCount;
        this.category = category;
        this.boardLikeCounts = boardLikeCounts;
        this.boardUnLikeCounts = boardUnLikeCounts;
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
                .boardLikeCounts(boardLikeAndUnLikeCounts.getBoardLikeCounts())
                .boardUnLikeCounts(boardLikeAndUnLikeCounts.getBoardUnLikeCounts())
                .build();
    }

}
