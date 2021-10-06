package com.example.nationalpetition.service.member;

import com.example.nationalpetition.domain.board.Board;
import com.example.nationalpetition.domain.board.BoardState;
import com.example.nationalpetition.domain.board.repository.BoardLikeRepository;
import com.example.nationalpetition.domain.board.repository.BoardRepository;
import com.example.nationalpetition.domain.comment.Comment;
import com.example.nationalpetition.domain.comment.CommentRepository;
import com.example.nationalpetition.domain.member.entity.Member;
import com.example.nationalpetition.domain.member.repository.DeleteMemberRepository;
import com.example.nationalpetition.domain.member.repository.MemberRepository;
import com.example.nationalpetition.dto.board.request.BoardLikeRequest;
import com.example.nationalpetition.dto.board.response.BoardInfoResponseInMyPage;
import com.example.nationalpetition.dto.member.MessageConst;
import com.example.nationalpetition.dto.member.request.NickNameRequest;
import com.example.nationalpetition.dto.member.response.MemberResponse;
import com.example.nationalpetition.utils.error.ErrorCode;
import com.example.nationalpetition.utils.error.exception.AlreadyExistException;
import com.example.nationalpetition.utils.error.exception.DuplicateException;
import com.example.nationalpetition.utils.error.exception.NotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;


import static org.assertj.core.api.Assertions.*;
import static org.springframework.data.domain.Sort.Direction.DESC;

@SpringBootTest
public class MemberServiceTest {

    @Autowired
    MemberService memberService;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    BoardRepository boardRepository;

    @Autowired
    BoardLikeRepository boardLikeRepository;

    @Autowired
    CommentRepository commentRepository;

    @Autowired
    DeleteMemberRepository deleteMemberRepository;

    @AfterEach
    public void clear() {
        memberRepository.deleteAll();
        boardRepository.deleteAll();
        boardLikeRepository.deleteAll();
        commentRepository.deleteAll();
        deleteMemberRepository.deleteAll();

    }

    @Test
    @DisplayName("회원 정보 조회 (마이페이지)")
    void getMyInfo()  {
        //given
        final Long memberId = 회원가입하기();
        //when
        final MemberResponse memberResponse = memberService.findById(memberId);
        //then
        assertThat(memberResponse.getName()).isEqualTo("이름");
        assertThat(memberResponse.getEmail()).isEqualTo("aa@naver.com");
    }

    @Test
    @DisplayName("회원 정보 조회 중 리포지토리에 저장되지 않은 Id값으로 회원을 찾는 경우 예외 발생")
    void getMyInfo_fail() {
        //given
        Long memberId = 123123L;
        //when && then
        assertThatThrownBy( () ->memberService.findById(memberId))
                .isInstanceOf(NotFoundException.class)
                .hasMessage(ErrorCode.NOT_FOUND_EXCEPTION_USER.getMessage());
    }

    @Test
    @DisplayName("회원 닉네임 등록 성공")
    void addNickName() {
        //given
        final Member member = memberRepository.save(Member.of("이름", "email@email.com", "picture"));
        final NickNameRequest request = new NickNameRequest("닉네임");
        //when
        final String message = memberService.addNickName(member.getId(), request);
        //then
        assertThat(message).isEqualTo(MessageConst.SUCCESS);
    }

    @Test
    @DisplayName("닉네임 등록 실패 --> 중복되는 닉네임")
    void addNickName_fail1() {
        //given
        회원가입하기();
        final Member member = memberRepository.save(Member.of("아아아", "eee@ee.ee", "piiicture"));
        final NickNameRequest request = new NickNameRequest("닉네임");
        //when
        final String message = memberService.addNickName(member.getId(), request);
        //then
        assertThat(message).isEqualTo(MessageConst.DUPLICATE);
    }

    @Test
    @DisplayName("닉네임 등록 실패 --> 이미 닉네임을 등록한 계정")
    void addNickName_fail2() {
        //given
        final Long memberId = 회원가입하기();
        final NickNameRequest request = new NickNameRequest("닉네임222");
        //when && then
        assertThatThrownBy(() -> memberService.addNickName(memberId, request))
                .isInstanceOf(AlreadyExistException.class)
                .hasMessage(ErrorCode.ALREADY_EXIST_EXCEPTION_ADD_NICKNAME.getMessage());

    }



