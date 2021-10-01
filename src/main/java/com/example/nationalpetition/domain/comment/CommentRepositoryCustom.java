package com.example.nationalpetition.domain.comment;

import java.util.List;

public interface CommentRepositoryCustom {

    Comment findByIdAndMemberIdAndIsDeletedIsFalse(Long id, Long MemberId);
    List<Comment> findByBoardIdAndIsDeletedIsFalse(Long boardId);
    Long findCommentCountByBoardIdAndIsDeletedIsFalse(Long boardId);
}
