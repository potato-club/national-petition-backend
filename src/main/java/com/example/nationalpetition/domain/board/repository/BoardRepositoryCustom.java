package com.example.nationalpetition.domain.board.repository;

import com.example.nationalpetition.domain.board.Board;

import java.util.Optional;

public interface BoardRepositoryCustom {

    Optional<Board> findByIdAndMemberId(Long boardId, Long memberId);

    long findBoardCounts();

}
