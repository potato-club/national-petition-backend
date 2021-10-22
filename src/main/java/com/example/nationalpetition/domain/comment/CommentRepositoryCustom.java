package com.example.nationalpetition.domain.comment;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CommentRepositoryCustom {

    Comment findByIdAndMemberIdAndIsDeletedIsFalse(Long commentId, Long MemberId);

    Long findCommentCountByBoardIdAndIsDeletedIsFalse(Long boardId);

    Page<Comment> findAllRootCommentByBoardId(Pageable pageable, Long boardId);

    Page<Comment> findAllChildCommentByCommentId(Pageable pageable, Long parentId);

}
