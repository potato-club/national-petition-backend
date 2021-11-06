package com.example.nationalpetition.domain.board.repository;

import com.example.nationalpetition.domain.board.Board;
import com.example.nationalpetition.domain.board.BoardCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface BoardRepositoryCustom {

    Optional<Board> findByIdAndMemberId(Long boardId, Long memberId);

    long findBoardCounts(String search, BoardCategory category);

    long findBoardCountsWithMemberId(Long memberId);

    Page<Board> findBySearchingAndCategory(String search, BoardCategory category, Pageable pageable);

}
