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
import com.example.nationalpetition.dto.member.request.AlarmRequest;
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
import org.springframework.transaction.annotation.Transactional;


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
    @DisplayName("회원 정보 조회 (마이페이지)")
    void getMyInfo()  {
        //given
        final Member member = 회원가입하기();
        //when
        final MemberResponse memberResponse = memberService.findById(member.getId());
        //then
        assertThat(memberResponse.getName()).isEqualTo("이름");
        assertThat(memberResponse.getEmail()).isEqualTo("aa@naver.com");
    }

    @Test
    @DisplayName("회원 정보 조회 중 리포지토리에 저장되지 않은 Id값으로 회원을 찾는 경우 예외 발생")
    void getMyInfo_fail() {
        //given
        Member member = memberRepository.save(Member.of("yerimkoko", "gochi97@naver.com", "wefwef"));
        //when && then
        assertThatThrownBy( () ->memberService.findById(member.getId()+1L))
                .isInstanceOf(NotFoundException.class);
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
        assertThat(message).isEqualTo(MessageType.NICKNAME_SUCCESS.getMessage());
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
        assertThat(message).isEqualTo(MessageType.NICKNAME_DUPLICATE.getMessage());
    }

    @Test
    @DisplayName("닉네임 등록 실패 --> 이미 닉네임을 등록한 계정")
    void addNickName_fail2() {
        //given
        final Member member = 회원가입하기();
        final NickNameRequest request = new NickNameRequest("닉네임222");
        //when && then
        assertThatThrownBy(() -> memberService.addNickName(member.getId(), request))
                .isInstanceOf(ConflictException.class);
    }


    @Test
    @DisplayName("마이페이지 - 내가 쓴 게시글 조회")
    void getMyBoardList() {
        //given
        final Member member = 회원가입하기();
        final Long boardId = 게시글_생성하기(member.getId(), 12);
        댓글_대댓글_생성하기(member, boardId, 10);
        좋아요_싫어요_생성하기(boardId,10);
        게시글_조회수_증가시키기(boardId, 10);

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
    @DisplayName("마이페이지 - 페이징 테스트")
    void getMyBoardList2() {
        //given
        final Member member = 회원가입하기();

        게시글_생성하기(member.getId(), 12);
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
    @DisplayName("회원 탈퇴 성공")
    void deleteMember() {
        //given
        final Member member = 회원가입하기();
        //when
        final String message = memberService.deleteMember(member.getId());
        //then
        assertThat(message).isEqualTo(MessageType.DELETE_MEMBER.getMessage());
        assertThat(deleteMemberRepository.findAll().size()).isEqualTo(1);
        assertThatThrownBy(() -> memberService.findById(member.getId()))
                .isInstanceOf(NotFoundException.class);
    }


    @Test
    @DisplayName("ID값으로 회원 찾기")
    void findById() {
        //given
        final Member member = 회원가입하기();
        //when
        final MemberResponse memberResponse = memberService.findById(member.getId());
        //then
        assertThat(memberResponse.getName()).isEqualTo("이름");
        assertThat(memberResponse.getNickName()).isEqualTo("닉네임");
        assertThat(memberResponse.getEmail()).isEqualTo("aa@naver.com");
    }

    @Test
    @DisplayName("Id값으로 회원 찾기 - 실패")
    void findById_fail() {
        //given
        final Member member = 회원가입하기();
        //when && then
        assertThatThrownBy(() -> memberService.findById(member.getId() + 1L))
                .isInstanceOf(NotFoundException.class);

    }

    @Test
    @DisplayName("이메일로 회원 찾기")
    void findByEmail() {
        //given
        회원가입하기();
        final String email = "aa@naver.com";
        //when
        final Member member = memberService.findByEmail(email);
        //then
        assertThat(member.getName()).isEqualTo("이름");
        assertThat(member.getNickName()).isEqualTo("닉네임");
        assertThat(member.getEmail()).isEqualTo("aa@naver.com");
    }

    @Test
    @DisplayName("이메일로 회원 찾기 - 실패")
    void findByEmail_fail() {
        //given
        회원가입하기();
        final String email = "fail@naver.com";
        //when && then
        assertThatThrownBy(() -> memberService.findByEmail(email))
                .isInstanceOf(NotFoundException.class);
    }

    @Transactional
    @Test
    @DisplayName("게시글 알람 설정하기")
    void changeBoardAlarm() {
        //given
        final Member member = 회원가입하기();
        final AlarmRequest request = new AlarmRequest(true);
        //when
        memberService.changeAlarm(member.getId(), request);
        //then
        assertThat(member.getIsAlarm()).isTrue();
    }

    protected Long 게시글_생성하기(Long memberId, int count) {
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


    protected void 댓글_대댓글_생성하기(Member member, Long boardId, int count) {
        for (int i = 0; i < count; i++) {
            commentRepository.save(Comment.newRootComment(member, boardId, "댓글" + i));
            commentRepository.save(Comment.newChildComment((long) i, member, boardId, 2, "대댓글" + i));
        }
    }

    protected void 좋아요_싫어요_생성하기(Long boardId, int count) {
        BoardLikeRequest likeRequest = BoardLikeRequest.testInstance(boardId, BoardState.LIKE);
        BoardLikeRequest unlikeRequest = BoardLikeRequest.testInstance(boardId, BoardState.UNLIKE);
        for (long i = 0; i < count; i++) {
            boardLikeRepository.save(likeRequest.toEntity(i));
            boardLikeRepository.save(unlikeRequest.toEntity(i + 10));
        }
    }
    protected Member 회원가입하기() {
        final Member member = Member.of("이름", "aa@naver.com", "picturepicture");
        member.addNickName("닉네임");
        return memberRepository.save(member);
    }

    protected void 게시글_조회수_증가시키기(Long boardId, int count) {
        final Board board = boardRepository.findById(boardId).orElseThrow(() -> new NotFoundException(String.format("해당하는 게시글 (%s)이 존재하지 않습니다", boardId), ErrorCode.NOT_FOUND_EXCEPTION_BOARD));
        for (int i = 0; i < count; i++) {
            board.incrementViewCount();
        }
        boardRepository.save(board);
    }

}
