package com.example.nationalpetition.service.alarm;

import com.example.nationalpetition.domain.alarm.Alarm;
import com.example.nationalpetition.domain.alarm.AlarmRepository;
import com.example.nationalpetition.domain.board.Board;
import com.example.nationalpetition.domain.board.repository.BoardRepository;
import com.example.nationalpetition.domain.comment.Comment;
import com.example.nationalpetition.domain.comment.CommentRepository;
import com.example.nationalpetition.domain.member.entity.Member;
import com.example.nationalpetition.domain.member.repository.MemberRepository;
import com.example.nationalpetition.testObject.BoardCreator;
import com.example.nationalpetition.testObject.MemberCreator;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static com.example.nationalpetition.domain.alarm.AlarmEventType.COMMENT_CREATED;
import static com.example.nationalpetition.domain.alarm.AlarmEventType.RE_COMMENT_CREATED;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class EventAlarmServiceTest {

	@Autowired
	private CommentRepository commentRepository;

	@Autowired
	private BoardRepository boardRepository;

	@Autowired
	private MemberRepository memberRepository;

	@Autowired
	private EventAlarmService eventAlarmService;

	@Autowired
	private AlarmRepository alarmRepository;

	@AfterEach
	void cleanUp() {
		boardRepository.deleteAll();
		commentRepository.deleteAll();
		memberRepository.deleteAll();
		alarmRepository.deleteAll();
	}

	@Test
	void 댓글이_작성되면_게시글_작성자한테_알림이_간다() {
		// given
		Member boardCreator = MemberCreator.create("will.seungho@gmail.com", "게시글 작성자");
		Member commentWriter = MemberCreator.create("will@gmail.com", "댓글 작성자");
		memberRepository.saveAll(List.of(boardCreator, commentWriter));

		Board board = BoardCreator.create(boardCreator.getId(), "게시글 제목", "게시글 내용");
		boardRepository.save(board);

		Comment comment = Comment.newRootComment(commentWriter, board.getId(), "댓글 내용");
		commentRepository.save(comment);

		// when
		eventAlarmService.notifyCreatedComment(comment.getId());

		// then
		List<Alarm> alarms = alarmRepository.findAll();
		assertThat(alarms).hasSize(1);
		assertThat(alarms.get(0).getMemberId()).isEqualTo(boardCreator.getId());
		assertThat(alarms.get(0).getEventType()).isEqualTo(COMMENT_CREATED);
		assertThat(alarms.get(0).getBoardId()).isEqualTo(board.getId());
	}

	@Test
	void 댓글_작성시_게시글_작성자와_같은_유저면_알림이_가지_않는다() {
		// given
		Member boardCreator = MemberCreator.create("will.seungho@gmail.com", "게시글 작성자");
		memberRepository.save(boardCreator);

		Board board = BoardCreator.create(boardCreator.getId(), "게시글 제목", "게시글 내용");
		boardRepository.save(board);

		Comment comment = Comment.newRootComment(boardCreator, board.getId(), "댓글 내용");
		commentRepository.save(comment);

		// when
		eventAlarmService.notifyCreatedComment(comment.getId());

		// then
		List<Alarm> alarms = alarmRepository.findAll();
		assertThat(alarms).isEmpty();
	}

	@Test
	void 대댓글_작성시_부모_댓글_작성자에게_알림이_간다() {
		// given
		Member boardCreator = MemberCreator.create("boardCreator@gmail.com", "게시글 작성자");
		Member parentCommentWriter = MemberCreator.create("parentWriter@gmail.com", "부모 댓글 작성자");
		Member commentWriter = MemberCreator.create("childWriter@gmail.com", "대댓글 작성자");
		memberRepository.saveAll(List.of(boardCreator, commentWriter, parentCommentWriter));

		Board board = BoardCreator.create(boardCreator.getId(), "게시글 제목", "게시글 내용");
		boardRepository.save(board);

		Comment comment = Comment.newRootComment(parentCommentWriter, board.getId(), "댓글 내용");
		commentRepository.save(comment);

		Comment childComment = Comment.newChildComment(comment.getId(), commentWriter, board.getId(), comment.getDepth() + 1, "대댓글 내용");
		commentRepository.save(childComment);

		// when
		eventAlarmService.notifyCreatedComment(childComment.getId());

		// then
		List<Alarm> alarms = alarmRepository.findAll();
		assertThat(alarms).hasSize(1);
		assertThat(alarms.get(0).getMemberId()).isEqualTo(parentCommentWriter.getId());
		assertThat(alarms.get(0).getEventType()).isEqualTo(RE_COMMENT_CREATED);
		assertThat(alarms.get(0).getBoardId()).isEqualTo(board.getId());
	}

	@Test
	void 대댓글_작성시_부모_댓글_작성자와_같은_유저면_알림이_가지않는다() {
		// given
		Member boardCreator = MemberCreator.create("boardCreator@gmail.com", "게시글 작성자");
		Member commentWriter = MemberCreator.create("parentWriter@gmail.com", "부모 댓글 작성자");
		memberRepository.saveAll(List.of(boardCreator, commentWriter));

		Board board = BoardCreator.create(boardCreator.getId(), "게시글 제목", "게시글 내용");
		boardRepository.save(board);

		Comment comment = Comment.newRootComment(commentWriter, board.getId(), "댓글 내용");
		commentRepository.save(comment);

		Comment childComment = Comment.newChildComment(comment.getId(), commentWriter, board.getId(), comment.getDepth() + 1, "대댓글 내용");
		commentRepository.save(childComment);

		// when
		eventAlarmService.notifyCreatedComment(childComment.getId());

		// then
		List<Alarm> alarms = alarmRepository.findAll();
		assertThat(alarms).isEmpty();
	}

}