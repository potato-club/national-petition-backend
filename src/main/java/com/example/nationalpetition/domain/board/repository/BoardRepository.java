package com.example.nationalpetition.domain.board.repository;

import com.example.nationalpetition.domain.board.Board;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends JpaRepository<Board, Long> {
}
