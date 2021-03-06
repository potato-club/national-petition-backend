package com.example.nationalpetition.service.comment;

import com.example.nationalpetition.domain.board.Board;
import com.example.nationalpetition.domain.board.repository.BoardRepository;
import com.example.nationalpetition.domain.comment.*;
import com.example.nationalpetition.domain.member.entity.Member;
import com.example.nationalpetition.domain.member.repository.MemberRepository;
import com.example.nationalpetition.domain.notification.NotificationTemplate;
import com.example.nationalpetition.dto.comment.request.CommentCreateDto;
import com.example.nationalpetition.dto.comment.CommentDto;
import com.example.nationalpetition.dto.comment.request.CommentUpdateDto;
import com.example.nationalpetition.dto.comment.request.LikeCommentRequestDto;
import com.example.nationalpetition.dto.comment.response.CommentPageResponseDto;
import com.example.nationalpetition.dto.notification.NotificationEvent;
import com.example.nationalpetition.service.board.BoardServiceUtils;
import com.example.nationalpetition.utils.error.ErrorCode;
import com.example.nationalpetition.utils.error.exception.ForbiddenException;
import com.example.nationalpetition.utils.error.exception.NotFoundException;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


@RequiredArgsConstructor
@Service
public class CommentService {

    private static final int commentDepthLimit = 2;

    private final ApplicationEventPublisher eventPublisher;
    private final CommentRepository commentRepository;
    private final LikeCommentRepository likeCommentRepository;
    private final BoardRepository boardRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public Long addComment(CommentCreateDto dto, Long boardId, Long memberId) {
        Board board = BoardServiceUtils.findBoardById(boardRepository, boardId);
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new NotFoundException("????????? ?????? ??? ?????????", ErrorCode.NOT_FOUND_EXCEPTION_COMMENT));

        if (dto.getParentId() == null) {
            board.countRootComments();
            if (member.isNotification() && !board.getMemberId().equals(memberId)) {
                eventPublisher.publishEvent(NotificationEvent.of(board.getMemberId(), board.getId(), NotificationTemplate.CREATE_COMMENT, member.getNickName()));
            }
            return commentRepository.save(Comment.newRootComment(member, boardId, dto.getContent())).getId();
        }

        Comment parentComment = CommentServiceUtils.findCommentById(commentRepository, dto.getParentId());

        int depth = parentComment.getDepth();

        if (depth == commentDepthLimit) {
            throw new ForbiddenException(String.format("??? ?????? ?????? (%s) ??? ????????? ??? ????????????.", parentComment.getId()), ErrorCode.FORBIDDEN_COMMENT_EXCEPTION);
        }

        parentComment.countChildComments();
        if (member.isNotification() && !parentComment.getMember().getId().equals(memberId)) {
            eventPublisher.publishEvent(NotificationEvent.of(parentComment.getMember().getId(), board.getId(), NotificationTemplate.CREATE_RE_COMMENT, member.getNickName()));
        }
        return commentRepository.save(Comment.newChildComment(dto.getParentId(), member, board.getId(), depth + 1, dto.getContent())).getId();
    }

    @Transactional
    public Comment updateComment(Long memberId, CommentUpdateDto updateDto) {
        Comment comment = commentRepository.findByIdAndMemberIdAndIsDeletedIsFalse(updateDto.getCommentId(), memberId);
        if (comment == null) {
            throw new NotFoundException(String.format("??????(%s)?????? ???????????? ??????(%s)??? ???????????? ????????????", memberId, updateDto.getCommentId()), ErrorCode.NOT_FOUND_EXCEPTION_COMMENT);
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
                .orElseThrow(() -> new NotFoundException(String.format("???????????? ???????????? (%s)??? ???????????? ????????????", comment.getParentId()), ErrorCode.NOT_FOUND_EXCEPTION_COMMENT));
        parentComment.decreaseChildCommentsCounts();
    }

    @Transactional
    public void addStatus(Long memberId, LikeCommentRequestDto requestDto) {
        Comment comment = CommentServiceUtils.findCommentById(commentRepository, requestDto.getCommentId());

        LikeComment likeComment = likeCommentRepository
                .findByIdAndMemberId(comment, memberId);

        if (likeComment != null) {
            likeComment.update(requestDto.getLikeCommentStatus());
            return;
        }

        likeCommentRepository.save(LikeComment.of(comment,
                requestDto.getLikeCommentStatus(), memberId));
    }

    @Transactional
    public void deleteStatus(Long memberId, LikeCommentRequestDto requestDto) {
        Comment comment = CommentServiceUtils.findCommentById(commentRepository, requestDto.getCommentId());

        LikeComment likeComment = likeCommentRepository
                .findByIdAndMemberIdAndLikeCommentStatus(comment, memberId,
                        requestDto.getLikeCommentStatus());

        if (likeComment == null) {
            throw new NotFoundException(String.format("?????? (%s)??? ??????(%s)??? ???????????? ????????? ???????????????.", memberId, requestDto.getCommentId()), ErrorCode.NOT_FOUND_EXCEPTION_COMMENT);
        }

        if (likeComment.getLikeCommentStatus() == requestDto.getLikeCommentStatus()) {
            likeCommentRepository.deleteById(likeComment.getId());
        }

    }

    @Transactional
    public CommentPageResponseDto replyCommentRequest(int page, int size, Long parentId, Long memberId) {
        Page<Comment> comments = commentRepository.findAllChildCommentByCommentId(PageRequest.of(page - 1, size), parentId);
        return CommentPageResponseDto.builder()
                .contents(comments.stream()
                        .map(comment -> CommentDto.of(comment, memberId))
                        .collect(Collectors.toList()))
                .totalPages(comments.getTotalPages())
                .totalElements(comments.getTotalElements())
                .build();
    }

    @Transactional(readOnly = true)
    public List<CommentDto> commentRequest(Long boardId, int size, Long lastId, Long memberId) {
        if (lastId == null) {
            List<Comment> contents = commentRepository.findALlRootCommentsByBoardIdAndSize(boardId, size);
            return getComment(contents, memberId);
        }
        List<Comment> comments = commentRepository.findAllRootCommentByBoardIdAndSizeAndLastId(boardId, size, lastId);
        return getComment(comments, memberId);
    }


    private List<CommentDto> getComment(List<Comment> contents, Long memberId) {
        return contents.stream()
                .map(content -> CommentDto.of(content, memberId))
                .collect(Collectors.toList());
    }

}