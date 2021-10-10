package com.example.nationalpetition.domain.comment;

import com.example.nationalpetition.domain.BaseTimeEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
    private int depth;

    private boolean isDeleted;

    @Builder
    private Comment(Long memberId, Long boardId, Long parentId, String content, int depth, boolean isDeleted) {
        this.memberId = memberId;
        this.boardId = boardId;
        this.parentId = parentId;
        this.content = content;
        this.depth = depth;
        this.isDeleted = isDeleted;
    }

    public static Comment newRootComment(Long memberId, Long boardId, String content) {
        return Comment.builder()
                .parentId(null)
                .memberId(memberId)
                .boardId(boardId)
                .content(content)
                .depth(1)
                .isDeleted(false)
                .build();
    }

    public static Comment newChildComment(Long parentId, Long memberId, Long boardId, int depth, String content) {
        return Comment.builder()
                .parentId(parentId)
                .memberId(memberId)
                .boardId(boardId)
                .content(content)
                .depth(depth)
                .isDeleted(false)
                .build();
    }

    public void update(String content) {
        this.content = content;
    }

    public void delete() {
        this.isDeleted = true;

    }

}
