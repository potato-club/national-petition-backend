package com.example.nationalpetition.service.member;

import com.example.nationalpetition.domain.board.Board;
import com.example.nationalpetition.domain.board.BoardCategory;
import com.example.nationalpetition.domain.board.BoardState;
import com.example.nationalpetition.domain.board.repository.BoardLikeRepository;
import com.example.nationalpetition.domain.board.repository.BoardRepository;
import com.example.nationalpetition.domain.comment.Comment;
import com.example.nationalpetition.domain.comment.CommentRepository;
import com.example.nationalpetition.domain.member.entity.Member;
import com.example.nationalpetition.domain.member.repository.DeleteMemberRepository;
import com.example.nationalpetition.domain.member.repository.MemberRepository;
import com.example.nationalpetition.dto.board.request.BoardLikeRequest;
import com.example.nationalpetition.dto.member.request.MemberPageRequest;
import com.example.nationalpetition.dto.member.request.NickNameRequest;
import com.example.nationalpetition.dto.member.response.MemberResponse;
import com.example.nationalpetition.dto.member.response.MyPageBoardListResponse;
import com.example.nationalpetition.utils.error.exception.ConflictException;
import com.example.nationalpetition.utils.message.MessageType;
import com.example.nationalpetition.utils.error.ErrorCode;
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
        commentRepository.deleteAll();
        boardRepository.deleteAll();
        boardLikeRepository.deleteAll();
        memberRepository.deleteAll();
        deleteMemberRepository.deleteAll();

    }

    @Test
    @DisplayName("?????? ?????? ?????? (???????????????)")
    void getMyInfo()  {
        //given
        final Member member = ??????????????????();
        //when
        final MemberResponse memberResponse = memberService.findById(member.getId());
        //then
        assertThat(memberResponse.getName()).isEqualTo("??????");
        assertThat(memberResponse.getEmail()).isEqualTo("aa@naver.com");
    }

    @Test
    @DisplayName("?????? ?????? ?????? ??? ?????????????????? ???????????? ?????? Id????????? ????????? ?????? ?????? ?????? ??????")
    void getMyInfo_fail() {
        //given
        Member member = memberRepository.save(Member.of("yerimkoko", "gochi97@naver.com", "wefwef"));
        //when && then
        assertThatThrownBy( () ->memberService.findById(member.getId()+1L))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    @DisplayName("?????? ????????? ?????? ??????")
    void addNickName() {
        //given
        final Member member = memberRepository.save(Member.of("??????", "email@email.com", "picture"));
        final NickNameRequest request = new NickNameRequest("?????????");
        //when
        final String message = memberService.addNickName(member.getId(), request);
        //then
        assertThat(message).isEqualTo(MessageType.NICKNAME_SUCCESS.getMessage());
    }

    @Test
    @DisplayName("????????? ?????? ?????? --> ???????????? ?????????")
    void addNickName_fail1() {
        //given
        ??????????????????();
        final Member member = memberRepository.save(Member.of("?????????", "eee@ee.ee", "piiicture"));
        final NickNameRequest request = new NickNameRequest("?????????");
        //when
        final String message = memberService.addNickName(member.getId(), request);
        //then
        assertThat(message).isEqualTo(MessageType.NICKNAME_DUPLICATE.getMessage());
    }

    @Test
    @DisplayName("????????? ?????? ?????? --> ?????? ???????????? ????????? ??????")
    void addNickName_fail2() {
        //given
        final Member member = ??????????????????();
        final NickNameRequest request = new NickNameRequest("?????????222");
        //when && then
        assertThatThrownBy(() -> memberService.addNickName(member.getId(), request))
                .isInstanceOf(ConflictException.class);
    }


    @Test
    @DisplayName("??????????????? - ?????? ??? ????????? ??????")
    void getMyBoardList() {
        //given
        final Member member = ??????????????????();
        final Long boardId = ?????????_????????????(member.getId(), 12);
        ??????_?????????_????????????(member, boardId, 10);
        ?????????_?????????_????????????(boardId,10);
        ?????????_?????????_???????????????(boardId, 10);

        final MemberPageRequest request = new MemberPageRequest(1, 10);
        //when
        final MyPageBoardListResponse myBoardList = memberService.getMyBoardList(member.getId(), request);
        //then

        assertThat(boardRepository.findAll().size()).isEqualTo(12);
        assertThat(myBoardList.getMyBoardList().size()).isEqualTo(10);

        assertThat(myBoardList.getMyBoardList().get(0).getTitle()).isEqualTo("titleLast");
        assertThat(myBoardList.getMyBoardList().get(9).getTitle()).isEqualTo("title2");

        assertThat(myBoardList.getMyBoardList().get(0).getBoardLikeCounts()).isEqualTo(10);
        assertThat(myBoardList.getMyBoardList().get(0).getBoardUnLikeCounts()).isEqualTo(10);

        assertThat(myBoardList.getMyBoardList().get(0).getViewCounts()).isEqualTo(10);

        assertThat(myBoardList.getMyBoardList().get(0).getBoardCommentCounts()).isEqualTo(20);
        assertThat(myBoardList.getBoardCounts()).isEqualTo(12);
    }




    @Test
    @DisplayName("??????????????? - ????????? ?????????")
    void getMyBoardList2() {
        //given
        final Member member = ??????????????????();

        ?????????_????????????(member.getId(), 12);
        final Pageable pageable = PageRequest.of(0, 10, Sort.by(DESC, "id"));
        //when
        final Page<Board> page = boardRepository.findByMemberIdAndIsDeletedIsFalse(member.getId(), pageable);
        //then
        assertThat(page.getTotalElements()).isEqualTo(12);
        assertThat(page.getContent().size()).isEqualTo(10);
        assertThat(page.getNumber()).isEqualTo(0);
        assertThat(page.getTotalPages()).isEqualTo(2);
        assertThat(page.isFirst()).isTrue();
        assertThat(page.hasNext()).isTrue();


    }
    @Test
    @DisplayName("?????? ?????? ??????")
    void deleteMember() {
        //given
        final Member member = ??????????????????();
        //when
        final String message = memberService.deleteMember(member.getId());
        //then
        assertThat(message).isEqualTo(MessageType.DELETE_MEMBER.getMessage());
        assertThat(deleteMemberRepository.findAll().size()).isEqualTo(1);
        assertThatThrownBy(() -> memberService.findById(member.getId()))
                .isInstanceOf(NotFoundException.class);
    }


    @Test
    @DisplayName("ID????????? ?????? ??????")
    void findById() {
        //given
        final Member member = ??????????????????();
        //when
        final MemberResponse memberResponse = memberService.findById(member.getId());
        //then
        assertThat(memberResponse.getName()).isEqualTo("??????");
        assertThat(memberResponse.getNickName()).isEqualTo("?????????");
        assertThat(memberResponse.getEmail()).isEqualTo("aa@naver.com");
    }

    @Test
    @DisplayName("Id????????? ?????? ?????? - ??????")
    void findById_fail() {
        //given
        final Member member = ??????????????????();
        //when && then
        assertThatThrownBy(() -> memberService.findById(member.getId() + 1L))
                .isInstanceOf(NotFoundException.class);

    }

    @Test
    @DisplayName("???????????? ?????? ??????")
    void findByEmail() {
        //given
        ??????????????????();
        final String email = "aa@naver.com";
        //when
        final Member member = memberService.findByEmail(email);
        //then
        assertThat(member.getName()).isEqualTo("??????");
        assertThat(member.getNickName()).isEqualTo("?????????");
        assertThat(member.getEmail()).isEqualTo("aa@naver.com");
    }

    @Test
    @DisplayName("???????????? ?????? ?????? - ??????")
    void findByEmail_fail() {
        //given
        ??????????????????();
        final String email = "fail@naver.com";
        //when && then
        assertThatThrownBy(() -> memberService.findByEmail(email))
                .isInstanceOf(NotFoundException.class);
    }

    protected Long ?????????_????????????(Long memberId, int count) {
        for (int i = 0; i < count-1; i++) {
            Board board = Board.builder()
                    .memberId(memberId)
                    .content("content")
                    .category(BoardCategory.HEALTH)
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
                .category(BoardCategory.HEALTH)
                .petitionContent("petitionContent")
                .petitionsCount("10000")
                .petitionTitle("petitionTitle")
                .petitionUrl("url")
                .title("titleLast")
                .build();
        return boardRepository.save(board).getId();

    }


    protected void ??????_?????????_????????????(Member member, Long boardId, int count) {
        for (int i = 0; i < count; i++) {
            commentRepository.save(Comment.newRootComment(member, boardId, "??????" + i));
            commentRepository.save(Comment.newChildComment((long) i, member, boardId, 2, "?????????" + i));
        }
    }

    protected void ?????????_?????????_????????????(Long boardId, int count) {
        BoardLikeRequest likeRequest = BoardLikeRequest.testInstance(boardId, BoardState.LIKE);
        BoardLikeRequest unlikeRequest = BoardLikeRequest.testInstance(boardId, BoardState.UNLIKE);
        for (long i = 0; i < count; i++) {
            boardLikeRepository.save(likeRequest.toEntity(i));
            boardLikeRepository.save(unlikeRequest.toEntity(i + 10));
        }
    }
    protected Member ??????????????????() {
        final Member member = Member.of("??????", "aa@naver.com", "picturepicture");
        member.addNickName("?????????");
        return memberRepository.save(member);
    }

    protected void ?????????_?????????_???????????????(Long boardId, int count) {
        final Board board = boardRepository.findById(boardId).orElseThrow(() -> new NotFoundException(String.format("???????????? ????????? (%s)??? ???????????? ????????????", boardId), ErrorCode.NOT_FOUND_EXCEPTION_BOARD));
        for (int i = 0; i < count; i++) {
            board.incrementViewCount();
        }
        boardRepository.save(board);
    }

}
