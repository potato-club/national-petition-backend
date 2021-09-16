package com.example.nationalpetition.service.board;

import com.example.nationalpetition.domain.board.Board;
import com.example.nationalpetition.domain.board.repository.BoardRepository;
import com.example.nationalpetition.dto.board.request.CreateBoardRequest;
import com.example.nationalpetition.dto.board.response.BoardInfoResponse;
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
//        PetitionResponse petitionInfo = petitionClient.getPetitionInfo(request.getPetitionId());
        final Board board = boardRepository.save(request.toEntity(PetitionResponse.of("title", "content", "1000", "status")));
        return BoardInfoResponse.of(board);
    }

}
