package com.example.nationalpetition.domain.board.repository;

import com.example.nationalpetition.domain.board.BoardLike;
import com.example.nationalpetition.dto.board.response.BoardLikeAndUnLikeCounts;

import java.util.Optional;

public interface BoardLikeRepositoryCustom {

    BoardLike findByBoardIdAndMemberId(Long boardId, Long memberId);

    Optional<BoardLikeAndUnLikeCounts> countLikeByBoardId(Long boardId);

}
