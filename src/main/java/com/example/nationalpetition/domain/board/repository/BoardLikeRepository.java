package com.example.nationalpetition.domain.board.repository;

import com.example.nationalpetition.domain.board.BoardLike;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardLikeRepository extends JpaRepository<BoardLike, Long>, BoardLikeRepositoryCustom {
}
