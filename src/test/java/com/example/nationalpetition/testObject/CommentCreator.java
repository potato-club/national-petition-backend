package com.example.nationalpetition.testObject;

import com.example.nationalpetition.domain.comment.Comment;
import com.example.nationalpetition.domain.member.entity.Member;

public class CommentCreator {

    public static Comment create(String content, Long boardId, Long parentId, Member member, int depth) {
        return Comment.builder()
                .content(content)
                .boardId(boardId)
                .parentId(parentId)
                .member(member)
                .depth(depth)
                .isDeleted(false)
                .build();
    }

}
