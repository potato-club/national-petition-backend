package com.example.nationalpetition.service.board;

import com.example.nationalpetition.domain.board.Board;
import com.example.nationalpetition.domain.board.repository.BoardRepository;
import com.example.nationalpetition.dto.board.request.CreateBoardRequest;
import com.example.nationalpetition.dto.board.request.UpdateBoardRequest;
import com.example.nationalpetition.dto.board.response.BoardInfoResponse;
import com.example.nationalpetition.utils.error.exception.NotFoundException;
import com.example.nationalpetition.external.petition.PetitionClient;
import com.example.nationalpetition.external.petition.dto.response.PetitionResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.springframework.data.domain.Sort.Direction.DESC;

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

    // 테스트를 위한 코드
    public void insert10() {
        for(int i = 1; i < 11; i++) {
            Board board = Board.builder()
                    .memberId(1L)
                    .content("content")
                    .category("category")
                    .petitionContent("petitionContent")
                    .petitionsCount("10000")
                    .petitionTitle("petitionTitle")
                    .petitionUrl("url")
                    .title("title" + i)
                    .build();
            boardRepository.save(board);
        }
    }

    @Test
    void 사용자가_청원_게시글을_생성한다() {
        // given
        CreateBoardRequest request = CreateBoardRequest.testInstance(600413L, "title", "content");

        // when
        final BoardInfoResponse response = boardService.createBoard(request, 1L);

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
        boardService.updateBoard(request, 1L);

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
            () -> boardService.updateBoard(request, 1L)
        ).isInstanceOf(NotFoundException.class);
    }

    @DisplayName("게시글 아이디로 게시글을 불러온다")
    @Test
    void 게시글_불러오기() {
        // given
        Board board = new Board(1L, "petitionTitle", "title", "petitionContent", "content", "url", "10000", "사회문제");
        boardRepository.save(board);

        // when
        boardService.getBoard(board.getId());

        // then
        final List<Board> boardList = boardRepository.findAll();
        assertThat(boardList).hasSize(1);
        assertThat(boardList.get(0).getTitle()).isEqualTo(board.getTitle());
        assertThat(boardList.get(0).getContent()).isEqualTo(board.getContent());
    }

    @DisplayName("title 기준으로 검색하고 기본 10개씩 첫페이지는 0으로 가져온다.")
    @Test
    void 게시글_리스트_불러오기() {
        // given
        insert10();

        // when
        final Pageable pageable = PageRequest.of(0, 10, DESC, "id");
        final List<BoardInfoResponse> responseList = boardService.retrieveBoard("", pageable);

        // then
        final List<Board> boardList = boardRepository.findAll();
        assertThat(boardList).hasSize(10);
        assertThat(responseList).hasSize(10);
        assertThat(responseList.get(0).getTitle()).isEqualTo("title10");
    }

    @DisplayName("title 기준으로 검색하고 기본 10개씩 첫페이지는 0으로 가져온다. - 타이틀검색어가 있을 시")
    @Test
    void 게시글_리스트_불러오기2() {
        // given
        insert10();

        // when
        final Pageable pageable = PageRequest.of(0, 10, DESC, "id");
        final List<BoardInfoResponse> responseList = boardService.retrieveBoard("1", pageable);

        // then
        final List<Board> boardList = boardRepository.findAll();
        assertThat(boardList).hasSize(10);
        assertThat(responseList).hasSize(2);
        assertThat(responseList.get(0).getTitle()).isEqualTo("title10");
        assertThat(responseList.get(1).getTitle()).isEqualTo("title1");
    }

    @DisplayName("title 기준으로 검색하고 기본 10개씩 첫페이지는 0으로 가져온다. 10개만 있을 시 페이지1은 빈배열 반환")
    @Test
    void 게시글_리스트_불러오기3() {
        // given
        insert10();

        // when
        final Pageable pageable = PageRequest.of(1, 10, DESC, "id");
        final List<BoardInfoResponse> responseList = boardService.retrieveBoard("1", pageable);

        // then
        assertThat(responseList).isEmpty();

    }

    private static class MockPetitionApiCaller implements PetitionClient {
        @Override
        public PetitionResponse getPetitionInfo(Long id) {
            return PetitionResponse.of("감자좀 살려주세요", "감자가 위험해요", "10000", "청원중");
        }
    }

}