    @Test
    @DisplayName("마이페이지 - 내가 쓴 게시글 조회")
    void getMyBoardList() {
        //given
        final Long memberId = 회원가입하기();
        final Long boardId = 게시글생성하기(memberId, 12);
        댓글대댓글생성하기(memberId, boardId, 10);
        좋아요싫어요생성하기(boardId,10);
        게시글조회수증가시키기(boardId, 10);

        final Pageable pageable = PageRequest.of(0, 10, Sort.by(DESC, "id"));
        //when
        final List<BoardInfoResponseInMyPage> myBoardList = memberService.getMyBoardList(memberId, pageable);
        //then
        assertThat(boardRepository.findAll().size()).isEqualTo(12);
        assertThat(myBoardList.size()).isEqualTo(10);

        assertThat(myBoardList.get(0).getTitle()).isEqualTo("titleLast");
        assertThat(myBoardList.get(9).getTitle()).isEqualTo("title2");

        assertThat(myBoardList.get(0).getBoardLikeCounts()).isEqualTo(10);
        assertThat(myBoardList.get(0).getBoardUnLikeCounts()).isEqualTo(10);

        assertThat(myBoardList.get(0).getViewCounts()).isEqualTo(10);

        assertThat(myBoardList.get(0).getBoardCommentCounts()).isEqualTo(20);
    }




    @Test
    @DisplayName("마이페이지 - 페이징 테스트")
    void getMyBoardList2() {
        //given
        final Long memberId = 회원가입하기();
        게시글생성하기(memberId, 12);
        final Pageable pageable = PageRequest.of(0, 10, Sort.by(DESC, "id"));
        //when
        final Page<Board> page = boardRepository.findByMemberIdAndIsDeletedIsFalse(memberId, pageable);
        //then
        assertThat(page.getTotalElements()).isEqualTo(12);
        assertThat(page.getContent().size()).isEqualTo(10);
        assertThat(page.getNumber()).isEqualTo(0);
        assertThat(page.getTotalPages()).isEqualTo(2);
        assertThat(page.isFirst()).isTrue();
        assertThat(page.hasNext()).isTrue();


    }
    @Test
    @DisplayName("회원 탈퇴 성공")
    void deleteMember() {
        //given
        final Long memberId = 회원가입하기();
        //when
        final String message = memberService.deleteMember(memberId);
        //then
        assertThat(message).isEqualTo(MessageConst.MESSAGE);
        assertThat(deleteMemberRepository.findAll().size()).isEqualTo(1);
        assertThatThrownBy(() -> memberService.findById(memberId))
                .isInstanceOf(NotFoundException.class)
                .hasMessage(ErrorCode.NOT_FOUND_EXCEPTION_USER.getMessage());

    }



    protected Long 게시글생성하기(Long memberId, int count) {
        for (int i = 0; i < count-1; i++) {
            Board board = Board.builder()
                    .memberId(memberId)
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
        Board board = Board.builder()
                .memberId(memberId)
                .content("content")
                .category("category")
                .petitionContent("petitionContent")
                .petitionsCount("10000")
                .petitionTitle("petitionTitle")
                .petitionUrl("url")
                .title("titleLast")
                .build();
        return boardRepository.save(board).getId();

    }


    protected void 댓글대댓글생성하기(Long memberId, Long boardId, int count) {
        for (int i = 0; i < count; i++) {
            commentRepository.save(Comment.newRootComment(memberId, boardId, "댓글" + i));
            commentRepository.save(Comment.newChildComment((long) i, memberId, boardId, 2, "대댓글" + i));
        }
    }

    protected void 좋아요싫어요생성하기(Long boardId, int count) {
        BoardLikeRequest likeRequest = BoardLikeRequest.testInstance(boardId, BoardState.LIKE);
        BoardLikeRequest unlikeRequest = BoardLikeRequest.testInstance(boardId, BoardState.UNLIKE);
        for (long i = 0; i < count; i++) {
            boardLikeRepository.save(likeRequest.toEntity(i));
            boardLikeRepository.save(unlikeRequest.toEntity(i + 10));
        }
    }
    protected Long 회원가입하기() {
        final Member member = Member.of("이름", "aa@naver.com", "picturepicture");
        member.addNickName("닉네임");
        return memberRepository.save(member).getId();
    }

    protected void 게시글조회수증가시키기(Long boardId, int count) {
        final Board board = boardRepository.findById(boardId).orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND_EXCEPTION_BOARD));
        for (int i = 0; i < count; i++) {
            board.incrementViewCount();
        }
        boardRepository.save(board);
    }

}
