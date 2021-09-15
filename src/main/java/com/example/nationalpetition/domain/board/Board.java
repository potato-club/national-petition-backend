package com.example.nationalpetition.domain.board;

import com.example.nationalpetition.domain.BaseTimeEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
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

    @Column(columnDefinition = "TEXT NOT NULL")
    private String petitionContent;

    private String content;

    private String petitionUrl;

    private String petitionsCount;

    private String category;

    // TODO: 2021-09-13 댓글 수, 조회, 청원 등록일, 청원 만료일

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
    }

}
