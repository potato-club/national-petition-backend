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

import javax.transaction.Transactional;
import java.util.List;

@RequiredArgsConstructor
@Service
public class CommentService {

    private final CommentRepository commentRepository;

    @Transactional
    public Long addComment(CommentCreateDto dto, Long boardId) {
        if (dto.getParentId() == null) {
            return commentRepository.save(Comment.newRootComment(dto.getMemberId(), boardId, dto.getContent())).getId();
        }
        Comment parentComment = commentRepository.findById(dto.getParentId())
                .orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND_EXCEPTION_COMMENT));
        int depth = parentComment.getDepth();
        return commentRepository.save(Comment.newChildComment(dto.getParentId(), dto.getMemberId(), boardId, depth + 1, dto.getContent())).getId();
    }

    @Transactional
    public Comment updateComment(CommentUpdateDto updateDto) {

        Comment comment = commentRepository.findByIdAndMemberIdAndIsDeletedIsFalse(updateDto.getId(), updateDto.getMemberId());
        if (comment == null) {
            throw new NotFoundException(ErrorCode.NOT_FOUND_EXCEPTION_COMMENT);
        }
        comment.update(updateDto.getContent());
        return comment;
    }

    @Transactional
    public Comment deleteComment(CommentDeleteDto deleteDto) {
        Comment comment = commentRepository.findByIdAndMemberIdAndIsDeletedIsFalse(deleteDto.getCommentId(), deleteDto.getMemberId());
        if (comment == null) {
            throw new NotFoundException(ErrorCode.NOT_FOUND_EXCEPTION_COMMENT);
        }
        comment.delete();
        return comment;
    }

//    @Transactional
//    public List<CommentRetrieveResponseDto> retrieveComments(CommentRetrieveRequestDto dto) {
//        List<Comment> comments = commentRepository.findByBoardIdAndIsDeletedIsFalse(dto.getBoardId());
//        // comments.stream().collect(Collectors.toList())
//
//        return comments;
//    }


}