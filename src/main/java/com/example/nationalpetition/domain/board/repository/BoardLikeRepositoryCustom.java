package com.example.nationalpetition.domain.board.repository;

import com.example.nationalpetition.domain.board.BoardLike;

import java.util.Optional;

public interface BoardLikeRepositoryCustom {

    BoardLike findByBoardIdAndMemberId(Long boardId, Long memberId);

}
