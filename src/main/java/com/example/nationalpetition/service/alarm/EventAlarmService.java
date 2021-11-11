package com.example.nationalpetition.service.alarm;

import com.example.nationalpetition.domain.board.Board;
import com.example.nationalpetition.domain.board.repository.BoardRepository;
import com.example.nationalpetition.domain.comment.Comment;
import com.example.nationalpetition.domain.comment.CommentRepository;
import com.example.nationalpetition.domain.member.entity.Member;
import com.example.nationalpetition.domain.member.repository.MemberRepository;
import com.example.nationalpetition.service.alarm.dto.request.CommentCreatedAlarmMessage;
import com.example.nationalpetition.service.alarm.dto.request.ReplyCommentCreatedAlarmMessage;
import com.example.nationalpetition.service.board.BoardServiceUtils;
import com.example.nationalpetition.service.comment.CommentServiceUtils;
import com.example.nationalpetition.service.member.MemberServiceUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Set;

@RequiredArgsConstructor
@Service
public class EventAlarmService {

	private final AlarmService alarmService;

	private final CommentRepository commentRepository;
	private final BoardRepository boardRepository;
	private final MemberRepository memberRepository;

	@Transactional
	public void notifyCreatedComment(Long commentId) {
		Comment comment = CommentServiceUtils.findCommentById(commentRepository, commentId);
		Board board = BoardServiceUtils.findBoardById(boardRepository, comment.getBoardId());

		if (comment.isRootComment()) {
			sendRootCommentCreatedAlarm(comment, board);
			return;
		}
		sendReplyCommentCreatedAlarm(comment, board);
	}

	private void sendRootCommentCreatedAlarm(Comment comment, Board board) {
		if (board.isCreator(comment.getMemberId())) {
			return;
		}
		Member creator = MemberServiceUtils.findMemberById(memberRepository, comment.getMemberId());
		alarmService.sendAlarm(Set.of(board.getMemberId()), CommentCreatedAlarmMessage.of(board.getId(), board.getTitle(), creator.getNickName()));
	}

	private void sendReplyCommentCreatedAlarm(Comment comment, Board board) {
		Comment parentComment = CommentServiceUtils.findCommentById(commentRepository, comment.getParentId());
		if (parentComment.getMemberId().equals(comment.getMemberId())) {
			return;
		}
		Member creator = MemberServiceUtils.findMemberById(memberRepository, comment.getMemberId());
		alarmService.sendAlarm(Set.of(parentComment.getMemberId()), ReplyCommentCreatedAlarmMessage.of(board.getId(), board.getTitle(), creator.getNickName()));
	}

}
