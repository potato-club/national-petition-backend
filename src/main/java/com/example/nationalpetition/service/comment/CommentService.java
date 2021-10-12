package com.example.nationalpetition.service.comment;

import com.example.nationalpetition.domain.board.Board;
import com.example.nationalpetition.domain.board.repository.BoardRepository;
import com.example.nationalpetition.domain.comment.*;
import com.example.nationalpetition.dto.comment.request.CommentCreateDto;
import com.example.nationalpetition.dto.comment.CommentDto;
import com.example.nationalpetition.dto.comment.request.CommentUpdateDto;
import com.example.nationalpetition.dto.comment.request.LikeCommentRequestDto;
import com.example.nationalpetition.dto.comment.response.CommentPageResponseDto;
import com.example.nationalpetition.utils.error.ErrorCode;
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

    private final CommentRepository commentRepository;
    private final LikeCommentRepository likeCommentRepository;
    private final BoardRepository boardRepository;

    @Transactional
    public Long addComment(CommentCreateDto dto, Long boardId, Long memberId) {
        Board board = boardRepository.findById(boardId).orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND_EXCEPTION_BOARD));

        if (dto.getParentId() == null) {
            board.countRootComments();
            return commentRepository.save(Comment.newRootComment(memberId, boardId, dto.getContent())).getId();
        }

        Comment parentComment = commentRepository.findById(dto.getParentId())
                .orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND_EXCEPTION_COMMENT));

        int depth = parentComment.getDepth();

        parentComment.countChildComments();

        return commentRepository.save(Comment.newChildComment(dto.getParentId(), memberId, board.getId(), depth + 1, dto.getContent())).getId();
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
        boardRepository.findById(comment.getBoardId()).orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND_EXCEPTION_BOARD));

        comment.delete();
    }

//    @Transactional(readOnly = true)
//    public List<CommentRetrieveResponseDto> retrieveComments(Long boardId) {
//        List<Comment> comments = commentRepository.findByBoardIdAndIsDeletedIsFalse(boardId);
//
//        return comments.stream().map(CommentRetrieveResponseDto::of).collect(Collectors.toList());
//    }

    @Transactional
    public LikeComment addStatus(Long memberId, LikeCommentRequestDto requestDto) {

        validateExistedComment(requestDto);

        LikeComment likeComment = likeCommentRepository
                .findByIdAndMemberId(requestDto.getCommentId(), memberId);

        if (likeComment != null) {
            likeComment.update(requestDto.getLikeCommentStatus());
            return likeComment;
        }

        return likeCommentRepository.save(LikeComment.of(requestDto.getCommentId(),
                requestDto.getLikeCommentStatus(), memberId));
    }

    @Transactional
    public void deleteStatus(Long memberId, LikeCommentRequestDto requestDto) {
        validateExistedComment(requestDto);

        LikeComment likeComment = likeCommentRepository
                .findByIdAndMemberIdAndLikeCommentStatus(requestDto.getCommentId(), memberId,
                        requestDto.getLikeCommentStatus());

        if (likeComment == null) {
            throw new NotFoundException(ErrorCode.NOT_FOUND_EXCEPTION_COMMENT);
        }

        if (likeComment.getLikeCommentStatus() == requestDto.getLikeCommentStatus()) {
            likeCommentRepository.deleteById(likeComment.getId());
        }

    }

    private void validateExistedComment(LikeCommentRequestDto requestDto) {
        commentRepository.findById(requestDto.getCommentId())
                .orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND_EXCEPTION_COMMENT));
    }

    @Transactional(readOnly = true)
    public CommentPageResponseDto pageRequest(int page, int size, Long boardId) {
        Page<Comment> commentList = commentRepository.findAllRootCommentByBoardId(PageRequest.of(page, size), boardId);
        return CommentPageResponseDto.builder()
                .contents(commentList.stream()
                        .map(CommentDto::of)
                        .collect(Collectors.toList()))
                .totalPages(commentList.getTotalPages())
                .totalElements(commentList.getTotalElements())
                .build();
    }

}