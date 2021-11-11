package com.example.nationalpetition.service.alarm;

import com.example.nationalpetition.domain.alarm.entity.CommentAlarm;
import com.example.nationalpetition.domain.alarm.entity.ReplyCommentAlarm;
import com.example.nationalpetition.domain.alarm.repository.CommentAlarmRepository;
import com.example.nationalpetition.domain.alarm.repository.ReplyCommentAlarmRepository;
import com.example.nationalpetition.domain.board.Board;
import com.example.nationalpetition.domain.board.repository.BoardRepository;
import com.example.nationalpetition.domain.comment.Comment;
import com.example.nationalpetition.domain.comment.CommentRepository;
import com.example.nationalpetition.domain.member.entity.Member;
import com.example.nationalpetition.utils.error.ErrorCode;
import com.example.nationalpetition.utils.error.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class AlarmServiceImpl implements AlarmService {

    private final CommentAlarmRepository commentAlarmRepository;
    private final ReplyCommentAlarmRepository replyCommentAlarmRepository;
    private final BoardRepository boardRepository;
    private final CommentRepository commentRepository;


    @Transactional
    @Override
    public void createCommentAlarm(Board board, String nickName, Long commentId) {

        // 게시글 페이지에서 댓글 알람 설정을 OFF 해놓았다면
        if (!board.getIsCommentAlarm()) {
            return;
        }

        // 게시글 작성자
        final Member member = boardRepository.findMemberById(board.getId()).orElseThrow(() -> new NotFoundException(String.format("게시글 작성자 (%s)를 찾을 수 없습니다", board.getId()), ErrorCode.NOT_FOUND_EXCEPTION_USER));

        // 마이페이지 알람 설정 안한 게시글 작성자면
        if (!member.getIsAlarm()) {
            return;
        }

        // 게시글 작성자가 댓글을 달았다면
        final Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new NotFoundException("멤버를 찾을 수 없어요", ErrorCode.NOT_FOUND_EXCEPTION_COMMENT));
        if (board.getMemberId().equals(comment.getMember().getId())) {
            return;
        }

        // 알람 설정한 사용자에게 보여줄 메시지 만들기
        final String message = AlarmServiceUtils.createCommentAlarmMessage(nickName, board.getTitle());

        final CommentAlarm commentAlarm = CommentAlarm.of(board.getId(), member.getId(), commentId, message);

        commentAlarmRepository.save(commentAlarm);
    }

    @Transactional
    @Override
    public void createReplyCommentAlarm(Board board, String nickName, Long parentId, Long commentId) {

        // 게시글 페이지에서 댓글 알람 설정을 OFF 해놓았다면
        if (!board.getIsReplyCommentAlarm()) {
            return;
        }

        // 게시글 작성자
        final Member member = boardRepository.findMemberById(board.getId()).orElseThrow(() -> new NotFoundException(String.format("게시글 작성자 (%s)를 찾을 수 없습니다", board.getId()), ErrorCode.NOT_FOUND_EXCEPTION_USER));

        // 마이페이지 알람 설정 안한 게시글 작성자면
        if (!member.getIsAlarm()) {
            return;
        }

        // 게시글 작성자가 대댓글을 달았다면
        final Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new NotFoundException("멤버를 찾을 수 없어요", ErrorCode.NOT_FOUND_EXCEPTION_COMMENT));
        if (board.getMemberId().equals(comment.getMember().getId())) {
            return;
        }

        // 알람 설정한 사용자에게 보여줄 메시지 만들기
        final String message = AlarmServiceUtils.createReplyMessage(nickName, board.getTitle());

        final ReplyCommentAlarm replyCommentAlarm = ReplyCommentAlarm.of(board.getId(), member.getId(), parentId, commentId, message);

        replyCommentAlarmRepository.save(replyCommentAlarm);
    }
}
