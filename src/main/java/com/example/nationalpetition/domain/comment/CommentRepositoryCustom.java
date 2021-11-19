package com.example.nationalpetition.domain.comment;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CommentRepositoryCustom {

    Comment findByIdAndMemberIdAndIsDeletedIsFalse(Long commentId, Long MemberId);

    Long findCommentCountByBoardIdAndIsDeletedIsFalse(Long boardId);

    Page<Comment> findAllChildCommentByCommentId(Pageable pageable, Long parentId);

    List<Comment> findALlRootCommentsByBoardIdAndSize(Long boardId, int size);

    List<Comment> findAllRootCommentByBoardIdAndSizeAndLastId(Long boardId, int size, long lastId);

}
