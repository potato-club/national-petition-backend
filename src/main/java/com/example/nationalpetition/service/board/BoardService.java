package com.example.nationalpetition.service.board;

import com.example.nationalpetition.domain.board.Board;
import com.example.nationalpetition.domain.board.repository.BoardRepository;
import com.example.nationalpetition.dto.board.request.CreateBoardRequest;
import com.example.nationalpetition.dto.board.request.UpdateBoardRequest;
import com.example.nationalpetition.dto.board.response.BoardInfoResponse;
import com.example.nationalpetition.error.exception.NotFoundException;
import com.example.nationalpetition.external.petition.PetitionClient;
import com.example.nationalpetition.external.petition.dto.response.PetitionResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final PetitionClient petitionClient;

    @Transactional
    public BoardInfoResponse createBoard(CreateBoardRequest request) {
        PetitionResponse petitionInfo = petitionClient.getPetitionInfo(request.getPetitionId());
        final Board board = boardRepository.save(request.toEntity(petitionInfo));
        return BoardInfoResponse.of(board);
    }

    @Transactional
    public BoardInfoResponse updateBoard(UpdateBoardRequest request) {
        Board board = boardRepository.findByIdAndIsDeletedFalse(request.getBoardId())
                .orElseThrow(() -> new NotFoundException(String.format("%s는 존재하지 않는 게시물입니다.", request.getBoardId())));
        board.updateBoard(request.getTitle(), request.getContent());
        return BoardInfoResponse.of(board);
    }

}
