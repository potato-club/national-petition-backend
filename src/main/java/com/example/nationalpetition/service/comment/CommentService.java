package com.example.nationalpetition.service.comment;

import com.example.nationalpetition.domain.comment.Comment;
import com.example.nationalpetition.domain.comment.CommentRepository;
import com.example.nationalpetition.dto.comment.CommentCreateDto;
import com.example.nationalpetition.dto.comment.request.CommentDeleteDto;
import com.example.nationalpetition.dto.comment.request.CommentRetrieveRequestDto;
import com.example.nationalpetition.dto.comment.request.CommentUpdateDto;
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
    public void deleteComment(Long memberId, CommentDeleteDto deleteDto) {
        Comment comment = commentRepository.findByIdAndMemberIdAndIsDeletedIsFalse(deleteDto.getCommentId(), memberId);
        if (comment == null) {
            throw new NotFoundException(ErrorCode.NOT_FOUND_EXCEPTION_COMMENT);
        }
        comment.delete();
    }

    @Transactional(readOnly = true)
    public List<CommentRetrieveResponseDto> retrieveComments(CommentRetrieveRequestDto dto) {
        List<Comment> comments = commentRepository.findByBoardIdAndIsDeletedIsFalse(dto.getCommentId());
        return comments.stream().map(CommentRetrieveResponseDto::of).collect(Collectors.toList());
    }

}