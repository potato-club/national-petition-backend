package com.example.nationalpetition.service.board;

import com.example.nationalpetition.domain.board.Board;
import com.example.nationalpetition.domain.board.repository.BoardRepository;
import com.example.nationalpetition.dto.board.request.CreateBoardRequest;
import com.example.nationalpetition.dto.board.request.UpdateBoardRequest;
import com.example.nationalpetition.dto.board.response.BoardInfoResponse;
import com.example.nationalpetition.error.exception.NotFoundException;
import com.example.nationalpetition.external.petition.PetitionClient;
import com.example.nationalpetition.external.petition.dto.response.PetitionResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
public class BoardServiceTest {

    @Autowired
    private BoardService boardService;

    @Autowired
    private BoardRepository boardRepository;

    @BeforeEach
    void setup() {
        boardService = new BoardService(boardRepository, new MockPetitionApiCaller());
    }

    @AfterEach
    void clean() {
        boardRepository.deleteAll();
    }

    @Test
    void 사용자가_청원_게시글을_생성한다() {
        // given
        CreateBoardRequest request = CreateBoardRequest.testInstance(600413L, "title", "content", 1L);

        // when
        final BoardInfoResponse response = boardService.createBoard(request);

        // then
        final List<Board> boardList = boardRepository.findAll();
        assertThat(boardList).hasSize(1);
        assertThat(boardList.get(0).getContent()).isEqualTo(request.getContent());
        assertThat(boardList.get(0).getPetitionTitle()).isEqualTo(response.getPetitionTitle());
    }

    @DisplayName("내가 올린 청원게시글을 수정한다")
    @Test
    void 게시글_수정() {
        // given
        Board board = new Board(1L, "petitionTitle", "title", "petitionContent", "content", "url", "10000", "사회문제");
        boardRepository.save(board);

        UpdateBoardRequest request = UpdateBoardRequest.testInstance(board.getId(), "title", "content");

        // when
        boardService.updateBoard(request);

        // then
        final List<Board> boardList = boardRepository.findAll();
        assertThat(boardList).hasSize(1);
        assertThat(boardList.get(0).getTitle()).isEqualTo(request.getTitle());
        assertThat(boardList.get(0).getContent()).isEqualTo(request.getContent());
    }

    @DisplayName("게시글 수정을 요청했는데 요청한 게시글 아이디가 없으면 예외처리 발생")
    @Test
    void 게시글이_없으면_예외처리() {
        // given
        final UpdateBoardRequest request = UpdateBoardRequest.testInstance(1L, "updateTitle", "updateContent");

        // when & then
        assertThatThrownBy(
            () -> boardService.updateBoard(request)
        ).isInstanceOf(NotFoundException.class);
    }

    private static class MockPetitionApiCaller implements PetitionClient {
        @Override
        public PetitionResponse getPetitionInfo(Long id) {
            return PetitionResponse.of("감자좀 살려주세요", "감자가 위험해요", "10000", "청원중");
        }
    }

}
