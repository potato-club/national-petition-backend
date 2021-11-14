package com.example.nationalpetition.service.board;

import com.example.nationalpetition.domain.board.Board;
import com.example.nationalpetition.domain.board.BoardCategory;
import com.example.nationalpetition.domain.board.BoardLike;
import com.example.nationalpetition.domain.board.BoardState;
import com.example.nationalpetition.domain.board.repository.BoardLikeRepository;
import com.example.nationalpetition.domain.board.repository.BoardRepository;
import com.example.nationalpetition.domain.member.entity.Member;
import com.example.nationalpetition.domain.member.repository.MemberRepository;
import com.example.nationalpetition.dto.board.request.BoardLikeRequest;
import com.example.nationalpetition.dto.board.request.CreateBoardRequest;
import com.example.nationalpetition.dto.board.request.UpdateBoardRequest;
import com.example.nationalpetition.dto.board.response.BoardInfoResponseWithLikeCount;
import com.example.nationalpetition.dto.board.response.BoardListResponse;
import com.example.nationalpetition.external.petition.PetitionClientWrapper;
import com.example.nationalpetition.testObject.BoardCreator;
import com.example.nationalpetition.testObject.BoardLikeCreator;
import com.example.nationalpetition.testObject.MemberCreator;
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
import org.springframework.data.domain.Sort;

