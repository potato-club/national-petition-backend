package com.example.nationalpetition.service.comment;

import com.example.nationalpetition.domain.comment.*;
import com.example.nationalpetition.dto.comment.CommentCreateDto;
import com.example.nationalpetition.dto.comment.request.CommentUpdateDto;
import com.example.nationalpetition.dto.comment.request.LikeCommentRequestDto;
import com.example.nationalpetition.utils.error.ErrorCode;
import com.example.nationalpetition.utils.error.exception.NotFoundException;
import com.example.nationalpetition.dto.comment.response.CommentRetrieveResponseDto;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;


@RequiredArgsConstructor
@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final LikeCommentRepository likeCommentRepository;

    @Transactional
    public Long addComment(CommentCreateDto dto, Long boardId, Long memberId) {
        if (dto.getParentId() == null) {
            return commentRepository.save(Comment.newRootComment(memberId, boardId, dto.getContent())).getId();
        }
        Comment parentComment = commentRepository.findById(dto.getParentId())
                .orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND_EXCEPTION_COMMENT));
        int depth = parentComment.getDepth();
        return commentRepository.save(Comment.newChildComment(dto.getParentId(), memberId, boardId, depth + 1, dto.getContent())).getId();
    }

    @Transactional
    public Comment updateComment(Long memberId, CommentUpdateDto updateDto) {

        Comment comment = commentRepository.findByIdAndMemberIdAndIsDeletedIsFalse(updateDto.getCommentId(), memberId);
        if (comment == null) {
            throw new NotFoundException(ErrorCode.NOT_FOUND_EXCEPTION_COMMENT);
        }
        comment.update(updateDto.getContent());
        return comment;
    }

    @Transactional
    public void deleteComment(Long memberId, Long commentId) {
        Comment comment = commentRepository.findByIdAndMemberIdAndIsDeletedIsFalse(commentId, memberId);
        if (comment == null) {
            throw new NotFoundException(ErrorCode.NOT_FOUND_EXCEPTION_COMMENT);
        }
        comment.delete();
    }

    @Transactional(readOnly = true)
    public List<CommentRetrieveResponseDto> retrieveComments(Long boardId) {
        List<Comment> comments = commentRepository.findByBoardIdAndIsDeletedIsFalse(boardId);
        return comments.stream().map(CommentRetrieveResponseDto::of).collect(Collectors.toList());
    }

    @Transactional
    public LikeComment addStatus(Long memberId, LikeCommentRequestDto requestDto) {
        commentRepository.findById(requestDto.getCommentId())
                .orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND_EXCEPTION_COMMENT));

        LikeComment likeComment = likeCommentRepository
                .findByIdAndLikeCommentStatus(requestDto.getCommentId(), requestDto.getLikeCommentStatus());

        if (likeComment != null) {
            likeComment.update(likeComment.getLikeCommentStatus());
        }

        return likeCommentRepository.save(LikeComment.of(requestDto.getCommentId(),
                requestDto.getLikeCommentStatus(), memberId));
    }

    @Transactional
    public void deleteStatus(Long memberId, LikeCommentRequestDto requestDto) {
        commentRepository.findById(requestDto.getCommentId())
                .orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND_EXCEPTION_COMMENT));
        LikeComment likeComment = likeCommentRepository
                .findByIdAndLikeCommentStatus(requestDto.getCommentId(), requestDto.getLikeCommentStatus());

        if (likeComment == null) {
            throw new NotFoundException(ErrorCode.NOT_FOUND_EXCEPTION_COMMENT);
        }

        if (likeComment.getLikeCommentStatus() == requestDto.getLikeCommentStatus()) {
            likeCommentRepository.deleteById(likeComment.getId());
        }

    }

}