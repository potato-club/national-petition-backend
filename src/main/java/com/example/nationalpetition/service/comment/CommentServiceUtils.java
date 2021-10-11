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
            throw new NotFoundException(ErrorCode.NOT_FOUND_EXCEPTION_COMMENT);
        }
        return comment;
    }

}
