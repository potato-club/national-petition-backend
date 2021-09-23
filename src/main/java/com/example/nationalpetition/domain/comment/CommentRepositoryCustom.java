package com.example.nationalpetition.domain.comment;

public interface CommentRepositoryCustom {

    Comment findByIdAndMemberIdAndIsDeletedIsFalse(Long id, Long MemberId);
}
