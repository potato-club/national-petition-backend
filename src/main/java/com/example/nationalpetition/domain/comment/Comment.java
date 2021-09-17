package com.example.nationalpetition.domain.comment;

import com.example.nationalpetition.domain.BaseTimeEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
public class Comment extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private Long memberId;

    @Column
    private Long boardId;

    @Column
    private Long parentId;

    @Column
    private String content;

    @Column
    @ColumnDefault("0")
    private int depth;

    private boolean isDeleted;

    @Builder
    public Comment(Long memberId, Long boardId, Long parentId, int depth, String content) {
        this.memberId = memberId;
        this.boardId = boardId;
        this.parentId = parentId;
        this.content = content;
        this.depth = depth;
        this.isDeleted = false;
    }

}
