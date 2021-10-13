package com.example.nationalpetition.domain.board;

import com.example.nationalpetition.domain.BaseTimeEntity;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

@ToString
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DynamicUpdate
public class Board extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long memberId;

    @Column(nullable = false)
    private String petitionTitle;

    @Column(length = 100)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT NOT NULL")
    private String petitionContent;

    private String content;

    private String petitionUrl;

    private String petitionsCount;

    private String category;

    private String petitionCreatedAt;

    private String petitionFinishedAt;

    private int viewCounts;

    private long boardCommentCounts;

    private Boolean isDeleted;

    @Builder
    public Board(Long memberId, String petitionTitle, String title, String petitionContent, String content, String petitionUrl, String petitionsCount, String category, String petitionCreatedAt, String petitionFinishedAt) {
        this.memberId = memberId;
        this.petitionTitle = petitionTitle;
        this.title = title;
        this.petitionContent = petitionContent;
        this.content = content;
        this.petitionUrl = petitionUrl;
        this.petitionsCount = petitionsCount;
        this.category = category;
        this.isDeleted = false;
        this.viewCounts = 0;
        this.boardCommentCounts = 0;
        this.petitionCreatedAt = petitionCreatedAt;
        this.petitionFinishedAt = petitionFinishedAt;
    }

    public void updateBoard(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public void incrementViewCount() {
        this.viewCounts += 1;
    }

    public void incrementCommentCounts() {
        this.boardCommentCounts += 1;
    }

    public void decreaseCommentCounts() {
        this.boardCommentCounts -= 1;
    }

}
