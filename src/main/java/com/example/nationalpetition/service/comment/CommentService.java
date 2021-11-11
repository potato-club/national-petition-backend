package com.example.nationalpetition.service.comment;

import com.example.nationalpetition.domain.board.Board;
import com.example.nationalpetition.domain.board.repository.BoardRepository;
import com.example.nationalpetition.domain.comment.*;
import com.example.nationalpetition.domain.member.entity.Member;
import com.example.nationalpetition.domain.member.repository.MemberRepository;
import com.example.nationalpetition.dto.comment.request.CommentCreateDto;
import com.example.nationalpetition.dto.comment.CommentDto;
import com.example.nationalpetition.dto.comment.request.CommentUpdateDto;
import com.example.nationalpetition.dto.comment.request.LikeCommentRequestDto;
import com.example.nationalpetition.dto.comment.response.CommentPageResponseDto;
import com.example.nationalpetition.service.alarm.AlarmService;
import com.example.nationalpetition.service.board.BoardServiceUtils;
import com.example.nationalpetition.utils.error.ErrorCode;
import com.example.nationalpetition.utils.error.exception.ForbiddenException;
import com.example.nationalpetition.utils.error.exception.NotFoundException;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;


@RequiredArgsConstructor
@Service
public class CommentService {

    private static final int commentDepthLimit = 2;

    private final CommentRepository commentRepository;
    private final LikeCommentRepository likeCommentRepository;
    private final BoardRepository boardRepository;
    private final MemberRepository memberRepository;
    private final AlarmService alarmService;

    @Transactional
    public Long addComment(CommentCreateDto dto, Long boardId, Long memberId) {
        Board board = BoardServiceUtils.findBoardById(boardRepository, boardId);
        Member member = memberRepository.findById(memberId).orElseThrow( () -> new NotFoundException("멤버를 찾을 수 없어요", ErrorCode.NOT_FOUND_EXCEPTION_COMMENT));


        if (dto.getParentId() == null) {
            board.countRootComments();
            final Long commentId = commentRepository.save(Comment.newRootComment(member, boardId, dto.getContent())).getId();
            alarmService.createCommentAlarm(board, member.getNickName(), commentId);
            return commentId;
        }

        Comment parentComment = CommentServiceUtils.findCommentById(commentRepository, dto.getParentId());

        int depth = parentComment.getDepth();

        if (depth == commentDepthLimit) {
            throw new ForbiddenException(String.format("더 이상 댓글 (%s) 을 생성할 수 없습니다.", parentComment.getId()), ErrorCode.FORBIDDEN_COMMENT_EXCEPTION);
        }

        parentComment.countChildComments();

        final Long commentId = commentRepository.save(Comment.newChildComment(dto.getParentId(), member, board.getId(), depth + 1, dto.getContent())).getId();
        alarmService.createReplyCommentAlarm(board, member.getNickName(), parentComment.getId(), commentId);
        return commentId;
    }

    @Transactional
    public Comment updateComment(Long memberId, CommentUpdateDto updateDto) {
        Comment comment = commentRepository.findByIdAndMemberIdAndIsDeletedIsFalse(updateDto.getCommentId(), memberId);
        if (comment == null) {
            throw new NotFoundException(String.format("멤버(%s)에게 해당하는 댓글(%s)은 존재하지 않습니다", memberId, updateDto.getCommentId()), ErrorCode.NOT_FOUND_EXCEPTION_COMMENT);
        }
        comment.update(updateDto.getContent());
        return comment;
    }

    @Transactional
    public void deleteComment(Long memberId, Long commentId) {
        Comment comment = CommentServiceUtils.findCommentByCommentIdAndMemberId(commentRepository, commentId, memberId);
        Board board = BoardServiceUtils.findBoardById(boardRepository, comment.getBoardId());
        decreaseCounts(comment, board);
        comment.delete();
    }

    private void decreaseCounts(Comment comment, Board board) {
        if (comment.isRootComment()) {
            board.decrementLikeCounts();
            return;
        }
        Comment parentComment = commentRepository.findById(comment.getParentId())
                .orElseThrow(() -> new NotFoundException(String.format("해당하는 부모댓글 (%s)은 존재하지 않습니다", comment.getParentId()), ErrorCode.NOT_FOUND_EXCEPTION_COMMENT));
        parentComment.decreaseChildCommentsCounts();
    }

    @Transactional
    public void addStatus(Long memberId, LikeCommentRequestDto requestDto) {

        validateExistedComment(requestDto);

        LikeComment likeComment = likeCommentRepository
                .findByIdAndMemberId(requestDto.getCommentId(), memberId);

        if (likeComment != null) {
            likeComment.update(requestDto.getLikeCommentStatus());
            return;
        }

        likeCommentRepository.save(LikeComment.of(requestDto.getCommentId(),
                requestDto.getLikeCommentStatus(), memberId));
    }

    @Transactional
    public void deleteStatus(Long memberId, LikeCommentRequestDto requestDto) {
        validateExistedComment(requestDto);

        LikeComment likeComment = likeCommentRepository
                .findByIdAndMemberIdAndLikeCommentStatus(requestDto.getCommentId(), memberId,
                        requestDto.getLikeCommentStatus());

        if (likeComment == null) {
            throw new NotFoundException(String.format("멤버 (%s)는 댓글(%s)에 좋아요를 누르지 않았습니다.",memberId, requestDto.getCommentId()), ErrorCode.NOT_FOUND_EXCEPTION_COMMENT);
        }

        if (likeComment.getLikeCommentStatus() == requestDto.getLikeCommentStatus()) {
            likeCommentRepository.deleteById(likeComment.getId());
        }

    }

    private void validateExistedComment(LikeCommentRequestDto requestDto) {
        commentRepository.findById(requestDto.getCommentId())
                .orElseThrow(() -> new NotFoundException(String.format("해당하는 댓글 (%s)은 존재하지 않습니다", requestDto.getCommentId()), ErrorCode.NOT_FOUND_EXCEPTION_COMMENT));
    }

    @Transactional(readOnly = true)
    public CommentPageResponseDto pageRequest(int page, int size, Long boardId) {
        Page<Comment> commentList = commentRepository.findAllRootCommentByBoardId(PageRequest.of(page - 1, size), boardId);
        return CommentPageResponseDto.builder()
                .contents(commentList.stream()
                        .map(CommentDto::of)
                        .collect(Collectors.toList()))
                .totalPages(commentList.getTotalPages())
                .totalElements(commentList.getTotalElements())
                .build();
    }

    @Transactional
    public CommentPageResponseDto replyCommentRequest(int page, int size, Long parentId) {
        Page<Comment> comments = commentRepository.findAllChildCommentByCommentId(PageRequest.of(page-1, size), parentId);
        return CommentPageResponseDto.builder()
                .contents(comments.stream()
                        .map(CommentDto::of)
                        .collect(Collectors.toList()))
                .totalPages(comments.getTotalPages())
                .totalElements(comments.getTotalElements())
                .build();
    }

}