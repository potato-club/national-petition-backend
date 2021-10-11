package com.example.nationalpetition.service.board;

import com.example.nationalpetition.domain.board.Board;
import com.example.nationalpetition.domain.board.repository.BoardRepository;
import com.example.nationalpetition.utils.error.ErrorCode;
import com.example.nationalpetition.utils.error.exception.NotFoundException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;


@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BoardServiceUtils {

    public static Board findBoardById(BoardRepository boardRepository, Long boardId) {
        return boardRepository.findById(boardId)
                .orElseThrow(() -> new NotFoundException((ErrorCode.NOT_FOUND_EXCEPTION_BOARD)));
    }

}
