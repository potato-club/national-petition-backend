package com.example.nationalpetition.domain.comment;

import com.example.nationalpetition.domain.BaseTimeEntity;
import com.example.nationalpetition.domain.member.entity.Member;
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

    @ManyToOne
    @JoinColumn(name="member_id")
    private Member member;

    @Column
    private Long boardId;

    @Column
    private Long parentId;

    @Column
    private String content;

    @Column
    private int depth;

    @Column
    private long childCommentsCount;

    private boolean isDeleted;

    @Builder
    private Comment(Long boardId, Long parentId, String content, int depth, boolean isDeleted, Member member) {
        this.boardId = boardId;
        this.parentId = parentId;
        this.content = content;
        this.depth = depth;
        this.isDeleted = isDeleted;
        this.childCommentsCount = 0;
        this.member = member;
    }

    public static Comment newRootComment(Member member, Long boardId, String content) {
        return Comment.builder()
                .parentId(null)
                .member(member)
                .boardId(boardId)
                .content(content)
                .depth(1)
                .isDeleted(false)
                .build();
    }

    public static Comment newChildComment(Long parentId, Member member, Long boardId, int depth, String content) {
        return Comment.builder()
                .parentId(parentId)
                .member(member)
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

    public void countChildComments() {
        this.childCommentsCount ++;
    }

    public void decreaseChildCommentsCounts() {
        this.childCommentsCount --;
    }

    public boolean isRootComment() {
        return this.parentId == null;
    }

}
