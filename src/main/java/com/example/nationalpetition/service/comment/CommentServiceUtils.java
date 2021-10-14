package com.example.nationalpetition.service.comment;

import com.example.nationalpetition.domain.comment.Comment;
import com.example.nationalpetition.domain.comment.CommentRepository;
import com.example.nationalpetition.utils.error.ErrorCode;
import com.example.nationalpetition.utils.error.exception.NotFoundException;

public class CommentServiceUtils {

    private CommentServiceUtils() {}

    public static Comment findCommentByCommentIdAndMemberId (CommentRepository commentRepository, Long commentId, Long memberId) {

        Comment comment = commentRepository.findByIdAndMemberIdAndIsDeletedIsFalse(commentId, memberId);
        if (comment == null) {
            throw new NotFoundException(String.format("해당하는 멤버 (%s)에게 댓글 (%s)은 존재하지 않습니다", memberId, commentId), ErrorCode.NOT_FOUND_EXCEPTION_COMMENT);
        }
        return comment;
    }

    public static Comment findCommentById(CommentRepository commentRepository, Long commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundException(String.format("해당하는 댓글(%s)은 존재하지 않습니다",commentId), ErrorCode.NOT_FOUND_EXCEPTION_COMMENT));
    }

}
