package com.example.nationalpetition.domain.board;

import com.example.nationalpetition.domain.BaseTimeEntity;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
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

    // TODO: 2021-09-13 청원 등록일, 청원 만료일
    @ColumnDefault("0")
    @Column(nullable = false)
    private int viewCounts;

    @ColumnDefault("0")
    @Column(nullable = false)
    private int rootCommentsCount;

    private Boolean isDeleted;

    @Builder
    public Board(Long memberId, String petitionTitle, String title, String petitionContent, String content, String petitionUrl, String petitionsCount, String category) {
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
        this.rootCommentsCount = 0;
    }

    public void updateBoard(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public void incrementViewCount() {
        this.viewCounts ++;
    }

    public void countRootComments() {
        this.rootCommentsCount ++;
    }

}