import javax.transaction.Transactional;
import java.util.Arrays;
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

    @Autowired
    private BoardLikeRepository boardLikeRepository;

    @Autowired
    private MemberRepository memberRepository;

    @BeforeEach
    void setup() {
        boardService = new BoardService(boardRepository, boardLikeRepository, new PetitionClientWrapper(new MockPetitionApiCaller()),  memberRepository);
    }

    @AfterEach
    void clean() {
        boardRepository.deleteAll();
        boardLikeRepository.deleteAll();
        memberRepository.deleteAll();
    }

    // 테스트를 위한 코드
    public void insert10() {
        for (int i = 1; i < 11; i++) {
            Board board = Board.builder()
                    .memberId(1L)
                    .content("content")
                    .category(BoardCategory.ADMINISTRATION)
                    .petitionContent("petitionContent")
                    .petitionsCount("10000")
                    .petitionTitle("petitionTitle")
                    .petitionUrl("url")
                    .title("title" + i)
                    .build();
            boardRepository.save(board);
        }
    }

    public void insertBoardLike4(Long boardId) {
        for (long i = 1; i < 5; i++) {
            BoardLike boardLike = BoardLikeCreator.create(boardId, i, BoardState.LIKE);
            boardLikeRepository.save(boardLike);
        }
    }

    @Test
    void 사용자가_청원_게시글을_생성한다() {
        // given
        CreateBoardRequest request = CreateBoardRequest.testInstance("title", "content", "www1.president.go.kr/petitions/6004");

        // when
        final BoardInfoResponseWithLikeCount response = boardService.createBoard(request, 1L);

        // then
        final List<Board> boardList = boardRepository.findAll();
        assertThat(boardList).hasSize(1);
        assertThat(boardList.get(0).getContent()).isEqualTo(request.getContent());
        assertThat(boardList.get(0).getPetitionTitle()).isEqualTo(response.getPetitionTitle());
    }

    @DisplayName("내가 올린 청원게시글을 수정한다")
    @Test
    @Transactional
    void 게시글_수정() {
        // given
        Board board = BoardCreator.create(1L, "title1", "content1");
        boardRepository.save(board);

        UpdateBoardRequest request = UpdateBoardRequest.testInstance(board.getId(), "updateTitle", "updateContent");

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
    void 특정_게시글_불러오기() {
        // given
        Member member = MemberCreator.create("nickname1");
        memberRepository.save(member);
        Board board = BoardCreator.create(member.getId(), "title1", "content1");
        boardRepository.save(board);

        // when
        BoardInfoResponseWithLikeCount response = boardService.getBoard(board.getId());

        // then
        final List<Board> boardList = boardRepository.findAll();
        assertThat(boardList).hasSize(1);
        assertThat(boardList.get(0).getTitle()).isEqualTo(board.getTitle());
        assertThat(boardList.get(0).getContent()).isEqualTo(board.getContent());
        assertThat(response.getViewCounts()).isEqualTo(1);
    }

    @DisplayName("게시글 아이디로 게시글을 불러올 때 좋아요 개수도 가져온다")
    @Test
    void 특정_게시글_불러오기2() {
        // given
        Member member = MemberCreator.create("nickname1");
        memberRepository.save(member);
        Board board = BoardCreator.create(member.getId(), "title1", "content1");
        boardRepository.save(board);
        BoardLike boardLike = BoardLikeCreator.create(board.getId(), board.getMemberId(), BoardState.LIKE);
        boardLikeRepository.save(boardLike);

        // when
        BoardInfoResponseWithLikeCount response = boardService.getBoard(board.getId());

        // then
        final List<Board> boardList = boardRepository.findAll();
        List<BoardLike> boardLikeList = boardLikeRepository.findAll();
        assertThat(boardList).hasSize(1);
        assertThat(boardList.get(0).getTitle()).isEqualTo(board.getTitle());
        assertThat(boardList.get(0).getContent()).isEqualTo(board.getContent());
        assertThat(boardLikeList).hasSize(1);
        assertThat(response.getBoardLikeCounts()).isEqualTo(1);
        assertThat(response.getBoardUnLikeCounts()).isEqualTo(0);
    }

    @DisplayName("여러 사람이 한 게시글에 찬성을 했을 경우 특정 게시글 불러오기")
    @Test
    void 특정_게시글_불러오기3() {
        // given
        Member member = MemberCreator.create("nickname1");
        memberRepository.save(member);
        Board board = BoardCreator.create(member.getId(), "title", "content");
        boardRepository.save(board);
        insertBoardLike4(board.getId());

        // when
        BoardInfoResponseWithLikeCount response = boardService.getBoard(board.getId());

        // then
        assertThat(response.getBoardLikeCounts()).isEqualTo(4);
    }

    @DisplayName("2명 찬성 1명 반대 했을 경우")
    @Test
    void 특정_게시글_불러오기4() {
        // given
        Member member = MemberCreator.create("nickname1");
        memberRepository.save(member);
        Board board = BoardCreator.create(member.getId(), "title", "content");
        boardRepository.save(board);
        BoardLike boardLike1 = BoardLikeCreator.create(board.getId(), 1L, BoardState.LIKE);
        BoardLike boardLike2 = BoardLikeCreator.create(board.getId(), 2L, BoardState.LIKE);
        BoardLike boardLike3 = BoardLikeCreator.create(board.getId(), 3L, BoardState.UNLIKE);
        boardLikeRepository.saveAll(Arrays.asList(boardLike1, boardLike2, boardLike3));

        // when
        BoardInfoResponseWithLikeCount response = boardService.getBoard(board.getId());

        // then
        assertThat(response.getBoardLikeCounts()).isEqualTo(2);
        assertThat(response.getBoardUnLikeCounts()).isEqualTo(1);
    }

    @DisplayName("title 기준으로 검색하고 기본 10개씩 첫페이지는 0으로 가져온다.")
    @Test
    @Transactional
    void 게시글_리스트_불러오기() {
        // given
        insert10();

        // when
        final Pageable pageable = PageRequest.of(0, 10, Sort.by(DESC, "id"));
        BoardListResponse responseList = boardService.retrieveBoard("", null, pageable);

        // then
        final List<Board> boardList = boardRepository.findAll();
        assertThat(boardList).hasSize(10);
        assertThat(responseList.getBoardList()).hasSize(10);
        assertThat(responseList.getBoardList().get(0).getTitle()).isEqualTo("title10");
        assertThat(responseList.getBoardList().get(1).getTitle()).isEqualTo("title9");
        assertThat(responseList.getBoardCounts()).isEqualTo(10);
    }

    @DisplayName("title 기준으로 검색하고 기본 10개씩 첫페이지는 0으로 가져온다. - 타이틀검색어가 있을 시")
    @Test
    void 게시글_리스트_불러오기2() {
        // given
        insert10();

        // when
        final Pageable pageable = PageRequest.of(0, 10, DESC, "id");
        BoardListResponse responseList = boardService.retrieveBoard("1", null, pageable);

        // then
        final List<Board> boardList = boardRepository.findAll();
        assertThat(boardList).hasSize(10);
        assertThat(responseList.getBoardList()).hasSize(2);
        assertThat(responseList.getBoardList().get(0).getTitle()).isEqualTo("title10");
        assertThat(responseList.getBoardList().get(1).getTitle()).isEqualTo("title1");
    }

    @DisplayName("title 기준으로 검색하고 기본 10개씩 첫페이지는 0으로 가져온다. 10개만 있을 시 페이지1은 빈배열 반환")
    @Test
    void 게시글_리스트_불러오기3() {
        // given
        insert10();

        // when
        final Pageable pageable = PageRequest.of(1, 10, DESC, "id");
        BoardListResponse responseList = boardService.retrieveBoard("1", null, pageable);

        // then
        assertThat(responseList.getBoardList()).isEmpty();
    }

    @DisplayName("게시글 불러올 때 조회순으로 불러오기")
    @Test
    void 게시글_리스트_불러오기4() {
        // given
        Board board1 = BoardCreator.create(1L, "title1", "content1");
        board1.incrementViewCount();
        board1.incrementViewCount();
        Board board2 = BoardCreator.create(1L, "title2", "content2");
        Board board3 = BoardCreator.create(1L, "title3", "content3");
        board3.incrementViewCount();
        boardRepository.saveAll(Arrays.asList(board1, board2, board3));

        // when
        final Pageable pageable = PageRequest.of(0, 3, Sort.by(DESC, "viewCounts"));
        BoardListResponse responseList = boardService.retrieveBoard("", null, pageable);

        // then
        assertThat(responseList.getBoardList()).hasSize(3);
        assertThat(responseList.getBoardList().get(0).getBoardId()).isEqualTo(board1.getId());
        assertThat(responseList.getBoardList().get(1).getBoardId()).isEqualTo(board3.getId());
    }

    @DisplayName("게시글 불러올 때 추천순으로 불러오기")
    @Test
    void 게시글_리스트_불러오기5() {
        // given
        Board board1 = BoardCreator.create(1L, "title1", "content1");
        board1.incrementLikeCounts();
        board1.incrementLikeCounts();
        Board board2 = BoardCreator.create(1L, "title2", "content2");
        Board board3 = BoardCreator.create(1L, "title3", "content3");
        board3.incrementLikeCounts();
        boardRepository.saveAll(Arrays.asList(board1, board2, board3));

        // when
        final Pageable pageable = PageRequest.of(0, 3, Sort.by(DESC, "likeCounts"));
        BoardListResponse responseList = boardService.retrieveBoard("", null, pageable);

        // then
        assertThat(responseList.getBoardList()).hasSize(3);
        assertThat(responseList.getBoardList().get(0).getBoardId()).isEqualTo(board1.getId());
        assertThat(responseList.getBoardList().get(1).getBoardId()).isEqualTo(board3.getId());
    }

    @DisplayName("게시글 불러올 때 인권/성평등 카테고리를 불러오기")
    @Test
    void 게시글_리스트_불러오기6() {
        // given
        Board board1 = BoardCreator.create(1L, "title1", "content1");
        Board board2 = BoardCreator.create(1L, "title2", "content2");
        Board board3 = BoardCreator.create(1L, "title3", "content3", BoardCategory.HEALTH);
        boardRepository.saveAll(Arrays.asList(board1, board2, board3));

        // when
        final Pageable pageable = PageRequest.of(0, 3, Sort.by(DESC, "likeCounts"));
        BoardListResponse responseList = boardService.retrieveBoard("", BoardCategory.HUMAN, pageable);

        // then
        assertThat(responseList.getBoardList()).hasSize(2);
        assertThat(responseList.getBoardList().get(0).getBoardId()).isEqualTo(board2.getId());
        assertThat(responseList.getBoardList().get(1).getBoardId()).isEqualTo(board1.getId());
    }

    @DisplayName("게시글 불러올 때 인권/성평등 카테고리를 불러오기 - 좋아요 순으로")
    @Test
    void 게시글_리스트_불러오기7() {
        // given
        Board board1 = BoardCreator.create(1L, "title1", "content1");
        board1.incrementLikeCounts();
        board1.incrementLikeCounts();
        Board board2 = BoardCreator.create(1L, "title2", "content2");
        Board board3 = BoardCreator.create(1L, "title3", "content3", BoardCategory.HEALTH);
        board3.incrementLikeCounts();
        boardRepository.saveAll(Arrays.asList(board1, board2, board3));

        // when
        final Pageable pageable = PageRequest.of(0, 3, Sort.by(DESC, "likeCounts"));
        BoardListResponse responseList = boardService.retrieveBoard("", BoardCategory.HUMAN, pageable);

        // then
        assertThat(responseList.getBoardList()).hasSize(2);
        assertThat(responseList.getBoardList().get(0).getBoardId()).isEqualTo(board1.getId());
        assertThat(responseList.getBoardList().get(1).getBoardId()).isEqualTo(board2.getId());
    }

    @DisplayName("게시글 찬성/반대를 한다. 이미 게시글에 찬성이나 반대를 했으면 boardState 의 값으로 업데이트 쳐주고 찬성/반대를 한적이 없으면 새로 생성해준다.")
    @Test
    void 게시글_찬성_반대1() {
        // given
        Board board = BoardCreator.create(1L, "title", "content");
        boardRepository.save(board);
        BoardLikeRequest request = BoardLikeRequest.testInstance(board.getId(), BoardState.LIKE);

        // when
        boardService.boardLikeOrUnLike(request, 1L);

        // then
        List<BoardLike> boardLikeList = boardLikeRepository.findAll();
        assertThat(boardLikeList).hasSize(1);
    }

    @DisplayName("이미 찬성을 한 상태에서 반대로 변경")
    @Test
    @Transactional
    void 게시글_찬성_반대2() {
        // given
        Board board = BoardCreator.create(1L, "title", "content");
        boardRepository.save(board);
        BoardLike boardLike = BoardLikeCreator.create(board.getId(), 1L, BoardState.LIKE);
        boardLikeRepository.save(boardLike);

        BoardLikeRequest request = BoardLikeRequest.testInstance(board.getId(), BoardState.UNLIKE);

        // when
        boardService.boardLikeOrUnLike(request, 1L);

        // then
        List<BoardLike> boardLikeList = boardLikeRepository.findAll();
        assertThat(boardLikeList).hasSize(1);
        assertThat(boardLikeList.get(0).getBoardState()).isEqualTo(BoardState.UNLIKE);
    }

    @DisplayName("찬성/반대 삭제")
    @Test
    void 게시글_찬성_반대_삭제() {
        // given
        Board board = BoardCreator.create(1L, "title", "content");
        boardRepository.save(board);
        BoardLike boardLike = BoardLikeCreator.create(board.getId(), 1L, BoardState.LIKE);
        boardLikeRepository.save(boardLike);

        // when
        boardService.deleteBoardLikeOrUnLike(board.getId(), 1L);

        // then
        List<BoardLike> boardLikeList = boardLikeRepository.findAll();
        assertThat(boardLikeList).isEmpty();
    }

    private static class MockPetitionApiCaller implements PetitionClient {
        @Override
        public PetitionResponse getPetitionInfo(Long id) {
            return PetitionResponse.of("감자좀 살려주세요", "감자가 위험해요", "10000", "청원중", "인권/성평등", "2021-06-22", "2021-06-23");
        }
    }

}
