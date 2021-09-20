package com.example.nationalpetition.domain.comment;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    Comment findByIdAndMemberIdAndIsDeletedIsFalse(Long id, Long MemberId);

}
