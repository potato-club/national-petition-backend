package com.example.nationalpetition.service.board;

import com.example.nationalpetition.domain.board.Board;
import com.example.nationalpetition.domain.board.BoardLike;
import com.example.nationalpetition.domain.board.repository.BoardLikeRepository;
import com.example.nationalpetition.domain.board.repository.BoardRepository;
import com.example.nationalpetition.dto.board.request.BoardLikeRequest;
import com.example.nationalpetition.dto.board.request.CreateBoardRequest;
import com.example.nationalpetition.dto.board.request.UpdateBoardRequest;
import com.example.nationalpetition.dto.board.response.BoardInfoResponseWithLikeCount;
import com.example.nationalpetition.dto.board.response.BoardLikeAndUnLikeCounts;
import com.example.nationalpetition.external.petition.PetitionClient;
import com.example.nationalpetition.external.petition.dto.response.PetitionResponse;
import com.example.nationalpetition.utils.error.ErrorCode;
import com.example.nationalpetition.utils.error.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final BoardLikeRepository boardLikeRepository;
    private final PetitionClient petitionClient;

    @Transactional
    public BoardInfoResponseWithLikeCount createBoard(CreateBoardRequest request, Long memberId) {
        PetitionResponse petitionInfo = petitionClient.getPetitionInfo(request.getPetitionId());
        final Board board = boardRepository.save(request.toEntity(petitionInfo, memberId));
        return BoardInfoResponseWithLikeCount.of(board, BoardLikeAndUnLikeCounts.of(0, 0));
    }

    @Transactional
    public BoardInfoResponseWithLikeCount updateBoard(UpdateBoardRequest request, Long memberId) {
        Board board = boardRepository.findByIdAndMemberId(request.getBoardId(), memberId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND_EXCEPTION_BOARD));
        board.updateBoard(request.getTitle(), request.getContent());
        BoardLikeAndUnLikeCounts boardLikeAndUnLikeCounts = boardLikeRepository.countLikeByBoardId(board.getId())
                .orElse(BoardLikeAndUnLikeCounts.of(0, 0));
        return BoardInfoResponseWithLikeCount.of(board, boardLikeAndUnLikeCounts);
    }

    @Transactional(readOnly = true)
    public BoardInfoResponseWithLikeCount getBoard(Long boardId) {
        final Board board = boardRepository.findByIdAndIsDeletedFalse(boardId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND_EXCEPTION_BOARD));
        BoardLikeAndUnLikeCounts boardLikeAndUnLikeCounts = boardLikeRepository.countLikeByBoardId(board.getId())
                .orElse(BoardLikeAndUnLikeCounts.of(0, 0));
        return BoardInfoResponseWithLikeCount.of(board, boardLikeAndUnLikeCounts);
    }

    @Transactional(readOnly = true)
    public List<BoardInfoResponseWithLikeCount> retrieveBoard(String search, Pageable pageable) {
        return boardRepository.findByTitleContaining(search, pageable)
                .stream().map(board -> BoardInfoResponseWithLikeCount.of(board, boardLikeRepository.countLikeByBoardId(board.getId())
                        .orElse(BoardLikeAndUnLikeCounts.of(0, 0))))
                .collect(Collectors.toList());
    }

    @Transactional
    public void boardLikeOrUnLike(BoardLikeRequest request, Long memberId) {
        boardRepository.findByIdAndIsDeletedFalse(request.getBoardId())
                .orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND_EXCEPTION_BOARD));
        BoardLike boardLike = boardLikeRepository.findByBoardIdAndMemberId(request.getBoardId(), memberId);
        if (boardLike == null) {
            boardLikeRepository.save(request.toEntity(memberId));
        } else {
            boardLike.updateState(request.getBoardState());
        }
    }

    @Transactional
    public void deleteBoardLikeOrUnLike(Long boardId, Long memberId) {
        boardRepository.findByIdAndIsDeletedFalse(boardId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND_EXCEPTION_BOARD));
        BoardLike boardLike = boardLikeRepository.findByBoardIdAndMemberId(boardId, memberId);
        boardLikeRepository.delete(boardLike);
    }

}
