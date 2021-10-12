package com.example.nationalpetition.domain.comment;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CommentRepositoryCustom {

    Comment findByIdAndMemberIdAndIsDeletedIsFalse(Long commentId, Long MemberId);

    List<Comment> findByBoardIdAndIsDeletedIsFalse(Long boardId);

    Long findCommentCountByBoardIdAndIsDeletedIsFalse(Long boardId);

    Page<Comment> findAllRootCommentByBoardId(Pageable pageable, Long boardId);

}
