package com.example.nationalpetition.domain.board.repository;

import com.example.nationalpetition.domain.board.Board;
import com.example.nationalpetition.domain.member.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BoardRepository extends JpaRepository<Board, Long>, BoardRepositoryCustom {

    Optional<Board> findByIdAndIsDeletedFalse(Long boardId);

    Page<Board> findByMemberIdAndIsDeletedIsFalse(Long memberId, Pageable pageable);
}
